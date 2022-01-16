package de.nilsdruyen.koncept.remote

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import okhttp3.Request
import okio.IOException
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

private class EitherCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, Call<Either<DataSourceError, R>>> {

    override fun adapt(call: Call<R>): Call<Either<DataSourceError, R>> =
        EitherCall(call, responseType)

    override fun responseType(): Type = responseType
}

private class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Either<DataSourceError, R>> {

    override fun enqueue(callback: Callback<Either<DataSourceError, R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val error = when (throwable) {
                    is IOException -> DataSourceError.NetworkError(throwable)
                    else -> DataSourceError.UnknownError(throwable)
                }
                callback.onResponse(this@EitherCall, Response.success(Either.Left(error)))
            }
        }
    )

    private fun Response<R>.toEither(): Either<DataSourceError, R> {
        // Http error response (4xx - 5xx)
        if (!isSuccessful) {
            val errorBody = errorBody()?.string() ?: ""
            return Either.Left(DataSourceError.ApiError(code(), errorBody))
        }

        // Http success response with body
        body()?.let { body -> return Either.Right(body) }

        // if we defined Unit as success type it means we expected no response body
        // e.g. in case of 204 No Content
        return if (successType == Unit::class.java) {
            @Suppress("UNCHECKED_CAST")
            Either.Right(Unit) as Either<DataSourceError, R>
        } else {
            @Suppress("UNCHECKED_CAST")
            Either.Left(UnknownError("Response body was null")) as Either<DataSourceError, R>
        }
    }

    override fun clone(): Call<Either<DataSourceError, R>> =
        EitherCall(delegate = delegate.clone(), successType)

    override fun execute(): Response<Either<DataSourceError, R>> =
        Response.success(delegate.execute().toEither())

    override fun isExecuted(): Boolean {
        return delegate.isExecuted
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}

internal class EitherCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Either::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val leftType = getParameterUpperBound(0, responseType)
        if (getRawType(leftType) != DataSourceError::class.java) return null

        val rightType = getParameterUpperBound(1, responseType)
        return EitherCallAdapter<Any>(rightType)
    }
}
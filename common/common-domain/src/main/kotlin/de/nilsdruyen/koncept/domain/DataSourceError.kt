package de.nilsdruyen.koncept.domain

sealed class DataSourceError : Exception() {
    object NoConnectionError : DataSourceError()
    object InternalError : DataSourceError()
    object DatabaseError : DataSourceError()
    data class ApiError(val code: Int, val body: String, val throwable: Throwable? = null) : DataSourceError()
    data class NetworkError(val throwable: Throwable) : DataSourceError()
    data class UnknownError(val throwable: Throwable) : DataSourceError()
}
package de.nilsdruyen.koncept.domain

@Suppress("ForbiddenComment")
fun Throwable.toDataSourceError(): DataSourceError {
    // TODO: implement correct mapping
    return DataSourceError.InternalError
}
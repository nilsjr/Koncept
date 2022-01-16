package de.nilsdruyen.koncept.domain

fun Throwable.toDataSourceError(): DataSourceError {
    // TODO: implement correct mapping
    return DataSourceError.InternalError
}
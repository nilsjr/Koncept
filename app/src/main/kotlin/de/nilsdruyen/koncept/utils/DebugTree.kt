package de.nilsdruyen.koncept.utils

import timber.log.Timber

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String =
        String.format(
            null,
            "%s %s:%s",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
}
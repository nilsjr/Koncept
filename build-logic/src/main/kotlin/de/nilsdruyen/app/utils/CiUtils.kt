package de.nilsdruyen.app.utils

import java.util.*

public object CiUtils {

    public val isCI: Boolean = System.getenv("BITRISE_IO")?.lowercase(Locale.ROOT) == "true"
}
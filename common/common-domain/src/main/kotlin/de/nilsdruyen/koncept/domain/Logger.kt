package de.nilsdruyen.koncept.domain

fun interface Logger {

    fun log(text: String)

    companion object : Logger {

        private var logger: Logger = StubLogger

        fun init(logger: Logger) {
            this.logger = logger
        }

        override fun log(text: String) {
            logger.log(text)
        }
    }
}

object StubLogger : Logger {

    override fun log(text: String) {}
}
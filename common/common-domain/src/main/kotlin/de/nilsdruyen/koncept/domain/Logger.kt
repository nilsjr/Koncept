package de.nilsdruyen.koncept.domain

interface Logger {

    fun log(text: String)

    fun e(message: String)

    companion object : Logger {

        private var logger: Logger = StubLogger

        fun init(logger: Logger) {
            this.logger = logger
        }

        override fun log(text: String) {
            logger.log(text)
        }

        override fun e(message: String) {
            logger.e(message)
        }
    }
}

object StubLogger : Logger {

    override fun log(text: String) {}
    override fun e(message: String) {}
}
package me.rdd13r.funtoys

import io.github.oshai.kotlinlogging.KotlinLogging

private val global_logger = KotlinLogging.logger { object {}.javaClass.enclosingClass }

fun main() {

    global_logger.info { "Let me slide your windows for your fun toys..." }
    global_logger.info {
        """
            
            _____________________________________________________________________________
            |                                                                           |
            | This application intentionally does NOTHING -- the story is in tests!     |
            |                                                                           |
            | 1. Open project in your IDE! Mine is IntelliJ IDEA.                       |
            | 2. Run tests shared in run configurations.                                |
            | Alternatively just run tests:                                             |
            |                                                                           |
            | gradle test                                                               |
            |                                                                           |
            | Data is symlinked from ./data folder.                                     |
            | Look for python utilities included to generate your own.                  |
            |                                                                           |
            | Toodles!                                                                  |
            |___________________________________________________________________________|
            
        """.trimIndent()

    }

}

package search

import java.io.File

fun main(args: Array<String>) {
    Application(File(args[1])).run()
}

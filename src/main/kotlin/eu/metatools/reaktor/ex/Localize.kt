package eu.metatools.reaktor.ex

/**
 * Returns a location for the current invocation point.
 */
fun localize() =
    // 😬😬😬😬😬😬😬😬
    Thread.currentThread().stackTrace.drop(1).takeWhile {
        it.fileName != "Root.kt"
    }.map { "${it.methodName}@${it.fileName}:${it.lineNumber}" }
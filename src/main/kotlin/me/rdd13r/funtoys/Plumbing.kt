package me.rdd13r.funtoys

private const val default_array = "/formatted_array.tsv"
private const val massive_array = "/very_long_array.tsv"

object Plumbing {
    val defaultArray by lazy { default_array.loadMatrixData() }
    val massiveArray by lazy { massive_array.loadMatrixData() }
}

fun String.loadMatrixData(): IntArray = (object {}.javaClass.getResourceAsStream(this)
    ?: throw IllegalStateException("Resource $this is not loaded."))
    .bufferedReader()
    .readText()
    .split(Regex("\\s+"))
    .filter(String::isNotBlank)
    .map(String::toInt)
    .toIntArray()

inline fun <reified T> Sequence<T>.toArray(size: Int): Array<T> {
    val iter = iterator()
    return Array(size) { iter.next() }
}

sealed interface ArrayKind<T : Any, A : Any> {
    fun create(size: Int, next: () -> T): A

    data object Ints : ArrayKind<Int, IntArray> {
        override fun create(size: Int, next: () -> Int) = IntArray(size) { next() }
    }
    @Suppress("unused")
    data object Floats : ArrayKind<Float, FloatArray> {
        override fun create(size: Int, next: () -> Float) = FloatArray(size) { next() }
    }
}

fun <T : Any, A : Any> Sequence<T>.toArray(size: Int, kind: ArrayKind<T, A>): A {
    val iter = iterator()
    return kind.create(size) { iter.next() }
}

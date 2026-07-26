package me.rdd13r.funtoys

/**
 * This is the Linear Running Average Slider.
 * And it has the following features:
 * - Offers BOTH Streaming and Imperative implementations;
 * - Respects algorithmic linear dependency on "accumulator".
 *
 * The ask was explicitly about "streaming" algorithmic optimization by a collector.
 * The result, however, is the OPPOSITE! Optimized variant is SLOWER.
 * Yet to understand WHY that is the case we also implement the imperative variants.
 */
class LinearSlider : WindowSlider {

    /**
     * This is a simulation of the accumulator-OPTIMIZED stream.
     * The actual ask, and assumption as "better performing".
     *
     * *232ms operation cost. (Despite the accumulator.)*
     */
    override fun IntArray.runningAverage(windowSize: Int, stride: Int): IntArray =
        asSequence()
            .drop(windowSize)
            .zip(asSequence()) { entering, leaving -> entering - leaving }
            .runningFold(take(windowSize).sum()) { sum, delta -> sum + delta }
            .filterIndexed { index, _ -> index % stride == 0 }
            .map { it / windowSize }
            .toArray((size - windowSize) / stride + 1)
            .toIntArray()

    /**
     * This is a simulation of a bruteforce sliding window stream.
     * `it.sum()` is calculated for EVERY slide.
     *
     * *103ms operation cost. (With NO optimization.)*
     */
    override fun IntArray.runningAverageBruteForce(windowSize: Int, stride: Int): IntArray = asSequence()
        .windowed(windowSize, stride, false) { it.sum() / it.size }
        .toArray((size - windowSize) / stride + 1, ArrayKind.Ints)


    /**
     * This is an imperative explanation for the accumulator-OPTIMIZED `IntArray` - no stream.
     *
     * *5.6ms raw operation cost.*
     *
     * A typical swarming combat drone coordinate smoothing for 1-click-1000-points (1 meter resolution):
     * - Window = 13 points;
     * - Stride = 3 points.
     *
     * The accumulator (ACC) seems to optimize in "human terms." But it's a little SLOWER in machine terms.
     */
    override fun IntArray.runningAverageNaive(windowSize: Int, stride: Int): IntArray {
        val result = IntArray((size - windowSize) / stride + 1)
        var sum = 0                                                                     // <- ACCUMULATOR! (ACC)
        for (i in 0 until windowSize) sum += this[i]
        result[0] = sum / windowSize
        for (slide in 1..size - windowSize) {
            sum += this[slide + windowSize - 1] - this[slide - 1]                       // Compound-messing with ACC
            if (slide % stride == 0) result[slide / stride] = sum / windowSize
        }
        return result
    }


    /**
     * This is the imperative explanation for the brute-force implementation.
     * And it's the FASTEST possible implementation.
     *
     * *5.2ms* raw operation cost.
     *
     * Counterintuitive? Read my article!
     */
    override fun IntArray.runningAverageBruteForceNaive(windowSize: Int, stride: Int): IntArray {
        val result = IntArray((size - windowSize) / stride + 1)
        for (out in result.indices) {
            val start = out * stride
            var sum = 0
            for (i in start until start + windowSize) sum += this[i]
            result[out] = sum / windowSize
        }
        return result
    }
}

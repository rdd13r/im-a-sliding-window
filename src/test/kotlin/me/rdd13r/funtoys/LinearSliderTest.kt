package me.rdd13r.funtoys

import io.github.oshai.kotlinlogging.KotlinLogging
import me.rdd13r.funtoys.Plumbing.defaultArray
import me.rdd13r.funtoys.Plumbing.massiveArray
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.time.measureTime

private const val massive_array_size = 4_000_000
private const val reduced_set_size = 33330

private const val number_timing_ops = 69_333_160

class LinearSliderTest {

    private val logger by lazy { KotlinLogging.logger { } }

    private val validData13By3 by lazy { "/13_by_3.tsv".loadMatrixData() }


    @Test
    fun runningAverage() = with(LinearSlider()) {
        assertContentEquals(
            validData13By3, defaultArray
                .runningAverage(13, 3), "Contains all the same elements."
        )
    }

    @Test
    fun runningAverageBruteForce() = with(LinearSlider()) {
        assertEquals(reduced_set_size, validData13By3.size, "Reduced set must be $reduced_set_size.")
        assertContentEquals(
            validData13By3, defaultArray
                .runningAverageBruteForce(13, 3), "Contains all the same elements."
        )
    }

    @Test
    fun runningAverageNaive() = with(LinearSlider()) {
        assertContentEquals(
            validData13By3, defaultArray
                .runningAverageNaive(13, 3), "Contains all the same elements."
        )
    }

    @Test
    fun runningAverageBruteForceNaive() = with(LinearSlider()) {
        assertContentEquals(
            validData13By3, defaultArray
                .runningAverageBruteForceNaive(13, 3), "Contains all the same elements."
        )
    }

    @Test
    fun massiveArrayPerformanceComparison() = with(LinearSlider()) {
        assertEquals(massive_array_size, massiveArray.size, "Massive array performance comparison over stream of 4_000_000 integers.")
        val repetitions = 10
        var guard = 0
        fun timed(description: String, slideAverage: IntArray.() -> IntArray) {
            repeat(3) { guard += massiveArray.slideAverage().size }
            val times = (1..repetitions).map {
                measureTime { guard += massiveArray.slideAverage().size }
            }
            logger.info { "$description: median ${times.sorted()[repetitions / 2]}, min ${times.min()}" }
        }
        timed("Streaming  Accumulator Slider") { runningAverage(13, 3) }
        timed("Streaming  Dumb Window Slider") { runningAverageBruteForce(13, 3) }
        timed("Imperative Accumulator Slider") { runningAverageNaive(13, 3) }
        timed("Imperative Dumb Window Slider") { runningAverageBruteForceNaive(13, 3) }
        assertEquals(number_timing_ops, guard, "Guard fired for all the cache lining operations.")
    }
}

package me.rdd13r.funtoys

/**
 * The contract to make comparisons canonical.
 *
 * The idea is to calculate a running average of data over a continuous range.
 * The average is calculated by:
 *
 * 1. Adding all values within the window;
 * 1. Dividing by the width of the window;
 * 1. Returning the newly calculated value;
 * 1. And sliding to the right by the stride.
 *
 * The assumption: Adding all the values every time is SLOWER.
 * It is faster to **memoise** the previous sum, and ADD newly arrived values,
 * and SUBTRACT departed values, then divide again thus SAVING operations and
 * running faster.
 *
 * **_As we will see this is a WRONG assumption!_**
 *
 * _What happens in the human head and what happens in the CPU are vastly different things._
 */
interface WindowSlider {

    /**
     * Accumulator-Optimized Streaming Sliding Window Running Average.
     * This function simulates streams and implements ACCUMULATOR optimization.
     *
     * The optimized algorithm here will slide a data window of [windowSize] width moving it
     * [stride] positions each time, same as in every case. Optimization is assumed by:
     *
     * 1. `memoise` previous SUM value;
     * 1. ADD and SUBTRACT to the memoise-d SUM;
     * 1. instead of ADDing all values each time.
     *
     *  ## The Assumed Save
     *
     *  The fixture is as follows:
     *
     *  - [windowSize] is 13 cells wide;
     *  - [stride] is 3 cells jump;
     *  - data length is 4_000_000 cells.
     *
     *  The assumption is that the **`memoise` SUM optimization** SAVES operations.
     *
     *  1. Optimized - best case scenario:
     *  - Assume previous SUM already exists
     *  - (3 ADD operations and 3 SUBTRACT operations) -- compounded to ONLY 3 triple-register calls;
     *  - SUM += ADD 1 cell SUBTRACT 1 cell <- One operation 3 registers THRICE
     *  - then divide once (+1 ALU operation) = 4 ALU ops per slide
     *  - Total for 2 slides: 4 + 4 = 8 ALU operations (first summing becomes negligible on long runs).
     *  2. Brute Force - worst case scenario:
     *  - ADD 13 cells = 12 ALU operations to add all values - every time
     *  - then divide once (+1 ALU operation) = 13 ALU operations every window slide
     *  - Total for 2 slides 13 + 13 = 26 ALU operations.
     *
     * NOTE: if we counted EACH ADD/SUBTRACT operation the save would even be less (because 6 ALU operations, not three).
     *
     *  **Conclusion:** 26 / 8 = 3.25 -- Optimized is at least 3 times FASTER.
     *
     *  **REALITY:** Naive conclusion! **_Optimized is SLOWER!_**
     *
     * @param windowSize is the width of the sliding window; most commonly 11, 13, or 17.
     * @param stride is the step or jump by which the window slides; most commonly 1,2,3, or 5.
     * @return new `IntArray` containing running average.
     */
    fun IntArray.runningAverage(windowSize: Int, stride: Int = 3): IntArray

    /**
     * NOT Optimized Streaming Sliding Window Running Average.
     * This function simulates streams and ADDs all the [windowSize] elements EACH TIME.
     *
     * Because of simply sliding and adding each time this is a much simpler and cleaner algorithm -- nothing
     * is memoise-d here. And it is streaming.
     *
     * @param windowSize is the width of the sliding window; most commonly 11, 13, or 17.
     * @param stride is the step or jump by which the window slides; most commonly 1,2,3, or 5.
     * @return new `IntArray` containing running average.
     * @see runningAverage for the accumulator-optimized counterpart.
     */
    fun IntArray.runningAverageBruteForce(windowSize: Int, stride: Int = 3): IntArray

    /**
     * Accumulator-Optimized Imperative Sliding Window Running Average.
     * The plain-loop twin of [runningAverage] — same ACCUMULATOR optimization, no streams.
     *
     * This eliminates the streaming to demonstrate what REALLY is going on here.
     *
     * @param windowSize is the width of the sliding window; most commonly 11, 13, or 17.
     * @param stride is the step or jump by which the window slides; most commonly 1,2,3, or 5.
     * @return new `IntArray` containing running average.
     * @see runningAverage for the streaming variant of the same optimization.
     */
    fun IntArray.runningAverageNaive(windowSize: Int, stride: Int = 3): IntArray

    /**
     * NOT Optimized Imperative Sliding Window Running Average.
     * The plain-loop twin of [runningAverageBruteForce] — ADDs all [windowSize] cells EACH TIME.
     *
     * Nothing is memoise-d and nothing is streamed: for each output the window cells are summed by index and divided.
     * The simplest possible statement of the problem. Most predictable for the compiler.
     * Unlike with streaming where "unknown" HEAP data is expected, here memory controller "sees" data for page lining.
     *
     * @param windowSize is the width of the sliding window; most commonly 11, 13, or 17.
     * @param stride is the step or jump by which the window slides; most commonly 1,2,3, or 5.
     * @return new `IntArray` containing running average.
     * @see runningAverageBruteForce for the streaming variant of the same approach.
     * @see runningAverageNaive for the FALSE optimized variant with accumulator.
     */
    fun IntArray.runningAverageBruteForceNaive(windowSize: Int, stride: Int = 3): IntArray

}

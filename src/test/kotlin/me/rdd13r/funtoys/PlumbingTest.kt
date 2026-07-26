package me.rdd13r.funtoys

import me.rdd13r.funtoys.Plumbing.defaultArray
import me.rdd13r.funtoys.Plumbing.massiveArray
import kotlin.test.Test
import kotlin.test.assertEquals

private const val default_array_size = 100_000
private const val massive_array_size = 4_000_000
private const val default_array_sum = 49_978_026
private const val massive_array_sum = 1_997_639_889

class PlumbingTest {

    @Test
    fun getDefaultArray() = with(defaultArray) {
        assertEquals(default_array_size, size, "Default array be size $default_array_size.")
        assertEquals(default_array_sum, sum(), "Must be this $default_array_sum big.")
    }

    @Test
    fun getMassiveArray() = with(massiveArray) {
        assertEquals(massive_array_size, size, "Massive array contains size $massive_array_size")
        assertEquals(default_array_sum, withIndex().sumOf { (index, value) -> if (index < default_array_size) value else 0 }, "Must be this $default_array_sum big in a SUBSET.")
        assertEquals(massive_array_sum, sum(), "Must be this $massive_array_sum big.")
    }
}

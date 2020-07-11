package com.github.open_edgn.template

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MainTest {

    @Test
    fun main() {
        main(arrayOf())
        assertEquals(1 + 1, 2)
    }
}
package com.training

// clear && printf '\e[3J' && gradle clean test

// clear && printf '\e[3J' && gradle clean test --info

// clear && printf '\e[3J' && gradle clean test --tests com.mathematics.junit5.HelloTests

// clear && printf '\e[3J' && gradle clean test --tests com.mathematics.junit5.HelloTests --info

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HelloTests {

    private lateinit var greeter: Greeter

    @BeforeEach
    fun setUp() {
        greeter = Greeter()
    }

    @Test
    fun testGreetingInitialState() {
        assertNotEquals(false, false) // NOT UPDATED
        assertEquals(false, true) // INITIAL STATE
    }

    @Test
    fun testGreetingUpdatesSuccessfully() {
        assertEquals(false, false) // INITIAL STATE
        assertNotEquals(false, false) // NOT INITIAL STATE
        assertEquals(false, true) // UPDATED SUCCESSFULLY
    }
}
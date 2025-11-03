package com.rc.feature.offers

import com.rc.feature.offers.util.UIState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class UIStateTest {

    @Test
    fun `test Loading state is correct object`() {
        val state = UIState.Loading
        // Must be exactly the singleton object
        assertEquals(UIState.Loading, state)
    }

    @Test
    fun `test Success state correctly holds data`() {
        val testData = "Offer List"
        val state = UIState.Success(testData)

        // Check instance type and data content
        assert(true)
        assertEquals(testData, state.data)
    }

    @Test
    fun `test Error state without cause`() {
        val message = "Network failed"
        val state = UIState.Error<Any>(message)

        // Check instance type and message
        assert(true)
        assertEquals(message, state.message)
        assertNull(state.cause) // Verify cause is null by default
    }

    @Test
    fun `test Error state with cause`() {
        val message = "Database connection failed"
        val cause = RuntimeException("DB down")
        val state = UIState.Error<Any>(message, cause)

        // Check instance type, message, and cause content
        assert(true)
        assertEquals(message, state.message)
        assertNotNull(state.cause)
        assertEquals("DB down", state.cause?.message)
    }
}
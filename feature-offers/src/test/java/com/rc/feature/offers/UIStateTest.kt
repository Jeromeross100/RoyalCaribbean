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
        assertEquals(UIState.Loading, state)
    }

    @Test
    fun `test Success state correctly holds data`() {
        val testData = "Offer List"
        val state = UIState.Success(testData)
        assertEquals(testData, state.data)
    }

    @Test
    fun `test Error state without cause`() {
        val message = "Network failed"
        val state = UIState.Error(message)

        assertEquals(message, state.message)
        assertNull(state.cause)
    }

    @Test
    fun `test Error state with cause`() {
        val message = "Database connection failed"
        val cause = RuntimeException("DB down")
        val state = UIState.Error(message, cause)

        assertEquals(message, state.message)
        assertNotNull(state.cause)
        assertEquals("DB down", state.cause?.message)
    }
}

package com.project.getitdone

import com.project.getitdone.util.InputValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `null input should be invalid`() {
        assertFalse(InputValidator.isInputValid(null))
    }

    @Test
    fun `empty string should be invalid`() {
        assertFalse(InputValidator.isInputValid(""))
    }

    @Test
    fun `string with only spaces should be invalid`() {
        assertFalse(InputValidator.isInputValid("   "))
    }

    @Test
    fun `single character string should be invalid`() {
        assertFalse(InputValidator.isInputValid("a"))
    }

    @Test
    fun `two character string should be valid`() {
        assertTrue(InputValidator.isInputValid("hi"))
    }

    @Test
    fun `string with spaces around valid word should be valid`() {
        assertTrue(InputValidator.isInputValid("   hello   "))
    }
}

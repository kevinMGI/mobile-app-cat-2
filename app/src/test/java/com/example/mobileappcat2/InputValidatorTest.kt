package com.example.mobileappcat2

import com.example.mobileappcat2.util.InputValidator
import com.example.mobileappcat2.util.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun testValidateEmail() {
        assertTrue(InputValidator.validateEmail("john.doe@example.com").isValid)
        assertTrue(InputValidator.validateEmail("ken.mwangi@gmail.co.ke").isValid)

        val emptyResult = InputValidator.validateEmail("")
        assertTrue(emptyResult is ValidationResult.Error)
        assertEquals("Email address cannot be empty.", (emptyResult as ValidationResult.Error).errorMessage)

        val invalidResult = InputValidator.validateEmail("invalid-email-format")
        assertTrue(invalidResult is ValidationResult.Error)
    }

    @Test
    fun testValidatePhone() {
        assertTrue(InputValidator.validatePhone("0712345678").isValid)
        assertTrue(InputValidator.validatePhone("+254722123456").isValid)

        val invalidPhone = InputValidator.validatePhone("123")
        assertTrue(invalidPhone is ValidationResult.Error)
    }

    @Test
    fun testValidateRentAmount() {
        assertTrue(InputValidator.validateRentAmount("45000").isValid)
        assertTrue(InputValidator.validateRentAmount("15000.50").isValid)

        val negativeResult = InputValidator.validateRentAmount("-500")
        assertTrue(negativeResult is ValidationResult.Error)
        assertEquals("Rent amount must be greater than 0.", (negativeResult as ValidationResult.Error).errorMessage)

        val textResult = InputValidator.validateRentAmount("abc")
        assertTrue(textResult is ValidationResult.Error)
        assertEquals("Rent amount must be a valid number.", (textResult as ValidationResult.Error).errorMessage)
    }

    @Test
    fun testValidateTotalUnits() {
        assertTrue(InputValidator.validateTotalUnits("12").isValid)

        val zeroResult = InputValidator.validateTotalUnits("0")
        assertTrue(zeroResult is ValidationResult.Error)
    }
}

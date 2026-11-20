package com.example.mobileappcat2.util

object InputValidator {

    private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$".toRegex()
    private val PHONE_REGEX = "^(\\+254|0)[17]\\d{8}\$".toRegex()

    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Email address cannot be empty.")
        }
        if (!trimmed.matches(EMAIL_REGEX)) {
            return ValidationResult.Error("Enter a valid email address (e.g. tenant@domain.com).")
        }
        return ValidationResult.Success
    }

    fun validatePhone(phone: String): ValidationResult {
        val trimmed = phone.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Phone number cannot be empty.")
        }
        if (!trimmed.matches(PHONE_REGEX)) {
            return ValidationResult.Error("Enter a valid phone number (e.g. 0712345678 or +254712345678).")
        }
        return ValidationResult.Success
    }

    fun validateName(name: String, label: String = "Name"): ValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("$label is required.")
        }
        if (trimmed.length < 2) {
            return ValidationResult.Error("$label must be at least 2 characters long.")
        }
        return ValidationResult.Success
    }

    fun validateRentAmount(amount: String): ValidationResult {
        val trimmed = amount.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Rent amount is required.")
        }
        val number = trimmed.toDoubleOrNull()
            ?: return ValidationResult.Error("Rent amount must be a valid number.")
        if (number <= 0) {
            return ValidationResult.Error("Rent amount must be greater than 0.")
        }
        return ValidationResult.Success
    }

    fun validateUnitNumber(unitNumber: String): ValidationResult {
        val trimmed = unitNumber.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Unit / House number is required.")
        }
        return ValidationResult.Success
    }

    fun validatePropertyName(name: String): ValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Property name is required.")
        }
        if (trimmed.length < 3) {
            return ValidationResult.Error("Property name must be at least 3 characters long.")
        }
        return ValidationResult.Success
    }

    fun validateAddress(address: String): ValidationResult {
        val trimmed = address.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Property address is required.")
        }
        if (trimmed.length < 5) {
            return ValidationResult.Error("Please enter a complete address (min 5 characters).")
        }
        return ValidationResult.Success
    }

    fun validateTotalUnits(units: String): ValidationResult {
        val trimmed = units.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Total units is required.")
        }
        val intValue = trimmed.toIntOrNull()
            ?: return ValidationResult.Error("Total units must be a valid whole number.")
        if (intValue <= 0) {
            return ValidationResult.Error("Total units must be at least 1.")
        }
        return ValidationResult.Success
    }

    fun validatePaymentAmount(amount: String): ValidationResult {
        val trimmed = amount.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Payment amount is required.")
        }
        val doubleValue = trimmed.toDoubleOrNull()
            ?: return ValidationResult.Error("Payment amount must be a valid number.")
        if (doubleValue <= 0) {
            return ValidationResult.Error("Payment amount must be greater than 0.")
        }
        return ValidationResult.Success
    }

    fun validateMaintenanceTitle(title: String): ValidationResult {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Issue title is required.")
        }
        if (trimmed.length < 5) {
            return ValidationResult.Error("Title must be at least 5 characters long.")
        }
        return ValidationResult.Success
    }

    fun validateMaintenanceDescription(description: String): ValidationResult {
        val trimmed = description.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult.Error("Description is required.")
        }
        if (trimmed.length < 10) {
            return ValidationResult.Error("Description must be at least 10 characters long.")
        }
        return ValidationResult.Success
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.Error("Password cannot be empty.")
        }
        if (password.length < 6) {
            return ValidationResult.Error("Password must be at least 6 characters long.")
        }
        return ValidationResult.Success
    }
}

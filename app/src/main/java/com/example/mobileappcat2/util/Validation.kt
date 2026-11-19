package com.example.mobileappcat2.util

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val errorMessage: String) : ValidationResult()

    val isValid: Boolean
        get() = this is Success

    val errorMessageOrNull: String?
        get() = (this as? Error)?.errorMessage
}

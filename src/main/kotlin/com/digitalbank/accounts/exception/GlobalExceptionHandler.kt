package com.digitalbank.accounts.exception

import com.digitalbank.contracts.accounts.AccountError
import com.digitalbank.contracts.common.ApiError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant
import java.util.UUID

/**
 * Global exception handler that maps [AccountDomainException] to structured HTTP responses.
 *
 * All error responses use [ApiError] from banking-contracts to ensure a consistent
 * error envelope across the platform API boundary.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AccountDomainException::class)
    fun handleAccountError(ex: AccountDomainException): ResponseEntity<ApiError> {
        return when (val error = ex.error) {
            is AccountError.NotFound -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiError("ACCOUNT_NOT_FOUND", "Account '${error.accountId}' was not found"))

            is AccountError.InsufficientFunds -> ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(apiError("INSUFFICIENT_FUNDS", "Account '${error.accountId}' has insufficient funds"))

            is AccountError.AccountFrozen -> ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiError("ACCOUNT_FROZEN", "Account '${error.accountId}' is already frozen"))
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(ex: Exception): ResponseEntity<ApiError> = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(apiError("INTERNAL_ERROR", "An unexpected error occurred"))

    private fun apiError(code: String, message: String) = ApiError(
        code = code,
        message = message,
        traceId = UUID.randomUUID().toString(),
        timestamp = Instant.now().toString()
    )
}

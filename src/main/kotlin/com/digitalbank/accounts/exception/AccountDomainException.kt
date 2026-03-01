package com.digitalbank.accounts.exception

import com.digitalbank.contracts.accounts.AccountError

/**
 * Runtime exception that wraps a typed [AccountError] for propagation through
 * the Spring MVC exception handling chain.
 *
 * Thrown by the controller layer; caught and mapped to HTTP responses by
 * [GlobalExceptionHandler].
 */
class AccountDomainException(val error: AccountError) : RuntimeException(error.toString())

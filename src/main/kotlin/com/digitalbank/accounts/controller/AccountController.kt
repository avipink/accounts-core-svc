package com.digitalbank.accounts.controller

import com.digitalbank.accounts.exception.AccountDomainException
import com.digitalbank.accounts.mapper.AccountMapper
import com.digitalbank.accounts.repository.AccountRepository
import com.digitalbank.contracts.accounts.AccountError
import com.digitalbank.contracts.accounts.AccountResponse
import com.digitalbank.contracts.accounts.AccountStatus
import com.digitalbank.contracts.accounts.AccountSummary
import com.digitalbank.contracts.common.MonetaryAmount
import com.digitalbank.contracts.common.PaginatedResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.math.ceil

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "Account management endpoints")
class AccountController(
    private val accountRepository: AccountRepository,
    private val accountMapper: AccountMapper
) {

    @GetMapping
    @Operation(
        summary = "List all accounts",
        description = "Returns a paginated list of account summaries. Internal fields are never exposed."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    ])
    fun listAccounts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): PaginatedResponse<AccountSummary> {
        val all = accountRepository.findAll()
        val totalItems = all.size.toLong()
        val totalPages = if (pageSize > 0) ceil(totalItems.toDouble() / pageSize).toInt().coerceAtLeast(1) else 1
        val start = ((page - 1) * pageSize).coerceAtMost(all.size)
        val end = (start + pageSize).coerceAtMost(all.size)
        val items = all.subList(start, end).map { accountMapper.toAccountSummary(it) }
        return PaginatedResponse(
            items = items,
            page = page,
            pageSize = pageSize,
            totalItems = totalItems,
            totalPages = totalPages
        )
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get account detail",
        description = "Returns full account details by ID. Internal fields are never included in the response."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Account found"),
        ApiResponse(responseCode = "404", description = "Account not found")
    ])
    fun getAccount(@PathVariable id: String): AccountResponse {
        val account = accountRepository.findById(id)
            ?: throw AccountDomainException(AccountError.NotFound(id))
        return accountMapper.toAccountResponse(account)
    }

    @GetMapping("/{id}/balance")
    @Operation(
        summary = "Get account balance",
        description = "Returns the current available balance of the account"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Balance retrieved"),
        ApiResponse(responseCode = "404", description = "Account not found")
    ])
    fun getBalance(@PathVariable id: String): MonetaryAmount {
        val account = accountRepository.findById(id)
            ?: throw AccountDomainException(AccountError.NotFound(id))
        return account.balance
    }

    @PostMapping("/{id}/hold")
    @Operation(
        summary = "Place hold on account",
        description = "Freezes the account, blocking all future debits and credits"
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Account frozen successfully"),
        ApiResponse(responseCode = "404", description = "Account not found"),
        ApiResponse(responseCode = "409", description = "Account is already frozen")
    ])
    fun holdAccount(@PathVariable id: String): AccountResponse {
        val account = accountRepository.findById(id)
            ?: throw AccountDomainException(AccountError.NotFound(id))
        if (account.status == AccountStatus.FROZEN) {
            throw AccountDomainException(AccountError.AccountFrozen(id))
        }
        val frozen = account.copy(status = AccountStatus.FROZEN)
        accountRepository.save(frozen)
        return accountMapper.toAccountResponse(frozen)
    }
}

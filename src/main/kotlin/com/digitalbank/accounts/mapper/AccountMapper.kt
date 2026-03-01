package com.digitalbank.accounts.mapper

import com.digitalbank.accounts.domain.Account
import com.digitalbank.contracts.accounts.AccountResponse
import com.digitalbank.contracts.accounts.AccountSummary
import org.springframework.stereotype.Component

/**
 * Maps the internal [Account] domain entity to contract types at the API boundary.
 *
 * This is the ONLY component authorised to access internal [Account] fields.
 * The internal fields [Account.internalRiskScore], [Account.kycVerified], and
 * [Account.createdBy] are intentionally absent from all contract projections.
 */
@Component
class AccountMapper {

    /**
     * Projects a full [Account] entity to the [AccountResponse] contract type.
     * Used by GET /api/v1/accounts/{id} and POST /api/v1/accounts/{id}/hold.
     */
    fun toAccountResponse(account: Account): AccountResponse = AccountResponse(
        accountId = account.accountId,
        accountType = account.accountType,
        holderName = account.holderName,
        balance = account.balance,
        status = account.status,
        currency = account.currency,
        lastTransactionDate = account.lastTransactionDate,
        createdAt = account.createdAt
        // internalRiskScore, kycVerified, createdBy intentionally omitted
    )

    /**
     * Projects an [Account] entity to the lightweight [AccountSummary] contract type.
     * Used by GET /api/v1/accounts (list endpoint).
     */
    fun toAccountSummary(account: Account): AccountSummary = AccountSummary(
        accountId = account.accountId,
        accountType = account.accountType,
        holderName = account.holderName,
        balance = account.balance,
        status = account.status
    )
}

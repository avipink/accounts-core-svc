package com.digitalbank.accounts.domain

import com.digitalbank.contracts.accounts.AccountStatus
import com.digitalbank.contracts.accounts.AccountType
import com.digitalbank.contracts.common.MonetaryAmount

/**
 * Internal domain entity for an account.
 *
 * This type is PRIVATE to accounts-core-svc. It MUST NOT be returned directly from
 * any controller method or serialized into an API response.
 *
 * The fields [internalRiskScore], [kycVerified], and [createdBy] are internal
 * operational data — they are intentionally absent from [com.digitalbank.contracts.accounts.AccountResponse].
 * The AccountMapper is the ONLY place where this entity is projected to contract types.
 */
data class Account(
    val accountId: String,
    val accountType: AccountType,
    val holderName: String,
    val balance: MonetaryAmount,
    val status: AccountStatus,
    val currency: String,
    val lastTransactionDate: String?,
    val createdAt: String,

    // Internal fields — NEVER expose these via the API boundary
    val internalRiskScore: Int,
    val kycVerified: Boolean,
    val createdBy: String
)

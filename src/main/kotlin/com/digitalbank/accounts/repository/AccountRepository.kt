package com.digitalbank.accounts.repository

import com.digitalbank.accounts.domain.Account
import com.digitalbank.contracts.accounts.AccountStatus
import com.digitalbank.contracts.accounts.AccountType
import com.digitalbank.contracts.common.MonetaryAmount
import org.springframework.stereotype.Repository

/**
 * In-memory account repository pre-seeded with 5 realistic accounts.
 *
 * This is a mock data store for the practice lab. In a production service
 * this would be replaced by a JPA repository backed by a relational database.
 */
@Repository
class AccountRepository {

    private val accounts: MutableMap<String, Account> = mutableMapOf(
        "ACC-001" to Account(
            accountId = "ACC-001",
            accountType = AccountType.CHECKING,
            holderName = "Sarah Mitchell",
            balance = MonetaryAmount("12450.75", "USD"),
            status = AccountStatus.ACTIVE,
            currency = "USD",
            lastTransactionDate = "2026-02-28",
            createdAt = "2021-03-15T10:00:00Z",
            internalRiskScore = 2,
            kycVerified = true,
            createdBy = "onboarding-svc"
        ),
        "ACC-002" to Account(
            accountId = "ACC-002",
            accountType = AccountType.SAVINGS,
            holderName = "James Worthington",
            balance = MonetaryAmount("48720.00", "USD"),
            status = AccountStatus.ACTIVE,
            currency = "USD",
            lastTransactionDate = "2026-02-25",
            createdAt = "2019-07-01T09:00:00Z",
            internalRiskScore = 1,
            kycVerified = true,
            createdBy = "onboarding-svc"
        ),
        "ACC-003" to Account(
            accountId = "ACC-003",
            accountType = AccountType.MONEY_MARKET,
            holderName = "Elena Vasquez",
            balance = MonetaryAmount("5200.00", "USD"),
            status = AccountStatus.ACTIVE,
            currency = "USD",
            lastTransactionDate = "2026-01-10",
            createdAt = "2023-01-20T14:30:00Z",
            internalRiskScore = 3,
            kycVerified = true,
            createdBy = "branch-ops"
        ),
        "ACC-004" to Account(
            accountId = "ACC-004",
            accountType = AccountType.CHECKING,
            holderName = "Robert Chen",
            balance = MonetaryAmount("875.50", "USD"),
            status = AccountStatus.DORMANT,
            currency = "USD",
            lastTransactionDate = "2025-06-15",
            createdAt = "2018-11-05T08:00:00Z",
            internalRiskScore = 4,
            kycVerified = false,
            createdBy = "onboarding-svc"
        ),
        "ACC-005" to Account(
            accountId = "ACC-005",
            accountType = AccountType.SAVINGS,
            holderName = "Diana Okonkwo",
            balance = MonetaryAmount("31100.00", "USD"),
            status = AccountStatus.ACTIVE,
            currency = "USD",
            lastTransactionDate = "2026-02-27",
            createdAt = "2020-05-10T11:15:00Z",
            internalRiskScore = 1,
            kycVerified = true,
            createdBy = "digital-onboarding"
        )
    )

    fun findAll(): List<Account> = accounts.values.toList()

    fun findById(accountId: String): Account? = accounts[accountId]

    fun save(account: Account): Account {
        accounts[account.accountId] = account
        return account
    }
}

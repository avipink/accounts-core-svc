# Requirements — accounts-core-svc

## Intent Analysis

| Attribute | Value |
|---|---|
| **User Request** | Create the `accounts-core-svc` Spring Boot microservice for account management |
| **Request Type** | New Project (Greenfield) |
| **Scope Estimate** | Single Component — one Spring Boot service with controller, service, repository, mapper layers |
| **Complexity Estimate** | Moderate — Spring Boot wiring, domain-to-contract boundary enforcement, in-memory mock data |

---

## Functional Requirements

### FR-01: Project Setup
- Kotlin 1.9.25, Spring Boot 3.3.x, Gradle Kotlin DSL
- `build.gradle.kts`: apply `org.springframework.boot`, `io.spring.dependency-management`, `kotlin("jvm")`, `kotlin("plugin.spring")`
- `settings.gradle.kts`: `includeBuild("../banking-contracts")` for composite build
- Port 8081 configured in `application.yml`
- Dependencies:
  - `spring-boot-starter-web`
  - `spring-boot-starter-validation`
  - `springdoc-openapi-starter-webmvc-ui:2.6.0`
  - `com.digitalbank:banking-contracts` (via composite build)

### FR-02: Internal Domain Model (PRIVATE — never exposed via API)

`Account.kt` — richer than the contract type:
| Field | Type | Note |
|---|---|---|
| `accountId` | `String` | Unique identifier |
| `accountType` | `AccountType` | From contracts |
| `holderName` | `String` | Full name |
| `balance` | `MonetaryAmount` | From contracts |
| `status` | `AccountStatus` | From contracts |
| `currency` | `String` | ISO 4217 |
| `lastTransactionDate` | `String?` | Nullable |
| `createdAt` | `String` | ISO 8601 |
| `internalRiskScore` | `Int` | **Internal only — MUST NOT leak to API** |
| `kycVerified` | `Boolean` | **Internal only — MUST NOT leak to API** |
| `createdBy` | `String` | **Internal only — MUST NOT leak to API** |

### FR-03: In-Memory Repository

`AccountRepository.kt` — pre-seeded with 5 realistic accounts:
- Varied `AccountType` values (at least one of each type)
- Realistic holder names
- Balances between $500–$50,000
- Mixed `AccountStatus` (mostly ACTIVE, one DORMANT)
- `internalRiskScore`, `kycVerified`, `createdBy` populated with plausible values

### FR-04: Mapper

`AccountMapper.kt` — maps `Account` → contract types at the API boundary:
- `toAccountResponse(account: Account): AccountResponse`
- `toAccountSummary(account: Account): AccountSummary`
- Internal fields (`internalRiskScore`, `kycVerified`, `createdBy`) are NEVER included

### FR-05: REST Controller

`AccountController.kt` — all endpoints annotated with OpenAPI (`@Operation`, `@ApiResponse`, `@Tag`):

| Method | Path | Returns | Notes |
|---|---|---|---|
| GET | `/api/v1/accounts` | `PaginatedResponse<AccountSummary>` | Supports `page` (default 1) and `pageSize` (default 10) query params |
| GET | `/api/v1/accounts/{id}` | `AccountResponse` | Throws `AccountError.NotFound` if absent |
| GET | `/api/v1/accounts/{id}/balance` | `MonetaryAmount` | Extracts balance from account |
| POST | `/api/v1/accounts/{id}/hold` | `AccountResponse` | Sets status to FROZEN, throws `AccountError.AccountFrozen` if already frozen |

### FR-06: Global Exception Handler

`GlobalExceptionHandler.kt` — maps `AccountError` sealed class → HTTP:

| Error | HTTP Status | Error Code |
|---|---|---|
| `AccountError.NotFound` | 404 Not Found | `ACCOUNT_NOT_FOUND` |
| `AccountError.InsufficientFunds` | 422 Unprocessable Entity | `INSUFFICIENT_FUNDS` |
| `AccountError.AccountFrozen` | 409 Conflict | `ACCOUNT_FROZEN` |

Returns `ApiError` contract type (from banking-contracts).

### FR-07: OpenAPI / Swagger UI
- `springdoc-openapi-starter-webmvc-ui 2.6.0`
- Swagger UI accessible at `/swagger-ui.html`
- All endpoints documented with `@Operation`, `@ApiResponse`, `@Tag`

### FR-08: Architecture Constraint — Boundary Enforcement
- Internal `Account` domain model MUST NEVER be returned directly from a controller
- All controller return types MUST use contract types from `com.digitalbank.contracts`
- Mapper is the ONLY place where `Account` → contract conversion occurs

---

## Non-Functional Requirements

### NFR-01: Build Verification
- `./gradlew bootRun` starts successfully on port 8081
- `GET http://localhost:8081/swagger-ui.html` returns Swagger UI
- `GET http://localhost:8081/api/v1/accounts` returns 5 mock accounts

### NFR-02: Dependency Management
- Composite build via `includeBuild("../banking-contracts")` — no separate publish step needed
- Pinned versions for all dependencies in `build.gradle.kts`

### NFR-03: Kotlin Conventions
- Strict null safety — no `!!` operator
- Coroutines not needed (synchronous in-memory service)
- Data classes for all DTOs and internal models

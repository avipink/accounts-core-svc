# accounts-core-svc

## Role in Product
Authoritative account management service. Owns the lifecycle of all account records and is the
sole source of truth for account state. Acts as the account validation authority for
`payments-core-svc`. Leaf node in the service graph — makes no outbound HTTP calls.

## Key Entry Points
| Class | Path | Methods |
|---|---|---|
| `AccountController` | `/api/v1/accounts` | `GET /api/v1/accounts` — paginated list (`page`, `pageSize` query params) |
| `AccountController` | `/api/v1/accounts/{id}` | `GET` — full account detail |
| `AccountController` | `/api/v1/accounts/{id}/balance` | `GET` — balance only |
| `AccountController` | `/api/v1/accounts/{id}/hold` | `POST` — freeze account (idempotent; rejects if already FROZEN) |

No Kafka consumers. No scheduled jobs.

## Domain Model
| Class | Kind | Notes |
|---|---|---|
| `Account` | internal `data class` | Full state including `internalRiskScore`, `kycVerified`, `createdBy` — **never exposed at API boundary** |
| `AccountMapper` | `@Component` | Anti-corruption layer — sole authorized projector from `Account` → `AccountResponse` / `AccountSummary` |
| `AccountRepository` | `@Repository` | In-memory `MutableMap<String, Account>`; 5 pre-seeded accounts (ACC-001 to ACC-005) |
| `AccountDomainException` | `RuntimeException` | Wraps `AccountError` sealed class for Spring MVC propagation |
| `GlobalExceptionHandler` | `@RestControllerAdvice` | Maps `AccountError` variants → HTTP 404 / 422 + `ApiError` response |

Internal fields suppressed from all API responses: `internalRiskScore`, `kycVerified`, `createdBy`.

## Depends On
- `banking-contracts` — Gradle composite build (`includeBuild("../banking-contracts")`)
  - Types used: `AccountResponse`, `AccountSummary`, `AccountStatus`, `AccountType`, `AccountError`, `MonetaryAmount`, `ApiError`, `PaginatedResponse`
- No outbound HTTP calls to any other service.

## Events Published and Consumed
None. No Kafka, no message broker, no scheduled jobs.

## Database
None. In-memory `MutableMap<String, Account>` mock store only. Data is lost on restart.
No JPA, no Flyway/Liquibase. Target: relational DB with JPA (noted in code comments).

Pre-seeded accounts: ACC-001 through ACC-005.

## External Integrations
None. `Account.kycVerified` field exists in the domain model but is not populated via any
external KYC provider call — it is set as static seed data only.

## Known Complexity and Patterns
- **Anti-corruption layer**: `AccountMapper` is the only class permitted to read `Account` internal
  fields. All other code receives `AccountResponse` or `AccountSummary`.
- **Typed sealed errors**: `AccountError` sealed class with exhaustive `when` mapping in handler.
- **No service layer**: Business logic lives directly in `AccountController` (noted as a quality gap).
- **Idempotency guard**: `POST /api/v1/accounts/{id}/hold` explicitly rejects double-freeze with
  `AccountError.AlreadyFrozen`.

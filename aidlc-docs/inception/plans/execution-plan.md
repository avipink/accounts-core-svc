# Execution Plan — accounts-core-svc

## Analysis Summary
- **Type**: Greenfield Spring Boot service
- **Risk**: Low — in-memory, no external dependencies at runtime
- **Complexity**: Moderate — Spring wiring, domain boundary enforcement, OpenAPI

## Stage Decisions

### INCEPTION PHASE
- [x] Workspace Detection — COMPLETED (Greenfield)
- [x] Requirements Analysis — COMPLETED (from practice lab Prompt 2)
- [ ] Reverse Engineering — SKIP (Greenfield)
- [ ] User Stories — SKIP (internal REST API, no end-user personas)
- [x] Workflow Planning — IN PROGRESS
- [ ] Application Design — SKIP (layers and types fully specified)
- [ ] Units Generation — SKIP (single unit)

### CONSTRUCTION PHASE
- [ ] Functional Design — SKIP (domain model and mapper fully specified)
- [ ] NFR Requirements — SKIP (tech stack fully specified)
- [ ] NFR Design — SKIP
- [ ] Infrastructure Design — SKIP (local dev, in-memory)
- [ ] Code Planning — EXECUTE
- [ ] Code Generation — EXECUTE
- [ ] Build and Test — EXECUTE

## File Inventory (11 files)
| # | File | Location |
|---|---|---|
| 1 | `settings.gradle.kts` | root |
| 2 | `build.gradle.kts` | root |
| 3 | `gradle/wrapper/gradle-wrapper.properties` | root |
| 4 | `application.yml` | `src/main/resources/` |
| 5 | `AccountsApplication.kt` | `src/main/kotlin/com/digitalbank/accounts/` |
| 6 | `Account.kt` | `.../accounts/domain/` |
| 7 | `AccountDomainException.kt` | `.../accounts/exception/` |
| 8 | `AccountRepository.kt` | `.../accounts/repository/` |
| 9 | `AccountMapper.kt` | `.../accounts/mapper/` |
| 10 | `AccountController.kt` | `.../accounts/controller/` |
| 11 | `GlobalExceptionHandler.kt` | `.../accounts/exception/` |

## Success Criteria
- `./gradlew bootRun` starts on port 8081
- `GET /api/v1/accounts` returns 5 mock accounts
- `GET /swagger-ui.html` renders Swagger UI
- Internal fields (`internalRiskScore`, `kycVerified`, `createdBy`) never appear in any API response

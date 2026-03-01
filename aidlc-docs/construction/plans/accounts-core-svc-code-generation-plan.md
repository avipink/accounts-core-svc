# Code Generation Plan — accounts-core-svc

## Unit Context
- **Type**: Greenfield Spring Boot 3.3.x service
- **Package root**: `com.digitalbank.accounts`
- **Code location**: `accounts-core-svc/src/main/kotlin/com/digitalbank/accounts/`
- **Composite build**: `includeBuild("../banking-contracts")` in settings.gradle.kts

## Steps

- [x] **Step 1** — `settings.gradle.kts` (includeBuild for banking-contracts)
- [x] **Step 2** — `build.gradle.kts` (Spring Boot 3.3.5, springdoc 2.6.0, banking-contracts dep)
- [x] **Step 3** — `gradle/wrapper/gradle-wrapper.properties` (Gradle 8.5)
- [x] **Step 4** — `src/main/resources/application.yml` (port 8081, app name)
- [x] **Step 5** — `AccountsApplication.kt` (@SpringBootApplication + main)
- [x] **Step 6** — `domain/Account.kt` (internal entity with 3 private fields)
- [x] **Step 7** — `exception/AccountDomainException.kt` (wraps AccountError sealed class)
- [x] **Step 8** — `repository/AccountRepository.kt` (in-memory, 5 pre-seeded accounts)
- [x] **Step 9** — `mapper/AccountMapper.kt` (Account → AccountResponse/AccountSummary)
- [x] **Step 10** — `controller/AccountController.kt` (4 endpoints + OpenAPI annotations)
- [x] **Step 11** — `exception/GlobalExceptionHandler.kt` (AccountError → HTTP + ApiError)

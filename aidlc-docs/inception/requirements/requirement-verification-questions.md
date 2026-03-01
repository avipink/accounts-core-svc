# Requirement Verification Questions — accounts-core-svc

> Requirements sourced from Practice Lab Part 3 Prompt 2 are complete and unambiguous.
> Only the mandatory Security Extension applicability question requires your input.

---

## Question 1: Security Extension

Should security extension rules be enforced for this project?

**Context**: `accounts-core-svc` is a Spring Boot REST service with HTTP endpoints.
More SECURITY rules are applicable here than for the contracts library:
- **SECURITY-03** (structured logging) — applicable
- **SECURITY-05** (input validation on API params) — applicable (Spring @Valid)
- **SECURITY-09** (hardening / error message hygiene) — applicable (GlobalExceptionHandler)
- **SECURITY-10** (supply chain / dependency pinning) — applicable
- **SECURITY-15** (exception handling / fail-safe) — applicable
- **SECURITY-08** (access control) — N/A (no auth in this practice lab)

A) Yes — enforce all SECURITY rules as blocking constraints; mark inapplicable rules as N/A
   (recommended for production-grade services)

B) No — skip all SECURITY rules
   (suitable for PoCs/prototypes and practice labs)

X) Other (please describe after [Answer]: tag below)

[Answer]: B

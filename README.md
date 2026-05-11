# Demy API – RESTful Backend (Spring Boot)

This repository contains the **backend API** of the **Demy** project, developed in **Java 21 with Spring Boot**.  
The goal is to provide RESTful services for user management, authentication, and functionalities related to the academic project.

----

## Quick Start

### Prerequisites
- **Java 21**
- **Maven Wrapper (`./mvnw`)**
- (Optional) **Docker** if you want to run external dependencies (DB, etc.)

### Run locally
```bash
# Clone repository
git clone https://github.com/<org>/<repo>.git
cd <repo>

# Run with Maven
./mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## Main structure
- `src/main/java` → application source code.
- `src/test/java` → unit and integration tests.
- `docs/` → extended documentation (architecture, ADRs, API, diagrams, guides).
- `CONTRIBUTING.md` → contribution rules (commits, branches, PRs, code style).

---

## Architecture – Domain-Driven Design (DDD)

The project follows **Domain-Driven Design (DDD)** principles adapted to an academic context.

- Each **module** corresponds to a **bounded context**, with its own logic, models, and layers.
- Example modules:
    - `iam`: identity and access management
    - `institution`: institution management
- The `shared` module contains **common components** (e.g., exceptions, utilities) and is not considered a bounded context.

Each context follows a layered architecture pattern:

```
institution/
├─ application/       # Use cases (services)
├─ domain/            # Domain models
├─ infrastructure/    # Technical implementations (repositories, external services)
└─ interfaces/        # REST controllers, mappers
```

An example of the **domain model** for the `institution` context:

![Domain-model-diagram](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/nistrahq/demy-api/refs/heads/feature/project-documentation/docs/diagrams/plantuml/domain-model-diagram.puml?token=GHSAT0AAAAAAC6GPIH5NCWVSIQRCHU4JC562FUQIMA)

For more details, see the complete **domain diagrams** in [`docs/diagrams/`](./docs/diagrams/).

---

## Documentation
Detailed documentation can be found in [`docs/`](./docs/):

- **Quick guides** → [`docs/guides/`](./docs/guides/)
- **API** (endpoints, OpenAPI, examples) → [`docs/api/`](./docs/api/)
- **Architecture and diagrams** → [`docs/architecture/`](./docs/architecture/)
- **Conventions and references** → [`docs/references/`](./docs/references/)

---

## Workflow
- Main branches:
    - `main` → stable releases (e.g., TB1, TP1, TB2, TF1).
    - `develop` → integration of new features.
- Support branches:
    - `feature/<topic>` → new features or improvements.
    - `hotfix/<topic>` → critical fixes in production.
    - `release/vX.Y.Z` → release preparation (we use **Semantic Versioning**).

More details in: [`CONTRIBUTING.md`](./CONTRIBUTING.md)

---

## Quick checklist for developers
- Configure UTF-8 in your IDE (IntelliJ: *Settings > Editor > File Encodings > UTF-8*).
- Follow the code style defined in the contributors guide [CONTRIBUTING.md](./CONTRIBUTING.md).
- Use commit messages with **Conventional Commits**.
- Run tests before opening a PR:
  ```bash
  ./mvnw verify
  ```

---

## Project status
This is an **academic and private** project, developed by the Software Engineering team – UPC.  
External contributions are not accepted.

---


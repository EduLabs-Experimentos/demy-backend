# Collaboration Guide

Available languages:
- [English](CONTRIBUTING.md)
- [Spanish](CONTRIBUTING.es.md)

This repository contains the **main project documentation**.  
This guide defines how we work as a team to maintain an organized and consistent workflow.

## 1) Quick Introduction

This section explains how to set up the repository locally and start contributing.

### **Clone the repository**

```bash
git clone <REPOSITORY-URL>
cd <REPO-NAME>
```










































































### **Configure your user**

Make sure Git has your user configured:
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### **Update your local branches**

```bash
git fetch origin
git checkout develop
git pull origin develop
```

### **Create your feature branch (from `develop`)**

Use a clear naming convention: `feature/<functionality>`
```bash
git checkout -b feature/institution-management
```

### **Work and commit (Conventional Commits + optional scope)**

Format: `<type>(<scope>): <message>`  
Examples:
- `feat(iam): add roles for institution admins`
- `refactor(institution): update model relationships`
- `fix(auth): correct login bug`
```bash
git add .
git commit -m "refactor(institution): update model relationships"
```

### **Keep your branch updated (fetch + merge)**

```bash
git checkout feature/institution-management
git fetch origin
git merge origin/develop
# if there are conflicts, resolve them and then:
git push origin feature/institution-management
```

> Tip: `git fetch` does not touch your working tree; integration happens when you run `git merge`.

### **Push your branch to remote**

```bash
git push origin feature/institution-management
```

### **Open a Pull Request (PR)**

- Base branch: `develop` (never `main`).
- Use the PR template and explain what/why.
- If applicable, link an Issue with `Closes #<issue-number>`.
- Request a review from at least one teammate.
- Merge using **Merge commit** (do not squash).

---

- Quick guide of basic Git commands:
    - `git status` → see modified files and repo status
    - `git add <file>` → stage changes
    - `git commit -m "message"` → save changes locally
    - `git log --oneline` → short commit history
    - `git fetch origin` → fetch remote refs without merging
    - `git merge origin/develop` → integrate remote develop into your branch
    - `git push origin <branch>` → push your branch to remote
    - `git pull origin <branch>` → fetch and merge changes from remote

---

## 2) Git Workflow

- Central repository: GitHub. Main branch: `main`.
- Branching model: **Gitflow**
    - `main`: stable production code/documentation.
    - `develop`: integration of new features.
    - Support branches:
        - `feature/<functionality>`: development of new features.  
          Example: `feature/institution-management`
        - `hotfix/<name>`: critical fixes in production.  
          Example: `hotfix/user-login-bug`
        - `release/<version>`: preparation of a stable release before merging into `main`.  
          Example: `release/v1.0.0`

### **Keeping branches up to date**:

- Do this regularly (not only before a PR), especially after merges into `develop`.
- **Bring the latest from `develop` and merge it into your `feature`:**
  ```bash
  # Be on your working branch
  git checkout feature/institution-management

  # Fetch remote references without merging yet
  git fetch origin

  # Merge confirmed changes from develop into your feature
  git merge origin/develop

  # (if there were conflicts, resolve them, commit, and continue)
  git push origin feature/institution-management
  ```
      > Advantage: `git fetch` does not touch your working tree; you see what’s coming and **you** decide when to merge.

- **Keep your local `develop` branch updated** periodically:
  ```bash
  git checkout develop
  git fetch origin
  git merge origin/develop
  ```

- **After another PR is merged into `develop`**, repeat the process on your `feature` branch to avoid late conflicts.

### **Commits**:

- Descriptive, concise, and consistent messages in English.
- We use the **Conventional Commits** standard with an **optional scope** to indicate the affected part of the project.
- Format: `<type>(<scope>): <message>`
- Examples:
    - `feat(shared): add i18n support for common components`
    - `refactor(auth): improve token refresh logic`
    - `fix(api): correct data serialization issue`
    - `docs(readme): update installation instructions`
    - `build(pom): update dependency versions`

### **Versioning**:

- We follow **Semantic Versioning 2.0 (semver)** for release tags.
    - Format: `MAJOR.MINOR.PATCH`
    - Examples: `1.0.0`, `1.1.0`, `1.1.1`

### **Pull Requests (PR)**:

- All changes must go through a PR from a `feature/*` branch into `develop` (never commit directly to `develop` or `main`).
- Include a summary of the change and screenshots if applicable.
- Ensure that exported files are synchronized with their sources (see Section 4).
- At least one teammate’s review is required before merging.
- Use **Merge commit** (not squash) to preserve the complete commit history.

---

## 3) DDD Structure

The project applies **Domain-Driven Design (DDD)** principles in a practical way, adapted to the academic context.

- Each **module** (context) corresponds to a **bounded context**, with its own logic, models, and internal layers.
- Example modules:
    - `iam`: identity and access management.
    - `institution`: institution management.
- The `shared` module contains **common components** (e.g., exceptions, utilities) and is not considered a bounded context.

### Internal structure of a module or context

Each context maintains the separation of responsibilities following the typical layered architecture:

```
institution/
├─ application/       # Use cases (services)
├─ domain/            # Domain models
├─ infrastructure/    # Technical implementations (repositories, external services)
└─ interfaces/        # REST controllers, mappers
```

### Domain diagram
The diagram of modules and their relationships is maintained in `docs/diagrams/plantuml/` and exported into readable formats to be included in the documentation:

![Domain-model-diagram](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/nistrahq/demy-api/refs/heads/feature/project-documentation/docs/diagrams/plantuml/domain-model-diagram.puml?token=GHSAT0AAAAAAC6GPIH5NCWVSIQRCHU4JC562FUQIMA)

---

## 4) Code Style and Conventions

This project uses **Java 21** and follows consistent style practices to maintain readability and quality across the team.

### General rules
- Always use **UTF-8** in all files (`.java`, `.properties`, `.md`).
    - In **IntelliJ IDEA**:  
      `Settings > Editor > File Encodings > Default encoding for properties files: UTF-8 > Apply`
- Maximum line length: **120 characters**.
- Indentation: **4 spaces** (no tabs).
- Do not leave unnecessary spaces:
    - No: `class User { }`
    - Yes:
      ```
      class User {
          // content
      }
      ```
- Each public class must be in its own file with the same name as the class.

### Import organization
- Order: `java.*` → `javax.*` → external libraries → project imports.
- Remove unused imports before committing.
- Do not use `import *;`, always import specific classes.

### Naming conventions
- **Classes and Enums**: PascalCase (`UserService`, `OrderStatus`).
- **Methods and variables**: camelCase (`calculateTotal`, `userName`).
- **Constants**: UPPERCASE with `_` (`MAX_RETRIES`, `DEFAULT_TIMEOUT`).
- **Packages**: always lowercase (`com.projectname.auth`).
- Avoid cryptic abbreviations: prefer `customerRepository` over `custRepo`.

---

## 5) Issues
- Use Issues to report **bugs**, **new features**, or **technical improvements**.
- When opening an Issue, follow the Issue template (bug report / feature request).
- Title format:
    - `[Bug]` for errors (example: `[Bug] 500 error when creating user without email`).
    - `[Feat]` for new features (example: `[Feat] Multi-language support in error messages`).
    - `[Docs]` for documentation improvements (example: `[Docs] Update setup instructions in README`).
- Assign the Issue to yourself if you plan to work on it.
- Link Issues with PRs whenever possible by writing `Closes #<issue-number>` in the PR description.

---

## 6) Pull Requests
- All changes must go through a **Pull Request (PR)** from a `feature/*` branch into `develop` (never commit directly to `develop` or `main`).
- Title format:
    - `[Feat] Implement authentication with JWT`
    - `[Fix] Email validation in registration`
- Description:
    - Explain **what** you changed, **why**, and **how to test it** (endpoints, sample payloads).
    - If it resolves an Issue, include `Closes #<issue-number>`.
- Before opening the PR:
    - Update your `feature/*` branch with `origin/develop`.
- Review:
    - At least one teammate’s review is required before merging.
    - Use the *Files changed* tab and add comments in the code if something is unclear.
- **History**:
    - Keep commits small and focused (avoid “big dump commits”).
    - Merge strategy: **Merge commit** (no squash) to maintain traceability.

---

## 7) Checklist before opening a PR

- [ ] The `feature/*` branch is synchronized with `origin/develop`.
- [ ] The project compiles without errors.
- [ ] All tests pass locally.
- [ ] Commit messages follow **Conventional Commits**.
- [ ] Endpoint documentation (`docs/api/` and/or `openapi.yaml`, if applicable) is updated.
- [ ] No temporary files or credentials are included in the commit.
- [ ] The PR links to an Issue in the description (if applicable).

---

## 8) Best Practices

- One logical change per PR (avoid giant PRs).
- Prefer clarity over “smart hacks.”
- Document new or modified endpoints in `openapi.yaml` (if applicable).
- Use clear logs (without exposing sensitive data).
- Attach request/response examples in the PR if API contracts were changed.
- Keep consistency with the established code style (see section 4).
- Do not commit sensitive data (PII, credentials, tokens, environment variables).

---

## 9) Conflict Resolution

- Update your branch with `develop` frequently:
  ```bash
  git checkout feature/<name>
  git fetch origin
  git merge origin/develop
  ```
- If conflicts occur:
    - Resolve them locally and commit.
    - If the conflict is in **critical files** (e.g., security config, `openapi.yaml`), coordinate with the team before resolving.
- Avoid overwriting others’ changes: review carefully before forcing a push.

---

## 10) Communication

- Use the agreed channel (Discord/WhatsApp) to coordinate PRs and reviews.
- In the PR, describe **what you changed and why**.
- Tag the appropriate teammate(s) for review.

---

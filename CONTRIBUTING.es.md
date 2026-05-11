# Guía de colaboración

Idiomas disponibles:
- [Inglés](CONTRIBUTING.md)
- [Español](CONTRIBUTING.es.md)

Este repositorio contiene la **documentación principal del proyecto**.  
Esta guía define cómo trabajamos en equipo para mantener un flujo ordenado y consistente.

## 1) Introducción rápida

Esta sección explica cómo configurar el repositorio en local y comenzar a contribuir.

### **Clonar el repositorio**

```bash
git clone <URL-DEL-REPOSITORIO>
cd <NOMBRE-DEL-REPO>
```

### **Configurar tu usuario**

Asegúrate de que Git tenga tu usuario configurado:
```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu.email`ejemplo.com"
```

### **Actualizar tus ramas locales**

```bash
git fetch origin
git checkout develop
git pull origin develop
```

### **Crear tu rama de feature (desde `develop`)**

Usa un naming claro: `feature/<funcionalidad>`
```bash
git checkout -b feature/institution-management
```

### **Trabajar y hacer commits (Conventional Commits + scope opcional)**

Formato: `<type>(<scope>): <message>`
Ejemplos:
- `feat(iam): add roles for institution admins`
- `refactor(institution): update model relationships`
- `fix(auth): correct login bug`
```bash
git add .
git commit -m "refactor(institution): update model relationships"
```

### **Mantener tu rama actualizada (fetch + merge)**

```bash
git checkout feature/institution-management
git fetch origin
git merge origin/develop
# si hubo conflictos, resuélvelos y luego:
git push origin feature/institution-management
```

> Consejo: `git fetch` no toca tu working tree; integras cuando haces `git merge`.

### **Subir tu rama al remoto**

```bash
git push origin feature/institution-management
```

### **Abrir un Pull Request (PR)**

- Base: `develop` (nunca a `main`).
- Usa la plantilla del PR y explica qué/por qué.
- Si aplica, enlaza un Issue con `Closes #<número-del-issue>`.
- Solicita la revisión de al menos un compañero.
- Haz merge usando **Merge commit** (sin squash).

---

- Guía rápida de comandos básicos de Git
    - `git status` → ver archivos modificados y estado
    - `git add <archivo>` → preparar cambios
    - `git commit -m "mensaje"` → guardar cambios localmente
    - `git log --oneline` → historial corto de commits
    - `git fetch origin` → traer refs remotas sin mezclarlas
    - `git merge origin/develop` → integrar develop remoto en tu rama
    - `git push origin <rama>` → subir tu rama al remoto
    - `git pull origin <rama>` → traer y mezclar cambios del remoto

---

## 2) Flujo de trabajo con Git

- Repositorio central: GitHub. Rama principal: `main`.
- Modelo de ramas: **Gitflow**
    - `main`: código/documentación estable en producción.
    - `develop`: integración de nuevas funcionalidades.
    - Ramas de soporte:
        - `feature/<funcionalidad>`: desarrollo de nuevas funcionalidades.  
          Ejemplo: `feature/institution-management`
        - `hotfix/<nombre>`: correcciones críticas en producción.  
          Ejemplo: `hotfix/user-login-bug`
        - `release/<versión>`: preparación de una entrega estable antes de fusionar en `main`.  
          Ejemplo: `release/v1.0.0`

### **Mantener ramas actualizadas**:

- Hazlo de forma regular (no solo antes de un PR), especialmente después de merges en `develop`.
- **Traer lo último de `develop` y mezclarlo en tu `feature`**:
  ```bash
  # Estar en tu rama de trabajo
  git checkout feature/institution-management

  # Traer referencias remotas sin mezclar aún
  git fetch origin

  # Mezclar los cambios confirmados en develop dentro de tu feature
  git merge origin/develop

  # (si hubo conflictos, resuélvelos, haz commit y continúa)
  git push origin feature/institution-management
  ```
      > Ventaja: `git fetch` no toca tu working tree; ves qué viene y **tú** decides cuándo mezclar (`merge`).

- **Mantén tu `develop` local actualizado** periódicamente:
  ```bash
  git checkout develop
  git fetch origin
  git merge origin/develop
  ```

- **Después de que otro PR se mergee a `develop`**, repite el proceso en tu `feature` para evitar conflictos tardíos.

### **Commits**:

- Mensajes descriptivos, concisos y consistentes en inglés.
- Usamos el estándar **Conventional Commits** con **scope opcional** para indicar la parte del proyecto afectada.
- Formato: `<type>(<scope>): <message>`
- Ejemplos:
    - `feat(shared): add i18n support for common components`
    - `refactor(auth): improve token refresh logic`
    - `fix(api): correct data serialization issue`
    - `docs(readme): update installation instructions`
    - `build(pom): update dependency versions`

### **Versionado**:

- Seguimos **Semantic Versioning 2.0 (semver)** para los tags de release.
    - Formato: `MAJOR.MINOR.PATCH`
    - Ejemplos: `1.0.0`, `1.1.0`, `1.1.1`

### **Pull Requests (PR)**:

- Todos los cambios deben pasar por un PR desde una rama `feature/*` hacia `develop` (nunca hacer commits directos a `develop` o `main`).
- Incluir un resumen del cambio y captura(s) de pantalla si aplica.
- Asegurarse de que los archivos exportados estén sincronizados con sus fuentes (ver Sección 4).
- Se requiere la revisión de al menos un compañero antes de hacer merge.
- Usar **Merge commit** (no squash) para preservar el historial completo de commits.

---

## 3) Estructura DDD

El proyecto aplica principios de **Domain-Driven Design (DDD)** de forma práctica y adaptada al contexto académico.

- Cada **módulo** (contexto) corresponde a un **bounded context**, con su propia lógica, modelos y capas internas.
- Ejemplos de módulos:
    - `iam`: gestión de identidades y accesos.
    - `institution`: gestión de instituciones.
- El módulo `shared` contiene **componentes comunes** (ej. excepciones, utilidades), y no se considera un bounded context como tal.

### Estructura interna de un módulo o contexto

Cada contexto mantiene la separación de responsabilidades siguiendo la arquitectura típica de arquitectura en capas:
```
institution/
├─ application/       # Casos de uso (servicios)
├─ domain/            # Modelos de dominio
├─ infrastructure/    # Implementaciones técnicas (repositorios, servicios externos)
└─ interfaces/        # Controladores REST, mapeadores
```

### Diagrama de dominios
El diagrama de módulos y sus relaciones se mantiene en `docs/diagrams/plantuml/` y se exporta a formatos legibles para incluir en la documentación:

![Domain-model-diagram](https://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/nistrahq/demy-api/refs/heads/feature/project-documentation/docs/diagrams/plantuml/domain-model-diagram.puml?token=GHSAT0AAAAAAC6GPIH5NCWVSIQRCHU4JC562FUQIMA)

---

## 4) Estilo de código y convenciones

Este proyecto usa **Java 21** y sigue prácticas consistentes de estilo para mantener legibilidad y calidad en equipo.

### Reglas generales
- Usar siempre **UTF-8** en todos los archivos (`.java`, `.properties`, `.md`).
    - En **IntelliJ IDEA**:  
      `Settings > Editor > File Encodings > Default encoding for properties files: UTF-8 > Apply`
- Longitud máxima de línea: **120 caracteres**.
- Indentación: **4 espacios** (no tabs).
- No dejar espacios innecesarios:
    - No: `class User { }`
    - Sí: `class User {`
      // contenido
      `}`
- Cada clase pública en su propio archivo con el mismo nombre que la clase.

### Organización de imports
- Orden: `java.*` → `javax.*` → librerías externas → imports del proyecto.
- Eliminar imports no usados antes de hacer commit.
- No usar `import *;`, importar clases específicas.

### Nomenclatura
- **Clases y Enums**: PascalCase (`UserService`, `OrderStatus`).
- **Métodos y variables**: camelCase (`calculateTotal`, `userName`).
- **Constantes**: MAYÚSCULAS con `_` (`MAX_RETRIES`, `DEFAULT_TIMEOUT`).
- **Paquetes**: siempre en minúsculas (`com.projectname.auth`).
- Evitar abreviaturas crípticas: preferir `customerRepository` antes que `custRepo`.

---

## 5) Issues
- Usa Issues para reportar **bugs**, **nuevas features** o **mejoras técnicas**.
- Al abrir un Issue, sigue la plantilla de Issue (bug report / feature request).
- Formato del título:
    - `[Bug]` para errores (ejemplo: `[Bug] Error 500 al crear usuario sin email`).
    - `[Feat]` para nuevas funcionalidades (ejemplo: `[Feat] Soporte multi-language en errores`).
    - `[Docs]` para mejoras en documentación técnica (ejemplo: `[Docs] Actualizar setup en README`).
- Asígnate el Issue si planeas trabajarlo.
- Enlaza Issues con PRs siempre que sea posible escribiendo `Closes #<número-del-issue>` en la descripción del PR.

---

## 6) Pull Requests
- Todos los cambios deben pasar por un **Pull Request (PR)** desde una rama `feature/*` hacia `develop` (nunca commits directos a `develop` o `main`).
- Formato del título:
    - `[Feat] Implementar autenticación con JWT`
    - `[Fix] Validación de correos en registro`
- Descripción:
    - Explica **qué** cambiaste, **por qué** y **cómo probarlo** (endpoints, payloads de ejemplo).
    - Si resuelve un Issue, incluye `Closes #<número-del-issue>`.
- Antes de abrir el PR:
    - Actualiza tu rama `feature/*` con `origin/develop`.
- Revisión:
    - Se requiere la revisión de al menos un compañero antes de hacer merge.
    - Usa la pestaña *Files changed* y comenta en el código si hay dudas.
- **Historial**:
    - Commits pequeños y temáticos (evita “big dump commits”).
    - Estrategia de merge: **Merge commit** (no squash) para mantener trazabilidad.

---

## 7) Lista de verificación antes de abrir un PR

- [ ] La rama `feature/*` está sincronizada con `origin/develop`.
- [ ] El proyecto compila sin errores.
- [ ] Todos los tests pasan localmente.
- [ ] Los mensajes de commit siguen **Conventional Commits**.
- [ ] La documentación de endpoints (`docs/api/` y/o `openapi.yaml`, en caso aplique) está actualizada.
- [ ] No hay archivos temporales ni credenciales en el commit.
- [ ] El PR enlaza un Issue en la descripción (si aplica).

---

## 8) Buenas prácticas

- Un cambio lógico por PR (evitar PRs gigantes).
- Prefiere claridad sobre “smart hacks”.
- Documenta endpoints nuevos o modificados en `openapi.yaml` (si aplica).
- Usa logs claros (sin exponer datos sensibles).
- Adjunta ejemplos de requests/responses en el PR si cambiaste contratos de API.
- Mantén consistencia en estilo de código (ver sección 4).
- No subir datos sensibles (PII, credenciales, tokens, variables de entorno).

---

## 9) Resolución de conflictos

- Actualiza tu rama con `develop` frecuentemente:
  ```bash
  git checkout feature/<nombre>
  git fetch origin
  git merge origin/develop
  ```
- Si hay conflictos:
    - Resuélvelos localmente y haz commit.
    - Si el conflicto es en **archivos críticos** (ej. configuración de seguridad, openapi.yaml), coordina antes con el equipo.
- Evita sobrescribir cambios de otros: revisa cuidadosamente antes de forzar un push.

---

## 10) Comunicación

- Usa el canal acordado (Discord/WhatsApp) para coordinar PRs y revisiones.
- En el PR, describe **qué cambiaste y por qué**.
- Etiqueta a quien corresponda para revisión.

---
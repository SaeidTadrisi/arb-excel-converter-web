# ARB ⇄ Excel Localization Toolkit

**A full-stack Java/Spring Boot service (with a lightweight JS client) for converting Flutter/Dart `.arb` localization files to translator-friendly Excel workbooks — and back.**

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Apache POI](https://img.shields.io/badge/Excel-Apache%20POI-blue)](https://poi.apache.org/)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker)](https://www.docker.com/)
[![Tests](https://img.shields.io/badge/Tests-JUnit5%20%2B%20AssertJ-25A162?logo=junit5)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

> ⚠️ Replace the badge/shield links and add a real CI badge once GitHub Actions is wired up (see [Roadmap](#roadmap)).

---

## Overview

Flutter/Dart projects store translatable strings in `.arb` (Application Resource Bundle) JSON files — one per locale. In real-world localization workflows, non-technical translators can't work directly in nested JSON, and professional translators typically expect a **spreadsheet**.

This project was built to solve that exact problem for a real internationalization pipeline: it lets a developer **export one or many `.arb` files into a single Excel workbook** (one column per locale) for translators to fill in, and then **re-import the completed workbook back into valid `.arb` files**, ready to drop back into the app.

The repository is organized as two cooperating parts:

- **`backend/`** — a Spring Boot REST API that performs the actual ARB↔Excel transformation (parsing, placeholder extraction/re-assembly, workbook generation).
- **`frontend/`** — a minimal vanilla HTML/CSS/JS client that lets a user upload files and download the converted result without needing Postman or curl.

Both parts originated as an internal tool and were consolidated into this single repository for clarity and easier deployment.

## Architecture & Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Backend runtime | Java 21, Spring Boot 3.2.4 | REST API, dependency injection, multipart file handling |
| Excel I/O | Apache POI (`poi-ooxml`) | Reading/writing `.xlsx` / `.xls` workbooks |
| JSON handling | `org.json`, Gson | Parsing ARB (JSON) files and re-serializing them with correct escaping |
| Boilerplate reduction | Lombok | Logging (`@Log4j2`), getters/setters |
| Testing | JUnit 5, AssertJ, Spring Boot Test | Unit tests per domain component + fake test doubles |
| Build | Maven (with Maven Wrapper) | Dependency management & packaging |
| Frontend | HTML5, CSS3, vanilla JavaScript (ES modules) | File upload UI, calls backend via `fetch` |
| Containerization | Docker (multi-stage build) | Reproducible backend deployment |

### Domain design

The backend follows a **layered / Clean-Architecture-inspired** structure rather than a typical "fat controller" Spring app:

```
controller/       → REST endpoints (thin, delegate to application layer)
application/      → Use-case orchestration (PrepareToTranslate, ConvertTranslation)
domain/arb/       → ARB parsing, placeholder extraction, ARB→Excel orchestration
domain/excel/     → Excel parsing, placeholder extraction, Excel→ARB orchestration
domain/exception/ → Domain-specific exceptions
infrastructure/   → Concrete adapters (file & Excel readers)
presentation/dto/ → Request/response data carriers
```

Each transformation step (simple elements, placeholders, combining, writing) is its own single-responsibility class, which is what makes the domain layer independently unit-testable with fake readers (`FakeFilesReader`, `FakeExcelReader`).

## Key Features

- **Bidirectional conversion**: `.arb → .xlsx` and `.xlsx/.xls → .arb` (packaged as a ZIP when multiple locales are involved).
- **Multi-locale support**: combine several `.arb` files into one workbook, using a reference locale to define row order.
- **ICU placeholder preservation**: nested `@key` placeholder metadata (`type`, `example`) is extracted, flattened for spreadsheet editing, and correctly reconstructed on the way back.
- **Layered, testable architecture**: domain logic has zero framework dependency and is covered by focused unit tests with fake test doubles (no mocking framework required).
- **Stateless REST API**: two endpoints (`/translate/prepare-translate`, `/translate/convert-translation`) that accept multipart uploads and stream back binary results.
- **Framework-agnostic frontend**: a dependency-free JS client — no build step, no bundler — demonstrating the API can be consumed by any client.

## Getting Started

### Prerequisites
- Docker (recommended), **or** JDK 21 + Maven if running locally without containers.

### Option 1 — Run with Docker (recommended)

```bash
# From the repository root
docker build -t arb-excel-converter -f Dockerfile .
docker run -p 8080:8080 arb-excel-converter
```

The API will be available at `http://localhost:8080`.

### Option 2 — Run the backend locally with Maven

```bash
cd backend
./mvnw spring-boot:run
```

### Running the frontend

The frontend is static — no build step required:

```bash
cd frontend
python3 -m http.server 5500   # or any static file server / Live Server extension
```

Then open `http://localhost:5500`. Update the `API_BASE_URL` in `scripts/config.js` (see [Refactoring Notes](#refactoring-notes--roadmap)) to point at your running backend instead of the previous hosted demo URL.

### API Reference

| Endpoint | Method | Params | Returns |
|---|---|---|---|
| `/translate/prepare-translate` | `POST` | `fileList` (multiple `.arb` files), `referenceFile` (string, filename used to fix column order) | `output.xlsx` |
| `/translate/convert-translation` | `POST` | `file` (single `.xlsx`/`.xls`) | `output.zip` containing one `.arb` per locale column |

### Running Tests

```bash
cd backend
./mvnw test
```

## Refactoring Notes & Roadmap

This codebase was reviewed and cleaned up for public presentation. Notable engineering improvements applied/recommended:

1. **Removed hardcoded I/O paths** — file uploads no longer write into `ServletContext.getRealPath("/")` (fragile and unsafe on read-only container filesystems); use a configurable temp directory instead.
2. **Removed dead/duplicate class** — a stray, empty `ExcelSimpleElementsExtractor` class inside `domain/exception` shadowed the real implementation in `domain/excel` and was deleted to avoid confusion.
3. **Repository hygiene** — sample binaries (`en.xlsx`, `output.xlsx`) and stray scratch files were moved into test resources / `.gitignore`d instead of being committed at the repo root.

Planned next steps: externalize the frontend API base URL into an environment-driven config, add a GitHub Actions CI workflow (build + test on push), and add integration tests for the REST controllers.

## License

MIT — see [LICENSE](LICENSE) for details.

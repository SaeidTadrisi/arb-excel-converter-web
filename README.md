# ARB ⇄ Excel Localization Toolkit

**A full-stack Java/Spring Boot service (with a lightweight JS client) for converting Flutter/Dart `.arb` localization files to translator-friendly Excel workbooks — and back.**

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Apache POI](https://img.shields.io/badge/Excel-Apache%20POI-blue)](https://poi.apache.org/)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker)](https://www.docker.com/)
[![Tests](https://img.shields.io/badge/Tests-JUnit5%20%2B%20AssertJ-25A162?logo=junit5)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)
[![CI](https://github.com/SaeidTadrisi/arb-excel-converter-web/actions/workflows/ci.yml/badge.svg)](https://github.com/SaeidTadrisi/arb-excel-converter-web/actions/workflows/ci.yml)

**[Live Demo 🌍](https://saeidtadrisi.github.io/arb-localization-converter) — Try the frontend right in your browser!**
---

## Why this project?

Localization files are convenient for developers but not always practical for translators. This project was built to provide a simple workflow:

1. Upload one or more `.arb` files.
2. Select the reference locale.
3. Export a single Excel workbook for translation.
4. Upload the translated workbook.
5. Download the generated ARB files as a ZIP archive.

The project preserves ARB message keys and placeholder metadata so translated resources can be returned to the application workflow.

## Repository layout

```text
.
├── backend/                 Spring Boot API and unit tests
├── frontend/                Static HTML, CSS, and JavaScript client
├── Dockerfile               Container build for the backend
└── .github/workflows/       CI workflow
```

The backend and frontend were originally developed as separate repositories in 2024. They were consolidated here in 2026 so the complete application can be reviewed, built, and maintained from one repository. The original Git history has been preserved.

## Tech stack

- Java 21
- Spring Boot 3.2
- Maven
- Apache POI
- Gson and org.json
- HTML, CSS, and vanilla JavaScript
- JUnit 5 and AssertJ
- Docker
- GitHub Actions

## Features

- Convert one or more ARB files into an Excel workbook.
- Convert `.xlsx` or `.xls` translation workbooks into ARB files.
- Package generated ARB files in a ZIP archive.
- Preserve locale ordering through a selected reference file.
- Extract and rebuild ARB placeholder metadata such as `type` and `example`.
- Separate application, domain, infrastructure, controller, and DTO concerns.
- Include unit tests for parsing, combining, reading, and conversion logic.

## Run locally

### Prerequisites

- JDK 21
- Docker, or Maven through the included Maven Wrapper

### Backend

```bash
cd backend
./mvnw test
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
cd backend
.\mvnw test
.\mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

### Frontend

The frontend is static and can be served from the `frontend/` directory using any static web server:

```bash
cd frontend
python3 -m http.server 5500
```

> The current frontend uses the deployed Render API URL that was configured for the original project. To use a locally running backend, update the two `fetch` URLs in `frontend/scripts/prepareToTranslate.js` and `frontend/scripts/convertTranslation.js`.

## API

| Method | Endpoint | Input | Output |
|---|---|---|---|
| `POST` | `/translate/prepare-translate` | Multiple ARB files under `fileList`, plus a `referenceFile` name | Excel workbook |
| `POST` | `/translate/convert-translation` | One `.xlsx` or `.xls` file under `file` | ZIP archive containing ARB files |

## Tests

```bash
cd backend
./mvnw test
```

The test suite covers the ARB/Excel conversion components and includes test doubles for file and Excel readers.

## Docker

From the repository root:

```bash
docker build -t arb-excel-converter .
docker run --rm -p 8080:8080 arb-excel-converter
```

## Notes & Infrastructure

- **Monorepo Refactoring (2026):** Originally developed as separate repositories in 2024, the frontend and backend were consolidated into this single repository. Full Git history has been preserved using unrelated-histories merging.
- **Frontend Deployment:** The UI is automatically deployed to GitHub Pages via a custom GitHub Actions CI/CD pipeline whenever changes are pushed to the `frontend/` directory.
- **Backend Hosting (Cold Start):** The REST API is hosted on a free Render instance. **Please note:** If the API hasn't received traffic in 15 minutes, the first request may take up to 50 seconds to complete while the server wakes up.

## License

MIT — see [LICENSE](LICENSE) for details.

# AI-Powered Study Resource Generator (Gen AI)

A highly scalable, production-ready backend system that leverages Generative AI to dynamically generate study notes, MCQ tests, and daily practice papers. Designed with performance and cost-optimization in mind, this system uses advanced Retrieval-Augmented Generation (RAG) concepts and semantic caching to ensure it never processes the same query twice.

---

## Key System Design Principles

This project was built with enterprise-level system design patterns to handle high traffic and provide a seamless user experience:

- **Scalability**: Designed to scale horizontally. The stateless Spring Boot backend can be replicated across multiple instances behind a load balancer, while PostgreSQL with pgvector handles both relational data and vector scaling effortlessly.
- **High Availability**: The architecture ensures high uptime by decoupling the heavy AI generation tasks from the core retrieval logic. Even if the LLM provider experiences latency, cached semantic hits are served instantly.
- **Data Consistency**: Relies on strict ACID properties of PostgreSQL for user data and scheduling logic, maintaining seamless consistency with the pgvector extension to ensure the semantic search index stays in sync with the primary data store.
- **Cost-Optimization (Semantic Caching)**: Instead of hitting the LLM for every request (which is costly and slow), the system embeds user queries and checks the vector database for a semantic match (cosine similarity ≥ 0.85). Only genuinely new content triggers a Gen AI generation call, drastically reducing API costs and response times.

---

## Generative AI & RAG Architecture

Most AI study tools naively pass every user prompt to an LLM. This system employs an intelligent semantic caching layer:

```text
User Request
     │
     ▼
[ PostgreSQL ] Exact match? ──── YES ──► Return it (O(1) fast path)
     │
     NO
     │
     ▼
[ Hugging Face (all-MiniLM-L6-v2) ] Embed the query into a 384-dimensional vector
     │
     ▼
[ pgvector ] Query Vector DB (Cosine Similarity)
     │
     ├── Score ≥ 0.75 ──► Cache Hit: Return existing note from DB
     │
     └── No match ────► Cache Miss: Generate via Hugging Face LLM
                            │
                            ├── Save payload to PostgreSQL
                            └── Index vector in pgvector
```

---

## Core Features

- **Dynamic Notes Generation**: On-demand, detailed study materials powered by state-of-the-art Gen AI models.
- **Automated Assessments (Tests & DPPs)**: Generates 10-question MCQs with answers. Supports Daily Practice Papers (DPPs) for spaced repetition and streak building.
- **Asynchronous Task Scheduling**: Users can schedule tests and DPPs to arrive in their inbox at specific times via email integrations.
- **Semantic Search Engine**: Ask a natural language question and retrieve the most relevant notes from the global database, ranked by semantic meaning rather than basic keyword matching.
- **Secure Authentication**: Robust session management using GitHub OAuth2.

---

## System Architecture

```text
                        ┌─────────────────────────┐
                        │      Spring Boot API    │
                        │  (Java 17, Spring AI)   │
                        └────────────┬────────────┘
                                     │
               ┌─────────────────────┼─────────────────────┐
               │                                           │
               ▼                                           ▼
    ┌──────────────────┐                  ┌────────────────────────────────┐
    │   Hugging Face   │                  │           PostgreSQL           │
    │  (Gen AI Layer)  │                  │          (Primary DB)          │
    │                  │                  │                                │
    │  Qwen / LLaMA    │                  │  Notes, Topics, Questions      │
    │  (generation)    │                  │                                │
    │                  │                  │       [ pgvector ext ]         │
    │  all-MiniLM-L6-v2│                  │  Vector index (semantic search)│
    │  (embeddings)    │                  │  384-dim cosine distance       │
    └──────────────────┘                  └────────────────────────────────┘
```

---

## Tech Stack

- **Backend Framework**: Spring Boot 3.5 (Java 17, JPA, Spring Security, Scheduling)
- **Generative AI Integration**: Spring AI 1.0, Hugging Face (Qwen / LLaMA, all-MiniLM-L6-v2)
- **Databases**: PostgreSQL (Relational) with `pgvector` extension for Semantic Search
- **Authentication**: OAuth 2.0 (GitHub)
- **Infrastructure / DevOps**: Docker, Docker Compose

---

## API Reference (RESTful)

All routes are prefixed with `/api/v1` and require GitHub OAuth2 authentication (except health checks).

### Notes
| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/note?topic=` | Get or generate a note for a topic (Semantic Cache first) |
| `GET` | `/note/search?query=` | Semantic search across all generated notes |

### Tests & DPPs
| Method | Path | Body | Description |
|--------|------|------|-------------|
| `POST` | `/tests/` | `{ "topic": "..." }` | Generate a test immediately |
| `POST` | `/tests/schedule` | `{ "topic": "...", "date": "...", "time": "..." }` | Schedule a one-time test via email |
| `GET` | `/dpp?topic=` | — | Generate a DPP immediately |
| `POST` | `/dpp/schedule` | `{ "topic": "...", "time": "HH:MM" }` | Schedule a recurring daily DPP |
| `DELETE` | `/tests/schedule` | — | Cancel scheduled test or DPP |

---

## Setup & Installation

### 1. Environment Variables
Create a `.env` file at the root:

```env
# Gen AI (Hugging Face)
HF_MODEL=Qwen/Qwen2.5-72B-Instruct
HF_TOKEN=

# Primary Database (used if running outside of Docker Compose)
DB_URL=jdbc:postgresql://localhost:5432/studydb
DB_USERNAME=postgres
DB_PASSWORD=postgres

# SMTP Mail Server
MAIL_USERNAME=
MAIL_PASSWORD=

# OAuth2 Authentication
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=

# Security
ALLOWED_ORIGINS=http://localhost:3000
```

### 2. Run the Application with Docker Compose
The easiest way to start the system, including the PostgreSQL database with `pgvector` and the Spring Boot application, is using Docker Compose. Ensure you have Docker installed.

```bash
git clone https://github.com/TornovDutta/AI-Powered-Study-Resource-Generator.git
cd AI-Powered-Study-Resource-Generator

# Package the application
./mvnw clean package -DskipTests

# Run the complete stack (app + database)
docker-compose up --build -d
```

### 3. Alternative: Run locally (IDE)
If you prefer to run the application from your IDE (e.g., IntelliJ IDEA), you can start just the database using Docker Compose, or have your own PostgreSQL instance with the `pgvector` extension installed.

```bash
# Start only the database container
docker-compose up db -d

# Run the Spring Boot app locally
./mvnw spring-boot:run
```

The server will start on `http://localhost:8080/api/v1`.
Access the Swagger UI at `http://localhost:8080/api/v1/swagger-ui.html`.

---

Built by [Tornov Dutta](https://github.com/TornovDutta) - Passionate about building scalable Backend Systems and Generative AI applications.

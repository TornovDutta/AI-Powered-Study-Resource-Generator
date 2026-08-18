# AI-Powered Study Resource Generator (Gen AI)

A highly scalable, production-ready backend system that leverages Generative AI to dynamically generate study notes, MCQ tests, and daily practice papers. Designed with performance and cost-optimization in mind, this system uses advanced Retrieval-Augmented Generation (RAG) concepts and semantic caching to ensure it never processes the same query twice.

---

## Key System Design Principles

This project was built with enterprise-level system design patterns to handle high traffic and provide a seamless user experience:

- **Scalability**: Designed to scale horizontally. The stateless Spring Boot backend can be replicated across multiple instances behind a load balancer, while Pinecone and PostgreSQL handle data scaling effortlessly.
- **High Availability**: The architecture ensures high uptime by decoupling the heavy AI generation tasks from the core retrieval logic. Even if the LLM provider experiences latency, cached semantic hits are served instantly.
- **Data Consistency**: Relies on strict ACID properties of PostgreSQL for user data and scheduling logic, while maintaining eventual consistency with the Pinecone Vector Database to ensure the semantic search index stays up-to-date with the primary data store.
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
[ NVIDIA nv-embed-v1 ] Embed the query into a 4096-dimensional vector
     │
     ▼
[ Pinecone ] Query Vector DB (Cosine Similarity)
     │
     ├── Score ≥ 0.85 ──► Cache Hit: Return existing note from DB
     │
     └── No match ────► Cache Miss: Generate via NVIDIA LLaMA 3.1
                            │
                            ├── Save payload to PostgreSQL
                            └── Index vector in Pinecone asynchronously
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
               │                     │                     │
               ▼                     ▼                     ▼
    ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
    │   NVIDIA NIM     │  │    PostgreSQL    │  │    Pinecone      │
    │   (Gen AI Layer) │  │   (Primary DB)   │  │   (Vector DB)    │
    │                  │  │                  │  │                  │
    │  LLaMA 3.1 8B    │  │  Notes, Topics,  │  │  Vector index    │
    │  (generation)    │  │  Questions,      │  │  (semantic       │
    │                  │  │  Users           │  │   search)        │
    │  nv-embed-v1     │  │                  │  │                  │
    │  (embeddings)    │  │                  │  │  4096-dim cosine │
    └──────────────────┘  └──────────────────┘  └──────────────────┘
```

---

## Tech Stack

- **Backend Framework**: Spring Boot 3.5 (Java 17, JPA, Spring Security, Scheduling)
- **Generative AI Integration**: Spring AI 1.0, NVIDIA NIM (LLaMA 3.1 8B, nv-embed-v1)
- **Databases**: PostgreSQL (Relational), Pinecone (Serverless Vector DB)
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
# Gen AI (NVIDIA NIM)
NVIDIA_API_KEY=

# Primary Database
DB_URL=jdbc:postgresql://localhost:5432/studydb
DB_USERNAME=
DB_PASSWORD=

# Vector Database (Pinecone)
PINECONE_API_KEY=
PINECONE_INDEX_HOST=https://your-index.svc.us-east-1.pinecone.io

# SMTP Mail Server
MAIL_USERNAME=
MAIL_PASSWORD=

# OAuth2 Authentication
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=

# Security
ALLOWED_ORIGINS=http://localhost:3000
```

### 2. Vector DB Setup (Pinecone)
1. Register at [Pinecone](https://pinecone.io).
2. Create a serverless index: **Dimensions:** `4096`, **Metric:** `cosine`.
3. Add the API key and Host URL to your `.env`.

### 3. Run the Application
Ensure you have Docker and Java 17 installed.

```bash
git clone https://github.com/TornovDutta/AI-Powered-Study-Resource-Generator.git
cd AI-Powered-Study-Resource-Generator

# (Optional) Run Postgres via Docker Compose
# docker-compose up -d

mvn spring-boot:run
```
The server will start on `http://localhost:8080/api/v1`.
Access the Swagger UI at `http://localhost:8080/api/v1/swagger-ui.html`.

---

Built by [Tornov Dutta](https://github.com/TornovDutta) - Passionate about building scalable Backend Systems and Generative AI applications.

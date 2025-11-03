# 🧠 Intelligent Job Tracker

**Intelligent Job Tracker** is a full-stack, AI-powered recruitment platform that intelligently matches **developer resumes** with **recruiter job descriptions** and enables **real-time meetings** — all self-hosted with no external APIs.

---

## 🚀 Features

### 🧩 Core Modules

* **Job Management** – Create, edit, and delete job postings with embedded descriptions.
* **Resume Management** – Upload and parse candidate resumes (PDF/Text) into searchable data.
* **Semantic Matching** – Uses SentenceTransformer embeddings (`all-mpnet-base-v2`) for resume ↔ job similarity.
* **Meeting System** – In-app recruiter–developer meetings using WebRTC and a Node.js signaling server.
* **Authentication** – JWT-based authentication and role-based access.
* **Admin Panel (Spring Boot)** – Manage users, recruiters, and applications.

---

## 🧱 Architecture Overview

| Layer             | Tech                                   | Role                                       |
| ----------------- | -------------------------------------- | ------------------------------------------ |
| 🗃️ Database      | **PostgreSQL + pgvector**              | Store structured data and embeddings       |
| ☕ Backend         | **Spring Boot 3 + JPA + JWT**          | Business logic, user/job/resume management |
| 🧠 AI Service     | **FastAPI (Python)**                   | Generates embeddings from text             |
| 🔌 Realtime       | **Node.js + WebSocket (Express + ws)** | Handles WebRTC signaling for live meetings |
| 🧩 Frontend       | *(Optional Next.js / React)*           | Recruiter & developer dashboard            |
| 🐳 Infrastructure | **Docker Compose**                     | Multi-container orchestration              |

---

## 🗂️ Project Structure

```
intelligent-job-tracker/
│
├── user-service/                # Spring Boot backend
│   ├── src/main/java/com/jobtracker/
│   ├── pom.xml
│   └── Dockerfile
│
├── python-service/              # FastAPI embedding microservice
│   ├── main.py
│   ├── Dockerfile
│   └── models/                  # Optional fine-tuned models
│
├── realtime-server/             # Node.js signaling server for WebRTC
│   ├── server.js
│   └── Dockerfile
│
├── docker-compose.yml           # Orchestrates all services
├── .env                         # Environment variables
└── README.md
```

---

## ⚙️ Installation

### 1️⃣ Clone the repository

```bash
git clone https://github.com/<your-username>/intelligent-job-tracker.git
cd intelligent-job-tracker
```

### 2️⃣ Create `.env`

```env
# PostgreSQL
POSTGRES_DB=innjobtracker
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# PGAdmin
PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin

# Spring Boot
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/innjobtracker
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JWT_SECRET=supersecretjwt
JWT_EXPIRATION_MS=86400000

# Python Embedding Service
PYTHON_SERVICE_URL=http://python:8001/embed

# Supabase (optional for file storage)
SUPABASE_URL=
SUPABASE_BUCKET=
SUPABASE_SERVICE_KEY=
SUPABASE_PUBLIC_KEY=
```

---

## 🐳 Docker Compose Setup

```bash
docker-compose up --build
```

✅ This will start:

* `innjobtracker-db` → PostgreSQL + pgvector
* `jobtracker-pgadmin` → PGAdmin at `http://localhost:5050`
* `jobtracker-spring` → Java backend at `http://localhost:8080`
* `jobtracker-python` → FastAPI embedding at `http://localhost:8001`
* *(Optional)* `jobtracker-signaling` → WebRTC signaling at `http://localhost:9000`

---

## 🧠 Embedding API (Python Service)

**Endpoint:**
`POST /embed`

**Request:**

```json
{
  "text": "Senior Backend Developer skilled in Java, Spring Boot, and PostgreSQL"
}
```

**Response:**

```json
{
  "embedding": [0.0123, -0.0345, 0.0567, ...]
}
```

---

## ☕ Spring Boot API Examples

### 🔹 Create Job

```http
POST /api/jobs
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "title": "Java Backend Developer",
  "company": "TechCorp",
  "location": "Casablanca",
  "description": "Work on microservices using Spring Boot and PostgreSQL",
  "status": "OPEN"
}
```

### 🔹 Create Resume

```http
POST /api/resumes
```

```json
{
  "fileName": "resume.pdf",
  "parsedText": "Experienced developer skilled in Java, Spring Boot, Docker..."
}
```

### 🔹 Search Matching Jobs

```http
GET /api/jobs/search?query=Spring%20Boot%20developer&threshold=0.7
```

---

## 🎥 Meeting Server (Node.js)

Real-time meetings between recruiters and developers are powered by a **WebSocket signaling server**.

### Run standalone:

```bash
cd realtime-server
npm install
node server.js
```

### Connect via WebRTC

* `/join` → join room
* `/signal` → exchange SDP/ICE
* `/leave` → disconnect

---

## 🧰 Development Commands

### Rebuild all containers

```bash
docker-compose down -v
docker-compose up --build
```

### Access PostgreSQL

```bash
docker exec -it innjobtracker-db psql -U postgres -d innjobtracker
```

### Logs

```bash
docker logs jobtracker-spring
docker logs jobtracker-python
```

---

## 🔐 Authentication

* Users register/login through Spring Boot endpoints.
* JWT tokens are used for all secured endpoints.
* Token validation is performed by a `JwtAuthenticationFilter`.

---

## 💡 Future Enhancements

* 🤖 Resume auto-parser (spaCy + PDFPlumber)
* 🧩 Fine-tuning embeddings for your domain
* 🧠 Feedback-based re-ranking
* 🪄 Meeting recording and chat
* 📈 Analytics dashboard (Next.js + Chart.js)
* ☁️ Cloud deployment (Hostinger / AWS / Railway / Render)

---

## 👨‍💻 Author

**Othman Essaadi**
🚀 Full-stack developer & AI engineer
📍 Morocco

---

## 📜 License

MIT License © 2025 noureddinle

---

**✅ Intelligent Job Tracker — where AI meets recruitment.**

# 📚 AI-Generated Study Material Platform

An AI-powered educational platform built with **Spring Boot** that generates **Notes**, **Daily Practice Papers (DPPs)**, and **Tests** using the OpenAI API.  
The system automatically schedules DPPs and tests and sends email reminders to students.

---

## 🚀 Features

- 🤖 **AI-Generated Notes** — Automatically create subject/topic-specific notes.
- 📄 **Daily Practice Papers (DPPs)** — Generated and scheduled daily.
- 📝 **Test Generation** — Automatic test creation for specific topics.
- 📅 **Automatic Scheduling** — DPPs and tests are scheduled without manual intervention.
- 📧 **Email Reminders** — Sends reminders for upcoming DPPs/tests.
- 🌐 **Deployed on Render** — Accessible online via a live demo.

---

## 🛠 Tech Stack

**Backend:**
- Java 17+
- Spring Boot (Web, Data JPA, Scheduling, Mail)
- Hibernate / JPA
- PostgreSQL (Database)

**AI Integration:**
- OpenAI API (ChatGPT/GPT models)

**Others:**
- Spring Scheduler / Quartz (for DPP/Test scheduling)
- JavaMailSender (for email reminders)
- Lombok (reduce boilerplate code)

---

## ⚙️ Installation & Setup

### 1️⃣ Clone the repository
```bash
git clone https://github.com/yourusername/ai-study-material.git
cd ai-study-material
```

### 2️⃣ Configure Database
Update `application.properties` or `application.yml`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ai_study
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 3️⃣ Add OpenAI API Key
```properties
openai.api.key=YOUR_OPENAI_API_KEY
```

### 4️⃣ Configure Email Settings
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 5️⃣ Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 REST API Endpoints

| Method | Endpoint               | Description |
|--------|------------------------|-------------|
| **POST**   | `/notes/generate`       | Generate AI-powered study notes for a given topic. |
| **GET**    | `/notes/{id}`           | Get a specific note by its ID. |
| **GET**    | `/notes`                | Get all generated notes. |
| **POST**   | `/dpp/generate`         | Generate a Daily Practice Paper (DPP) for a given topic. |
| **GET**    | `/dpp/{id}`             | Get a specific DPP by its ID. |
| **GET**    | `/dpp`                  | Get all generated DPPs. |
| **POST**   | `/tests/generate`       | Generate a test for a given topic. |
| **GET**    | `/tests/{id}`           | Get a specific test by its ID. |
| **GET**    | `/tests`                | Get all generated tests. |
| **POST**   | `/schedule/dpp`         | Schedule a DPP at a specific date/time. |
| **POST**   | `/schedule/test`        | Schedule a test at a specific date/time. |
| **POST**   | `/email/reminder`       | Send a manual email reminder. |

---

## 📂 Project Structure
```
src/main/java/com/example/aiStudyMaterial
│── controller/      # API endpoints
│── service/         # Business logic
│── repository/      # JPA repositories
│── model/           # Entities (User, Notes, DPP, Test, etc.)
│── scheduler/       # Scheduled tasks for DPP/Test
```

---

## 📜 License
This project is licensed under the MIT License.

---

## 👨‍💻 Author
**Tornov Dutta**  
📧 your-email@example.com  
🌐 [LinkedIn](https://linkedin.com/in/yourprofile) | [GitHub](https://github.com/yourusername)

# 🚀 My Atomic Transaction Engine

This project is my personal deep dive into building a high-concurrency **distributed transaction engine**. I'm tackling core distributed systems challenges: **atomicity**, **idempotency**, **concurrency control**, and **high-load performance**, using a powerful tech stack: **Spring Boot**, **Redis Lua**, **Kafka**, **Docker**, and **k6**.

My goal is not just to build it, but to truly understand *why* each component is essential for system correctness under pressure.

## 🗺️ The Architecture I'm Building

This is the asynchronous ingestion pipeline designed for reliability and massive transaction volume. 

| Component | Why I'm Using It | Key Concept I Will Master |
| :--- | :--- | :--- |
| **Client/k6** | To simulate real-world, high-volume stress (Goal: $\text{2,000+ RPS}$). | Load Testing & Performance Benchmarking |
| **Spring Boot API** | My entry point for transactions; handles validation and publishing. | Producer Reliability |
| **Kafka** | To decouple the ingestion from the processing, absorbing large spikes. | At-Least-Once Delivery & Message Durability |
| **Spring Consumer** | Reads messages off the queue and applies the core logic. | Idempotency and Consumer Groups |
| **Redis Lua** | **Crucial** for executing balance updates in a single, **atomic** operation. | Race-Condition Elimination |
| **Redis** | My blazing-fast, in-memory source of truth for user balances. | State Management & Low-Latency Access |

### My Key Learning Goals:

* **✔ Atomic Updates:** Guaranteeing data integrity with Lua scripting.
* **✔ High Throughput:** Mastering the Kafka asynchronous pipeline setup.
* **✔ Idempotency:** Ensuring that retries or duplicates don't break the bank (literally).
* **✔ Performance:** Benchmarking the system's limits with k6 ($\text{2,000+ RPS}$).
* **✔ Orchestration:** Running the entire multi-service system with Docker Compose.

---

## 📅 The Build Journey

I will tackle this project sequentially. The complexity builds phase-by-phase, ensuring a solid foundation.

### 1. 🚀 Phase 1: Spring Boot Fundamentals

* **Goal:** Set up the basic transaction REST API.
* **Action:** Create the skeleton, define the `TransactionRequest` DTO, and map `POST /transaction`.
* **Outcome:** A running application ready to accept traffic.

### 2. 🔥 Phase 2: Redis Fundamentals (The State Store)

* **Goal:** Integrate Redis for low-latency balance storage.
* **Action:** Connect via `StringRedisTemplate`. Implement `GET /balance/{userId}` and a setter.
* **Key:** Balances are stored simply as `balance:userId` → *integer*.
* **Outcome:** I can read and write simple key-value state.

### 3. ⚡ Phase 3: Atomic Transactions using Redis Lua (The Core Integrity)

* **Goal:** Eliminate race conditions with atomic scripts. This is critical.
* **Action:** Write and load `/lua/atomic_update.lua`. The script must check if `balance >= amount` and, if so, atomically execute `DECRBY`.
* **Key:** The Lua script executes as a single command, guaranteeing atomicity.
* **Outcome:** My balance update logic is now race-condition-free and safe.

### 4. 📡 Phase 4: Kafka Asynchronous Ingestion Pipeline (The Throughput Engine)

* **Goal:** Move processing off the REST thread to handle extreme load asynchronously.
* **Action:** Implement the **Kafka Producer** (in the API) and the **Kafka Consumer**. The Consumer calls the atomic Lua script.
* **Learning Focus:** Understanding **At-least-once delivery**, **Partitioning** for concurrency, and **Consumer Groups**.
* **Outcome:** The full asynchronous data flow: `POST /transaction` → **Kafka** → **Consumer** → **Lua Script** → **Redis**.

### 5. 🛡️ Phase 5: Distributed Correctness: Idempotency + Reliability (The Safety Net)

* **Goal:** Make the system bulletproof against consumer crashes or message retries (duplicate events).
* **Action:** Implement idempotency. Before processing a message, check if the `tx:{id}:processed` flag exists in Redis. Skip if it does.
* **Learning Focus:** The practical meaning of **Idempotency** and why it's necessary with **At-least-once semantics**.
* **Outcome:** My consumer logic is now **crash-safe** and won't double-spend money.

### 6. 🧪 Phase 6: Load Testing With k6 (The Stress Test)

* **Goal:** Prove the system can handle the required load and find bottlenecks.
* **Action:** Write the `k6/load.js` script (e.g., $\text{200 VUs}$ for $\text{20s}$). Post transactions to the REST endpoint.
* **Objective:** Hit a sustained **$\text{2,000+ requests/second}$** locally.
* **Outcome:** Data-driven confidence in the engine's performance limits.

### 7. 🐳 Phase 7: Docker Compose Orchestration (The Repeatable Environment)

* **Goal:** Package everything into a self-contained, repeatable environment.
* **Action:** Create `docker-compose.yml` to spin up **Redis**, **Kafka**, and my **Spring Service** easily.
* **Optional:** Add volume persistence for Kafka and service health checks.
* **Outcome:** A complete, portable microservice stack running with a single command.

---

## ✅ Final Milestones

I will consider the project complete when I've achieved these demonstrations:

* **✔** Full **Asynchronous Pipeline** operational.
* **✔** **Atomic** and **Race-Free** updates confirmed.
* **✔** **Idempotency** proved by replaying duplicate messages without error.
* **✔** **$\text{2,000+ RPS}$** sustained performance validated by k6.
* **✔** Clean code and documented setup.

## 💡 What's Next? (Future Learning)

If I want to push my knowledge further:

* **Observability:** Adding Prometheus and Grafana for real-time metrics.
* **Advanced Transactions:** Researching **Saga** patterns for multi-step, distributed transaction consistency.
* **Rate Limiting:** Implementing a Redis-backed rate limiter on the ingestion API.

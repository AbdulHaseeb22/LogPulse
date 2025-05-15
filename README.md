# 🚦 LogPulse — Real-time HTTP Request Log Analyzer

**LogPulse** is a modular Java + Spring Boot project for **capturing**, **streaming**, and **analyzing HTTP request logs** in real time. It simulates a miniature version of an ELK stack, using **Kafka** for transport and Java-based analytics for reporting insights like:

- 📊 HTTP status code distribution
- 🐢 Slowest responding endpoints
- ❌ Top IPs causing 404 errors

---

## ⚙️ Architecture

```text
                    ┌──────────────┐
                    │ User Request │
                    └────┬─────────┘
                         ↓
               ┌─────────────────────┐
               │  log-capture (API)  │
               │  (Spring Boot App)  │
               └────────┬────────────┘
                        │ Logs HTTP requests (via filter)
                        ↓
              ┌─────────────────────┐
              │      Kafka          │
              │  (log-events topic) │
              └────────┬────────────┘
                       ↓
         ┌────────────────────────────┐
         │ log-analyzer (Java App)    │
         │ - Consumes logs from Kafka │
         │ - Parses + aggregates      │
         │ - Prints real-time reports │
         └────────────────────────────┘


```

## 🌟 Features

* ✅ Captures real HTTP requests from a live Spring Boot app
* ✅ Streams logs into Apache Kafka
* ✅ Parses logs and converts to structured Java objects
* ✅ Aggregates:
    * Top 404-causing IPs
    * Slowest requests
    * HTTP status code breakdown
* ✅ Real-time console reports every 30 seconds
* ✅ Modular architecture (cleanly separated producer and consumer)
* ✅ Uses Spring context + KafkaListenerContainer

## 🚀 Getting Started

### 🧱 Prerequisites

* Java 17
* Apache Kafka running locally (`localhost:9092`)
* Maven

💡 You can use Docker Compose to spin up Kafka quickly (see below)

### 1️⃣ Start Kafka (via Docker)

```yaml
# docker-compose.yml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
  kafka:
    image: confluentinc/cp-kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

Run with:
```bash 
docker-compose up -d
```

### 2️⃣ Run the Log Capture API

```bash
cd log-capture
mvn spring-boot:run
```

Then test it:
```bash
curl http://localhost:8085/ping
curl http://localhost:8085/invalid-endpoint
```

### 3️⃣ Run the Log Analyzer

```bash
cd log-analyzer
mvn clean compile exec:java "-Dexec.mainClass=com.lg.App"
```

You'll start seeing output like:

```
📊 Report:
🔟 Top 404 IPs: {127.0.0.1=3}
🐢 Slowest Requests: [2025-05-15T12:30:01] 127.0.0.1 GET /login -> 404 (48ms)
📊 Status Counts: {200=12, 404=3}
```

## 📁 Project Structure

```
.
├── log-capture/          # Spring Boot producer app
│   └── ...               # Filters, Kafka config
├── log-analyzer/         # Java consumer app
│   └── ...               # Parser, Aggregator, Report
├── docker-compose.yml    # Kafka/Zookeeper setup
├── README.md
└── .gitignore
```

## 🔧 TODO / Extensions

* Export reports to `JSON` or `CSV`
* Add REST endpoint to fetch reports
* Add WebSocket support for live UI dashboard

## 📄 License

MIT — free to use and extend.

## ✨ Author

**@AbdulHaseeb22** — Built with passion for backend analytics, distributed systems, and clean software architecture.
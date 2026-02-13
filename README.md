# SentinelFlow ⚡

**A High-Performance, Reactive Audit & Budgeting Ecosystem built with Spring Boot 3.2+ (Coroutines) and Native Jetpack Compose.**

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.23-blue.svg)](http://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen.svg)](https://spring.io/projects/spring-boot/)
[![R2DBC](https://img.shields.io/badge/R2DBC-Reactive-purple.svg)](https://r2dbc.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Android](https://img.shields.io/badge/Android-Native_Compose-3DDC84.svg)](https://developer.android.com/jetpack/compose)

---

## 📖 Table of Contents

- [SentinelFlow ⚡](#sentinelflow-)
  - [📖 Table of Contents](#-table-of-contents)
  - [1. Introduction](#1-introduction)
    - [What is SentinelFlow?](#what-is-sentinelflow)
    - [Why SentinelFlow?](#why-sentinelflow)
  - [2. Key Features \& Differentiators](#2-key-features--differentiators)
    - [📱 Top-Tier Native Android Experience](#-top-tier-native-android-experience)
    - [⚡ Reactive Backend Mastery](#-reactive-backend-mastery)
  - [3. Technology Stack](#3-technology-stack)
    - [**Backend (The Engine)**](#backend-the-engine)
    - [**Mobile (The Interface)**](#mobile-the-interface)
  - [4. Architectural Overview](#4-architectural-overview)

---

## 1. Introduction

### What is SentinelFlow?

SentinelFlow is a highly efficient, event-driven ecosystem designed to ingest, process, and audit financial transactions in real-time. It bridges an enterprise-grade **Spring Boot WebFlux** backend with a high-fidelity **Native Android** application, delivering sub-millisecond updates via Server-Sent Events (SSE).

### Why SentinelFlow?

Modern financial systems demand more than just CRUD operations. This project demonstrates a mastery of **Reactive Systems** and **Native UI Architecture**, moving beyond simple "WebView wrappers" to build a true native experience that leverages hardware-backed security and non-blocking I/O.

---

## 2. Key Features & Differentiators

### 📱 Top-Tier Native Android Experience

- **iMessage-Quality Chat:** A professional messaging system featuring high-fidelity bubbles with dynamic "tail" shapes, sender avatars, and full support for threaded replies and long-press gestures.
- **Glowing Role Badges:** A custom-built RBAC UI where `ADMIN` and `MODERATOR` users feature glowing, high-contrast badges based on system roles defined in the backend.
- **Wide Performance Analytics:** Real-time, glowing sparkline charts built with Jetpack Compose Canvas that jump instantly as the backend streams new transaction data.

### ⚡ Reactive Backend Mastery

- **Functional Routing (`coRouter`):** Utilizes Spring WebFlux's functional routing API with Kotlin's `coRouter` DSL for a composable, type-safe API definition.
- **R2DBC Persistence:** Truly reactive database interactions with PostgreSQL, eliminating blocking JDBC calls and maximizing throughput.
- **SSE Streaming:** A persistent Event Stream pushes transaction stats and spending trends directly to the mobile UI without polling.

---

## 3. Technology Stack

### **Backend (The Engine)**

- **Language:** Kotlin 1.9.23 (JVM 21)
- **Framework:** Spring Boot 3.2.4 (WebFlux, Data R2DBC, Security)
- **Database:** PostgreSQL 16 + Supabase (RLS & Storage)
- **Concurrency:** Kotlin Coroutines & Flow

### **Mobile (The Interface)**

- **UI:** Jetpack Compose (Material 3)
- **Networking:** Retrofit 2 + OkHttp SSE
- **Security:** BiometricPrompt API & EncryptedSharedPreferences
- **Image Loading:** Coil (Supabase Storage integration)

---

## 4. Architectural Overview

[Image of a Full-Stack Reactive Architecture Diagram showing an Android Client, a Spring Boot Microservice, and a PostgreSQL/Supabase Database]

```text
+----------------+       +-------------------+       +-------------------+
|  Native Android|       |  Spring WebFlux   |       |   SentinelFlow    |
|   (Compose)    <------->  (coRouter /      <------->    Application    |
|  [SSE Client]  |       |   SSE Endpoint)   |       | (Coroutines, Flow)|
+-------^--------+       +---------^---------+       +---------^---------+
        |                          |                           |
        |      JWT Auth / SSE      |          R2DBC            |
        +--------------------------+---------------------------+
                                   |
                           +-------v-------+
                           |   PostgreSQL  |
                           |  (Audit Logs) |
                           +---------------+

---

├── backend/
│   ├── api/                  # coRouter, Functional Handlers (Auth, Tx, User)
│   ├── domain/               # Pure Kotlin Business Logic (Sealed Classes, Value Classes)
│   ├── service/              # Use-Case Orchestration (Transaction Streams)
│   └── repository/           # R2DBC Interfaces & PostgreSQL Entities
└── mobile/
    ├── ui/                   # Jetpack Compose Screens (iMessage Bubbles, Charts)
    ├── viewmodel/            # LiveMonitor (SSE Collection)
    ├── security/             # BiometricAuthenticator & AuthInterceptors
    └── api/                  # Retrofit & OkHttp Service Definitions

---

```

6. API Endpoints
   GET /api/transactions/stream
   Establishes an SSE connection to receive live transaction events.

Content-Type: text/event-stream

Data: { "amount": 150.75, "category": "Cloud", "timestamp": "..." }

GET /api/dashboard/stats
Consolidated endpoint for the "Financial Overview" cards.

Returns: totalVolume, avgValue, transactionCount

7. Security & Compliance
   Biometric Vault: Native Android Fingerprint/Face authentication gatekeeping sensitive financial data.

Admin Audit Logs: Every security-sensitive change (2FA toggles, role changes) is automatically captured in a JSONB audit trail via PostgreSQL Triggers.

Row Level Security (RLS): Strict Supabase policies ensure users and moderators only access authorized data slices.

Password Strength Engine: A native meter enforcing capital letters, digits, and length requirements with real-time visual feedback.

```

---
* Full-Stack Proficiency: Demonstrates the ability to own the entire data lifecycle, from a PostgreSQL row to an animated Android chart.

* Advanced Concurrency: Heavy use of Kotlin Coroutines and Flow across both backend and frontend to handle asynchronous streams elegantly.

* High Fidelity UI: Focus on "top tier" UX, specifically the iMessage-style chat and glowing Admin badges, showing attention to detail.

* Clean Architecture: Strict separation between framework-specific code and pure domain business logic.

---
```

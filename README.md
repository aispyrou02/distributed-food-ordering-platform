# 🚀 Distributed Food Ordering Platform

A full-stack food ordering system featuring a **distributed Java backend** and a **mobile frontend**, designed using a **Master–Worker–Reducer architecture** for scalability and parallel processing.

---

## 📌 Overview

This project simulates a real-world food delivery platform where:

* Customers can browse stores, search with filters, purchase products, and rate stores
* Managers can manage stores, products, and inventory
* The backend distributes data across multiple worker nodes
* A reducer node aggregates distributed results

The system demonstrates key distributed systems concepts such as:

* request routing
* parallel execution
* data partitioning
* result aggregation

---

## 🧠 System Architecture

```text
                    ┌────────────────────┐
                    │   Mobile / CLI     │
                    │   Client Apps      │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │       Master       │
                    │   (Coordinator)    │
                    └─────────┬──────────┘
                ┌─────────────┼─────────────┐
                │             │             │
                ▼             ▼             ▼
         ┌──────────┐  ┌──────────┐  ┌──────────┐
         │ Worker 1 │  │ Worker 2 │  │ Worker N │
         └────┬─────┘  └────┬─────┘  └────┬─────┘
              │              │              │
              └──────┬───────┴───────┬─────┘
                     ▼               ▼
                ┌────────────────────────┐
                │        Reducer         │
                │   (Aggregation Node)   │
                └──────────┬─────────────┘
                           │
                           ▼
                       Response
```

---

## ⚙️ How It Works

### 🔹 Request Flow

1. Client sends request → Master
2. Master decides:

   * **Single worker** → based on hash(store name)
   * **All workers** → for search / allStores
3. Workers process request
4. Reducer aggregates results (if needed)
5. Master sends response back to client

---

### 🔹 Data Distribution

* Stores are distributed across workers using:

  ```java
  hash(storeName) % numberOfWorkers
  ```
* Ensures scalability and load distribution

---

## 🧩 System Components

### 🧭 Master Node

* Entry point of the system
* Routes requests to workers
* Handles aggregation responses

---

### ⚙️ Worker Nodes

* Store and manage:

  * stores
  * products
  * inventory
* Execute:

  * buy
  * update
  * rating
  * filtering

---

### 🔄 Reducer Node

* Collects results from all workers
* Combines them into a single response
* Used for:

  * search
  * allStores

---

### 📱 Applications

#### 👤 Client App

* Browse stores
* Search with filters
* Buy products
* Rate stores

#### 🧑‍💼 Manager App

* Add stores (via JSON)
* Manage products
* Update stock
* View sales

---

## ✨ Features

### Customer

* View all stores
* Search with:

  * price range ($, $$, $$$)
  * rating range
  * category
* Buy products
* Rate stores

---

### Manager

* Add store from JSON
* Add/remove products
* Update stock
* Track product sales

---

## 🛠 Tech Stack

* Java
* TCP Socket Programming
* Multithreading
* Object Serialization
* Gson (JSON parsing)

---

## 📂 Project Structure

```text
backend/
│
├── model/
│   ├── Store.java
│   ├── Product.java
│   └── Message.java
│
├── network/
│   ├── Master.java
│   ├── Worker.java
│   └── Reducer.java
│
├── app/
│   ├── ClientApp.java
│   └── ManagerApp.java
│
├── lib/
│   └── gson-2.9.1.jar
│
├── sample-data/
│   ├── store.json
│   └── product.json
│
└── docs/
    └── architecture.png
```

---

## ▶️ How to Run

### 1. Compile

```bash
javac -cp ".:lib/gson-2.9.1.jar" backend/**/*.java
```

---

### 2. Start Reducer

```bash
java network.Reducer <reducer_port> <master_port> <num_workers>
```

Example:

```bash
java network.Reducer 6000 5000 2
```

---

### 3. Start Workers

```bash
java network.Worker <worker_port> <reducer_port>
```

Example:

```bash
java network.Worker 7000 6000
java network.Worker 7001 6000
```

---

### 4. Start Master

```bash
java network.Master <master_port> <worker_ports...>
```

Example:

```bash
java network.Master 5000 7000 7001
```

---

### 5. Run Applications

```bash
java app.ClientApp 5000
java app.ManagerApp 5000
```

---

## 📄 Example Store JSON

```json
{
  "StoreName": "PizzaPlace",
  "Latitude": 40.0,
  "Longitude": 22.0,
  "FoodCategory": "Italian",
  "Stars": 4.5,
  "NoOfVotes": 100,
  "StoreLogo": "logo.png",
  "Products": [
    {
      "ProductName": "Pizza",
      "ProductType": "Food",
      "Available Amount": 10,
      "Price": 12.5
    }
  ]
}
```

---

## 📚 Key Concepts Demonstrated

* Distributed systems architecture
* Hash-based partitioning
* Parallel processing
* Aggregation via reducer
* Socket communication
* Concurrency with threads

---

## ⚖️ Design Trade-offs

| Decision           | Benefit             | Trade-off            |
| ------------------ | ------------------- | -------------------- |
| Raw sockets        | Low-level control   | Harder to scale      |
| In-memory storage  | Fast                | No persistence       |
| Hash partitioning  | Simple distribution | Possible imbalance   |
| Thread-per-request | Easy concurrency    | Not optimal at scale |

---

## 🔮 Future Improvements

* REST API (Spring Boot)
* Database (PostgreSQL / MongoDB)
* Dockerization
* Load balancing
* Fault tolerance
* Authentication system
* Unit & integration tests

---

## 📜 Author
 Spyrou Agamemnon-Ioannis


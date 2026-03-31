# 🚀 Distributed Food Ordering Platform

A full-stack food ordering system featuring a **distributed Java backend** and a **mobile frontend**, designed using a **master–worker–reducer architecture** for scalability and parallel processing.

---

## 📌 Overview

This project simulates a real-world food delivery platform where:

* Customers can browse stores, search with filters, purchase products, and rate stores
* Managers can manage stores, products, and inventory
* The backend distributes data across multiple worker nodes for scalability
* A reducer node aggregates distributed results

This system demonstrates core distributed systems concepts such as **request routing, parallel processing, and result aggregation**.

---

## 🧠 System Design

### Architecture Pattern

* **Master–Worker–Reducer**

### Components

#### 🔹 Master Node

* Routes requests using **hash-based partitioning**
* Coordinates communication between clients and workers
* Handles both targeted and broadcast requests

#### 🔹 Worker Nodes

* Store and manage data (stores, products, sales)
* Execute business logic:

  * product updates
  * purchases
  * rating
  * filtering

#### 🔹 Reducer Node

* Aggregates responses from multiple workers
* Required for:

  * global search
  * retrieving all stores

#### 🔹 Client Applications

* Customer interface (CLI / mobile app)
* Manager/admin interface

---

## ⚙️ Key Features

### 👤 Customer Features

* View all stores
* Search with filters:

  * Price range ($, $$, $$$)
  * Star rating range
  * Food category
* Purchase products
* Rate stores

### 🧑‍💼 Manager Features

* Add store via JSON input
* Add/remove products
* Update stock levels
* Retrieve total sales per product

---

## 🛠 Tech Stack

* **Java**
* **TCP Socket Programming**
* **Multithreading**
* **Object Serialization**
* **Gson (JSON parsing)**

---

## 📂 Project Structure

```
distributed-food-ordering-platform/
│
├── backend/
│   ├── model/
│   │   ├── store.java
│   │   ├── product.java
│   │   └── message.java
│   │
│   ├── network/
│   │   ├── master.java
│   │   ├── worker.java
│   │   └── reducer.java
│   │
│   ├── app/
│   │   ├── ClientApp.java
│   │   └── ManagerApp.java
│   │
│   └── lib/
│       └── gson-2.9.1.jar
│
├── mobile-app/
│   └── (frontend project)
│
├── sample-data/
│   ├── store.json
│   └── product.json
│
├── docs/
│   └── architecture.png
│
├── README.md
└── .gitignore
```

---

## ▶️ How to Run

### 1. Compile

```
javac -cp ".:lib/gson-2.9.1.jar" backend/**/*.java
```

---

### 2. Start Reducer

```
java reducer <reducer_port> <master_port> <number_of_workers>
```

Example:

```
java reducer 6000 5000 2
```

---

### 3. Start Workers

```
java worker <worker_port> <reducer_port>
```

Example:

```
java worker 7000 6000
java worker 7001 6000
```

---

### 4. Start Master

```
java master <master_port> <worker_port_1> <worker_port_2> ...
```

Example:

```
java master 5000 7000 7001
```

---

### 5. Run Applications

#### Customer App

```
java ClientApp 5000
```

#### Manager App

```
java ManagerApp 5000
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

* Distributed system design
* Hash-based data partitioning
* Parallel request handling
* Aggregation via reducer pattern
* Socket-based inter-process communication
* Thread-based concurrency

---

## ⚖️ Design Trade-offs

* **Sockets vs REST API**

  * Used raw sockets for learning low-level networking
  * Trade-off: less scalability compared to HTTP-based services

* **In-memory storage**

  * Faster performance
  * Trade-off: no persistence or fault tolerance

* **Hash-based partitioning**

  * Simple and efficient
  * Trade-off: uneven distribution if data is skewed

---

## 🔮 Future Improvements

* Replace sockets with REST (Spring Boot)
* Add database persistence (PostgreSQL / MongoDB)
* Introduce load balancing
* Add fault tolerance and replication
* Dockerize services
* Add authentication and user accounts
* Implement automated tests

---

## 💡 Why This Project Matters

This project demonstrates:

* Real-world **distributed backend architecture**
* Strong understanding of **concurrency and networking**
* Ability to design **scalable systems**
* Full-stack thinking (backend + mobile frontend)

---


## 📜 Author
  Spyrou Agamemnon-Ioannis

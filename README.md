# 🚀 Distributed Food Ordering Platform

A full-stack food ordering platform featuring a **distributed Java backend** and an **Android mobile frontend**, designed around a **Master–Worker–Reducer architecture** for scalable request routing, parallel processing, and distributed data handling.

The project simulates a real-world food delivery system where customers can browse stores, filter results, buy products, and rate stores, while managers can add and manage store data through the backend.

---

## 📚 Table of Contents

- [📌 About](#-about)
- [✨ Features](#-features)
- [🧠 System Architecture](#-system-architecture)
- [🔄 How It Works](#-how-it-works)
- [🧩 System Components](#-system-components)
- [📱 Android Frontend](#-android-frontend)
- [🖥️ Java Backend](#️-java-backend)
- [🛠️ Tech Stack](#️-tech-stack)
- [📂 Project Structure](#-project-structure)
- [▶️ How to Run](#️-how-to-run)
- [📱 Android Emulator Networking](#-android-emulator-networking)
- [📄 Example Store JSON](#-example-store-json)
- [📚 Key Concepts Demonstrated](#-key-concepts-demonstrated)
- [⚖️ Design Trade-offs](#️-design-trade-offs)
- [⚠️ Known Notes](#️-known-notes)
- [🔮 Future Improvements](#-future-improvements)
- [👤 Author](#-author)
- [📜 License](#-license)

---

## 📌 About

**Distributed Food Ordering Platform** is a full-stack academic project that combines a distributed backend system with an Android mobile client.

The backend is implemented in Java and uses TCP sockets, multithreading, object serialization, and a Master–Worker–Reducer architecture. The Master coordinates requests, Workers store and process data, and the Reducer aggregates distributed results.

The Android frontend acts as the customer-facing mobile app. It allows users to enter delivery information, browse food stores, apply filters, view store products, place orders, and rate stores after a successful purchase.

The project demonstrates how a mobile application can communicate with a distributed backend using socket-based communication and serialized Java objects.

---

## ✨ Features

### 👤 Customer Features

- 🏪 Browse available food stores
- 🔍 Search and filter stores by:
  - 💸 Price range
  - ⭐ Rating range
  - 🍕 Food category
- 🏬 Open a store page
- 🍽️ View active products of a store
- ➕ Increase product quantity
- ➖ Decrease product quantity
- 🛒 Buy selected products
- ⭐ Rate stores after purchase
- 👤 Store delivery information locally in the app flow

---

### 🧑‍💼 Manager Features

- 🏪 Add new stores through JSON input
- 🍽️ Add and manage products
- 📦 Update product stock
- ❌ Remove or deactivate stores/products
- 📊 View store and product sales information
- 🧾 Manage store inventory data through the backend

---

### 🧠 Distributed Backend Features

- 🧭 Master node for request coordination
- ⚙️ Worker nodes for distributed data storage and processing
- 🔄 Reducer node for result aggregation
- 🧵 Multithreaded request handling
- 🌐 TCP socket communication
- 📦 Java object serialization
- 🧮 Hash-based data partitioning
- 🔍 Parallel search across workers

---

## 🧠 System Architecture

```text
                    ┌──────────────────────────┐
                    │   Android / CLI Client   │
                    │  Customer & Manager Apps │
                    └─────────────┬────────────┘
                                  │
                                  │ TCP Socket Request
                                  ▼
                    ┌──────────────────────────┐
                    │          Master          │
                    │      Coordinator Node    │
                    └─────────────┬────────────┘
                  ┌───────────────┼───────────────┐
                  │               │               │
                  ▼               ▼               ▼
          ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
          │   Worker 1   │ │   Worker 2   │ │   Worker N   │
          │ Store Shard  │ │ Store Shard  │ │ Store Shard  │
          └──────┬───────┘ └──────┬───────┘ └──────┬───────┘
                 │                │                │
                 └────────┬───────┴───────┬────────┘
                          │               │
                          ▼               ▼
                    ┌──────────────────────────┐
                    │         Reducer          │
                    │    Aggregation Node      │
                    └─────────────┬────────────┘
                                  │
                                  ▼
                             Response
```

---

## 🔄 How It Works

### 🔹 Request Flow

1. 📱 The Android app or CLI client sends a request to the Master.
2. 🧭 The Master checks the request type.
3. 🧮 For store-specific actions, the Master selects one Worker using hash-based routing.
4. 🔍 For search/all-store actions, the Master forwards the request to all Workers.
5. ⚙️ Workers process the request locally.
6. 🔄 The Reducer aggregates results when multiple Workers are involved.
7. 📤 The Master sends the final response back to the client.

---

### 🔹 Data Distribution

Stores are distributed across Workers using hash-based partitioning:

```java
hash(storeName) % numberOfWorkers
```

This allows the backend to split store data across multiple nodes and route store-specific operations directly to the correct Worker.

---

### 🔹 Search and Aggregation

For global operations such as searching stores, the request is sent to every Worker.

Each Worker returns matching results from its own local store list. The Reducer combines all partial results into a single response and sends it back through the Master.

---

## 🧩 System Components

### 🧭 Master Node

The Master is the main entry point of the backend.

Responsibilities:

- 🌐 Accept client connections
- 🧭 Route requests to the correct Worker
- 🔍 Broadcast search requests to all Workers
- 🔄 Coordinate reducer-based responses
- 📤 Send final responses back to clients

---

### ⚙️ Worker Nodes

Workers store and process distributed store data.

Responsibilities:

- 🏪 Store assigned stores
- 🍽️ Manage products
- 📦 Manage product stock
- 🛒 Process purchases
- ⭐ Update store ratings
- 🔍 Execute filtering/search logic
- 📊 Track product/store sales

---

### 🔄 Reducer Node

The Reducer aggregates responses from multiple Workers.

Responsibilities:

- 📥 Receive partial results from Workers
- 🔄 Merge distributed search results
- 📦 Build a final combined response
- 📤 Return aggregated results to the Master

---

### 📱 Android Mobile Client

The Android app provides the customer-facing interface.

Responsibilities:

- 👤 Collect delivery/user information
- 🏪 Display stores
- 🔍 Apply search filters
- 🏬 Display store details
- 🍽️ Display products
- 🛒 Submit purchase requests
- ⭐ Submit store ratings

---

### 💻 CLI Client App

The CLI client can be used for testing backend customer operations.

Typical operations:

- Browse stores
- Search stores
- Buy products
- Rate stores

---

### 🧑‍💼 Manager App

The manager app is used to manage store data.

Typical operations:

- Add stores from JSON files
- Add products
- Remove products
- Update stock
- View sales statistics

---

## 📱 Android Frontend

The Android frontend is implemented using Java and XML layouts.

### 📱 Main Android Screens

```text
👤 User Info Screen
        ↓
🏪 Browse Stores Screen
        ↓
🔍 Filters Screen
        ↓
🏬 Store Page
        ↓
🛒 Purchase Products
        ↓
⭐ Rate Store
```

---

### 🧩 Main Android Files

```text
app/src/main/java/com/example/ks_android_app/
├── mainActivity.java
├── browse_stores_activity.java
├── filters_activity.java
├── store_activity.java
├── store_adapter.java
├── store_model.java
├── product_adapter.java
├── product_model.java
├── user.java
├── store.java
├── product.java
└── message.java
```

---

### 📄 Main Android Layouts

```text
app/src/main/res/layout/
├── user_info.xml
├── activitymain.xml
├── filters.xml
├── store_page.xml
├── store_row.xml
└── product_row.xml
```

---

### 🔍 Android Filters

The app supports filtering stores by:

- 🍕 Category
- ⭐ Rating range
- 💸 Price range

The selected filters are stored in the `user` object and sent to the backend during search requests.

---

### 🛒 Android Purchase Flow

The user opens a store, selects product quantities, and completes the purchase.

If the required delivery information is missing, the app redirects the user to the user information screen before allowing the order to continue.

---

## 🖥️ Java Backend

The backend is implemented in Java and communicates with clients through sockets.

### 🧭 Backend Nodes

```text
Master
Worker 1
Worker 2
Worker N
Reducer
```

---

### ✉️ Message-Based Communication

The backend uses serializable `message` objects to transfer commands and data between clients, Master, Workers, and Reducer.

Examples of message commands:

```text
Search
Buy
rateStore
addStore
addProduct
removeProduct
updateStock
```

---

### 🧵 Multithreading

Each backend node can handle multiple connections by creating request-handling threads.

This allows multiple clients or operations to be processed concurrently.

---

### 📦 Object Serialization

The project uses Java object serialization through:

```java
ObjectOutputStream
ObjectInputStream
```

This allows Java objects such as stores, products, users, and messages to be transferred between different parts of the system.

---

## 🛠️ Tech Stack

### 📱 Frontend

- Java
- Android SDK
- XML Layouts
- AppCompat
- RecyclerView
- CardView
- ConstraintLayout

### 🖥️ Backend

- Java
- TCP Socket Programming
- Multithreading
- Object Serialization
- Gson for JSON parsing

### 🧠 Architecture

- Master–Worker–Reducer
- Hash-based partitioning
- Parallel search
- Reducer-based aggregation

---

## 📂 Project Structure

```text
distributed-food-ordering-platform/
├── frontend/
│   └── KS_ANDROID_APP/
│       ├── build.gradle.kts
│       ├── settings.gradle.kts
│       └── app/
│           └── src/
│               └── main/
│                   ├── AndroidManifest.xml
│                   ├── java/
│                   │   └── com/example/ks_android_app/
│                   │       ├── mainActivity.java
│                   │       ├── browse_stores_activity.java
│                   │       ├── filters_activity.java
│                   │       ├── store_activity.java
│                   │       ├── store_adapter.java
│                   │       ├── product_adapter.java
│                   │       ├── store_model.java
│                   │       ├── product_model.java
│                   │       ├── user.java
│                   │       ├── store.java
│                   │       ├── product.java
│                   │       └── message.java
│                   └── res/
│                       ├── layout/
│                       ├── menu/
│                       ├── drawable/
│                       └── values/
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
│   ├── lib/
│   │   └── gson-2.9.1.jar
│   │
│   └── sample-data/
│       ├── store.json
│       └── product.json
│
├── docs/
│   └── architecture.png
│
└── README.md
```

---

## ▶️ How to Run

### ✅ Prerequisites

Make sure you have installed:

- Java JDK
- Android Studio
- Android SDK
- Android emulator or physical Android device
- Gson JAR file for backend JSON parsing

---

## 🖥️ Running the Backend

### 1. Compile Backend Files

From the backend directory, compile the Java files.

Linux/macOS:

```bash
javac -cp ".:lib/gson-2.9.1.jar" backend/**/*.java
```

Windows PowerShell may require compiling files explicitly or using an IDE.

Example:

```bash
javac -cp ".;lib/gson-2.9.1.jar" backend\model\*.java backend\network\*.java backend\app\*.java
```

---

### 2. Start Reducer

```bash
java network.reducer <reducer_port> <master_port> <num_workers>
```

Example:

```bash
java network.reducer 6000 5000 2
```

---

### 3. Start Workers

```bash
java network.worker <worker_port> <reducer_port>
```

Example:

```bash
java network.worker 7000 6000
java network.worker 7001 6000
```

---

### 4. Start Master

```bash
java network.master <master_port> <worker_ports...>
```

Example:

```bash
java network.master 5000 7000 7001
```

---

### 5. Run CLI Applications

Customer CLI app:

```bash
java app.ClientApp 5000
```

Manager CLI app:

```bash
java app.ManagerApp 5000
```

---

## 📱 Running the Android Frontend

### 1. Open Android Project

Open the Android app folder in Android Studio:

```text
android/KS_ANDROID_APP/
```

---

### 2. Check Server IP

For Android emulator usage, the app should connect to:

```java
private String serverIp = "10.0.2.2";
private int serverPort = 5000;
```

Use the same port as the Master node.

If your backend Master runs on port `4000`, then use:

```java
private int serverPort = 4000;
```

---

### 3. Run the App

In Android Studio:

```text
Run → Run 'app'
```

---

### 4. Test the Full Flow

Recommended test flow:

1. 🧑‍💼 Start Reducer.
2. ⚙️ Start Workers.
3. 🧭 Start Master.
4. 📱 Open Android app.
5. 👤 Enter user information.
6. 🏪 Browse stores.
7. 🔍 Apply filters.
8. 🏬 Open a store.
9. 🛒 Buy products.
10. ⭐ Rate the store.

---

## 📱 Android Emulator Networking

When the backend runs on your computer and the app runs in the Android emulator, use:

```java
10.0.2.2
```

Do not use:

```java
localhost
```

Inside the emulator, `localhost` refers to the emulator itself, not your computer.

If you run the Android app on a physical device, use your computer's local network IP address instead:

```java
private String serverIp = "192.168.1.50";
```

Both the phone and computer must be connected to the same Wi-Fi/network.

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

- 🧠 Distributed systems architecture
- 🧭 Master–Worker–Reducer coordination
- 🧮 Hash-based data partitioning
- ⚙️ Worker-based distributed processing
- 🔄 Reducer-based aggregation
- 🌐 TCP socket communication
- 📦 Java object serialization
- 🧵 Multithreaded request handling
- 📱 Android client integration
- 🍽️ Food-ordering workflow simulation

---

## ⚖️ Design Trade-offs

| Decision | Benefit | Trade-off |
|---|---|---|
| 🌐 Raw sockets | Low-level control and clear networking logic | Harder to scale than REST/gRPC |
| 📦 Object serialization | Easy Java-to-Java object transfer | Requires class compatibility |
| 🧠 In-memory storage | Fast access and simple implementation | Data is lost when servers stop |
| 🧮 Hash partitioning | Simple distribution across workers | Possible uneven load |
| 🧵 Thread-per-request | Easy concurrency model | Not optimal for very high traffic |
| 🔄 Reducer aggregation | Clean distributed search results | Adds extra coordination step |
| 📱 Android Java frontend | Simple mobile UI integration | Requires matching backend protocol |

---

## ⚠️ Known Notes

- The backend should be started before running the Android app.
- The Android app and backend must use compatible serializable classes.
- The Android emulator should use `10.0.2.2` to reach the backend running on the host machine.
- If the backend runs on a different Master port, update the Android client port accordingly.
- If using a physical Android device, replace `10.0.2.2` with the computer's local network IP.
- The project currently uses in-memory backend storage, so data may not persist after restarting nodes.
- Java object serialization requires stable class names, package names, and `serialVersionUID` values.

---

## 🔮 Future Improvements

- 🌐 Replace raw sockets with a REST API using Spring Boot
- 🗄️ Add persistent storage with PostgreSQL or MongoDB
- 🐳 Dockerize backend services
- ⚖️ Add load balancing between workers
- 🔐 Add authentication for customers and managers
- 🧾 Add order history
- 💳 Add payment simulation
- 📦 Add persistent inventory management
- 📊 Add manager dashboard
- 🧪 Add unit and integration tests
- 📱 Improve Android UI/UX
- 🖼️ Load store images from URLs
- 🧭 Add location-based store filtering
- 🛡️ Add fault tolerance for failed workers
- 📈 Add monitoring/logging for backend nodes

---

## 👤 Author

**Spyrou Agamemnon-Ioannis**

---

## 📜 License

This project is intended for educational and academic use and is under the MIT license.


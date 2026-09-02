# HealingKuy 🧘‍♂️

<div align="center">
  <img src="https://media.licdn.com/dms/image/v2/D562DAQFf9MOYA1iJhQ/profile-treasury-image-shrink_1280_1280/B56Zg0uYSdH0AU-/0/1753231231572?e=1788919200&v=beta&t=OwwCmz_uaMcHdgKX00Rg6kMZdpayS-3PRDrE20h6ucc" alt="HealingKuy" width="900"/>
</div>

HealingKuy is an Android-based application designed to help users discover and explore healing destinations and activities. The application provides destination discovery, category browsing, search functionality, bookmarking, detailed destination information, and user profile management through a clean and structured Android architecture.

> **Project Type:** Personal Project
> **Platform:** Android
> **Framework:** Android SDK
> **Language:** Kotlin
> **Architecture:** Clean Architecture + Multi-Module

## ✨ Features

### 🔍 Destination Discovery

* Explore various healing destinations
* Browse destinations based on available categories
* Search for destinations based on user preferences

### 📄 Destination Details

* View detailed destination information
* Display destination images and descriptions
* View additional information related to selected destinations

### 🗂️ Category

* Browse destinations by category
* Filter destinations based on available categories
* Navigate from category results to destination details

### 🔖 Bookmark

* Save favorite destinations
* Access bookmarked destinations
* Manage saved destinations locally

### 👤 Profile

* User registration and login
* Manage user profile
* Access personalized user information

### 📍 Location

* Access location-based functionality
* Utilize device location services to support destination discovery

### 🎨 Modern UI

* Material Design components
* Responsive Android layouts
* ViewBinding for safer view interaction
* Jetpack Navigation Component for screen navigation

## 🏗️ Architecture

```text
HealingKuy
│
├── app/
│   └── Presentation Layer
│
└── core/
    ├── Data Layer
    ├── Domain Layer
    ├── Dependency Injection
    ├── UI
    └── Utilities
```

### Layer Responsibilities

**App Layer**

* Contains application-specific presentation components
* Handles Activities, Fragments, ViewModels, and UI navigation
* Connects the application layer with the reusable core module

**Data Layer**

* Handles local and remote data sources
* Manages API communication using Retrofit
* Manages local persistence using Room
* Provides data through repository implementations

**Domain Layer**

* Contains domain models
* Defines repository contracts
* Contains reusable use cases
* Keeps business logic independent from Android framework implementation

**Dependency Injection**

* Uses Hilt for dependency injection
* Provides dependencies for repositories, data sources, ViewModels, and application components

**UI Layer**

* Contains reusable UI-related components
* Supports common presentation functionality across the application

## 🛠️ Tech Stack

| Category             | Technology                        |
| -------------------- | --------------------------------- |
| Language             | Kotlin                            |
| Platform             | Android                           |
| UI                   | XML, Material Design, ViewBinding |
| Architecture         | Clean Architecture                |
| Project Structure    | Multi-Module                      |
| Dependency Injection | Hilt                              |
| Networking           | Retrofit, Gson, OkHttp            |
| Local Database       | Room                              |
| Local Preferences    | DataStore Preferences             |
| Navigation           | Jetpack Navigation Component      |
| Lifecycle            | ViewModel, LiveData               |
| Image Loading        | Glide                             |
| Location             | Google Play Services Location     |
| Code Generation      | KSP                               |
| Testing              | JUnit, AndroidX Test, Espresso    |

## 🔄 Application Flow

```text
Splash
  ↓
Authentication
  ↓
Home
  ├── Category
  │     ↓
  │   Destination List
  │     ↓
  │   Destination Detail
  │
  ├── Search
  │     ↓
  │   Destination List
  │     ↓
  │   Destination Detail
  │
  ├── Bookmark
  │     ↓
  │   Saved Destinations
  │
  └── Profile
        ↓
      User Profile
```

## 📁 Project Structure

```text
HealingKuy/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/rifftyo/healingkuy/
│           │   ├── di/
│           │   ├── ui/
│           │   │   ├── bookmark/
│           │   │   ├── category/
│           │   │   ├── detail/
│           │   │   ├── home/
│           │   │   ├── login/
│           │   │   ├── profile/
│           │   │   ├── register/
│           │   │   ├── search/
│           │   │   └── splash/
│           │   │
│           │   ├── utils/
│           │   ├── MainActivity.kt
│           │   └── MyApplication.kt
│           │
│           └── res/
│
├── core/
│   └── src/
│       └── main/
│           ├── java/com/rifftyo/core/
│           │   ├── data/
│           │   │   ├── source/
│           │   │   │   ├── local/
│           │   │   │   ├── remote/
│           │   │   │   └── repository/
│           │   │   └── ...
│           │   │
│           │   ├── di/
│           │   ├── domain/
│           │   │   ├── model/
│           │   │   ├── repository/
│           │   │   └── usecase/
│           │   │
│           │   ├── ui/
│           │   └── utils/
│           │
│           └── res/
│
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/
```

The project separates reusable business and data functionality into the `core` module while application-specific presentation components are maintained inside the `app` module. This multi-module structure helps maintain separation of concerns, improves code reusability, and keeps the application easier to maintain and extend.

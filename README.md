# RentPulse — Real Estate & Property Management System 🏢📱

**CAT 2 – Mobile Application Development Project**  
**Bachelor in Business Information Technology (BBIT Year 3.2)**  
*School of Computing and Engineering Sciences*

---

## 📌 Problem Statement
Small landlords and property managers frequently rely on paper records, physical receipts, or fragmented spreadsheets to manage property units, tenant contact details, rent payment tracking, and maintenance requests. This manual approach results in:
- Missing or lost lease and payment records.
- Delayed rent collection tracking and lack of real-time financial visibility.
- Untracked property maintenance issues causing communication breakdowns with tenants.

## 💡 Proposed Solution
**RentPulse** is a native Android application built with Kotlin and Jetpack Compose that provides a centralized, mobile-first management platform for landlords and property managers. RentPulse enables users to:
- Monitor key portfolio KPIs (*Total Monthly Revenue, Pending Rent Balances, Occupancy Rate %, Active Maintenance Tickets*).
- Perform full **CRUD (Create, Read, Update, Delete)** operations on Properties, Tenants, Rent Payments, and Maintenance Requests.
- Maintain persistent local storage via **Room Database**, ensuring data remains saved across application restarts.
- Filter, search, and manage real estate records effortlessly with reactive Jetpack Compose UI.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.2.10
- **UI Framework**: Jetpack Compose with Material 3 Design System
- **Architecture**: Clean Architecture (UI Layer → ViewModel → Repository → Room DB)
- **State Management**: Kotlin Coroutines & `StateFlow`
- **Navigation**: Jetpack Compose Navigation
- **Local Database**: Room Database 2.8.5 with KSP
- **Testing**: JUnit 4

```
Android UI (Compose Screens)
        ↓
RealEstateViewModel (StateFlow & Form Validation)
        ↓
RealEstateRepository
        ↓
AppDatabase (Room Database & DAOs)
```

---

## ✨ Features

### 1. 📊 Interactive Dashboard
- Real-time revenue metrics: Total Collected Rent vs. Pending Rent.
- Portfolio Occupancy progress bar and metrics.
- Active maintenance ticket counters.
- Quick action shortcuts (*Add Property, Add Tenant*).

### 2. 🏠 Property Management (CRUD)
- View list of properties with status badges (*Occupied, Vacant, Maintenance*).
- Filter by property type (*Apartment, Single Family, Commercial, Villa*).
- Add new properties or update property details (address, unit count, monthly rent, description).
- View property detail screen with assigned tenants and maintenance history.

### 3. 👥 Tenant Management (CRUD)
- Track active, pending, and past tenants with unit numbers and rent amounts.
- Direct phone dial and email action buttons for tenant communication.
- Form validation for full name, email format, phone number, and lease terms.

### 4. 💳 Rent Payments Ledger (CRUD)
- Maintain financial transaction log with payment methods (*M-Pesa, Bank Transfer, Cash, Cheque*).
- Track payment statuses (*Paid, Pending, Overdue*) with reference numbers.
- Record new payments or modify existing records.

### 5. 🛠️ Maintenance Ticket Management (CRUD)
- Log property maintenance issues with category tags (*Plumbing, Electrical, HVAC, Appliance, Structural*).
- Assign priority levels (*High, Medium, Low*) and status chips (*Pending, In Progress, Resolved*).
- Delete/resolve tickets upon completion.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer
- JDK 11 or JDK 17
- Android SDK 37 (Minimum SDK 24)

### Installation & Build
1. Clone the repository:
   ```bash
   git clone https://github.com/kevinMGI/mobile-app-cat-2.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies:
   ```bash
   ./gradlew assembleDebug
   ```
4. Run the application on an Android Emulator or physical device (API Level 24+).

---

## 🧪 Testing & Verification
To run the automated unit test suite:
```bash
./gradlew testDebugUnitTest
```

---

## 📄 License & Credits
Developed as part of **CAT 2 – Mobile Application Development** (BBIT Aug–Dec 2026).

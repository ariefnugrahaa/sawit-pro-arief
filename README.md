# Weighbridge Ticket Management System
## Mobile Engineer Interview Assignment Solution

### Project Overview
This project implements a truck weighbridge management application using modern Android development practices and architecture. The application allows users to create, view, edit, and manage weighbridge tickets with a focus on offline-first approach and data persistence.

### Technical Specifications
- **Language:** Kotlin 1.8.10
- **Architecture:** MVVM (Model-View-ViewModel)
- **Build System:** Gradle 8.1.1
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 33 (Android 13)
- **JVM:** JVM 17

### Libraries and Dependencies
- **Android Jetpack Components:**
    - ViewModel: For UI-related data management with lifecycle awareness
    - LiveData: For observable data holders
    - Room: For local database storage
    - Navigation Component: For in-app navigation
    - Compose: For building the UI with a declarative approach
    - Flow: For asynchronous data streams

- **Firebase:**
    - Firebase Realtime Database: For online data synchronization
    - Firebase Authentication: For user authentication
    - Firebase Crashlytics: For crash reporting

- **Coroutines:** For asynchronous operations and background tasks

- **Dependency Injection:**
    - Hilt: For dependency injection implementation

- **Testing:**
    - JUnit4: For unit testing
    - Mockk: For mocking in unit tests
    - Espresso: For UI testing


### Project Structure
The project follows the Clean Architecture principles with a clear separation of concerns:

- **Domain Layer:** Contains business logic, use cases, and domain models
    - `domain/model`: Data models representing core business entities
    - `domain/repository`: Repository interfaces
    - `domain/usecase`: Use cases implementing business logic

- **Data Layer:** Implements repositories, manages data sources
    - `data/repository`: Repository implementations
    - `data/source/local`: Room database and related components
    - `data/source/remote`: Firebase integration and API calls

- **Presentation Layer:** Contains UI-related components
    - `presentation/screen`: Compose screens
    - `presentation/components`: Reusable UI components
    - `presentation/viewmodel`: ViewModels coordinating UI and data

### Key Features Implemented
1. **Ticket Management:**
    - Create new weighbridge tickets
    - Edit existing tickets
    - Validate license numbers using proper format
    - Calculate net weight automatically from inbound/outbound weights
    - Store date and time information

2. **List and Filtering:**
    - Display tickets in a scrollable card-based vertical list
    - Filter tickets by date, driver name, and license number
    - Sort tickets by various criteria (date ascending/descending, driver name, license number)
    - Handle edge cases for empty states and filtering

3. **Offline-First Approach:**
    - Local persistence using Room database
    - Synchronization with Firebase when online
    - Work in offline mode with pending changes queued for later sync

4. **User Interface:**
    - Intuitive and responsive material design UI
    - Form validation with informative error messages
    - Detail view for comprehensive ticket information
    - Navigation between list, detail, and form screens




### Testing Strategy
The project implements a comprehensive testing strategy:

**Unit Tests:**
- Domain layer: All use cases tested with mocked repositories
- Repository implementations: Tested with mocked data sources


### Implementation Notes
- **MVVM Architecture:** The application strictly follows MVVM architecture with unidirectional data flow:
    - ViewModels expose UI state as immutable state flows
    - UI layer observes changes and renders accordingly
    - User actions are processed through ViewModels

- **Coroutines:** Used extensively for asynchronous operations:
    - Network calls handled in IO dispatcher
    - Database operations executed in appropriate dispatchers
    - ViewModelScope used for lifecycle-aware coroutine handling

- **Firebase Integration:**
    - Real-time sync of weighbridge ticket data
    - Conflict resolution strategy for offline-online synchronization
    - Efficient data structure for minimizing bandwidth usage

- **Edge Case Handling:**
    - Network connectivity monitoring and appropriate UI feedback
    - Input validation for license numbers and weight values

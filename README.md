# Planty
*Planty transforms your garden dreams into reality!*

![main](https://github.com/user-attachments/assets/052efaa3-0739-4601-ab3b-6b91f911ec2a)

##### Video:
<a href="https://www.youtube.com/watch?v=7-Ms4Raf1tY">
  <img src="https://img.youtube.com/vi/7-Ms4Raf1tY/0.jpg" alt="Planty video" width="120" height="90" border="10" />
</a>

## Overview
### Big picture
```mermaid
graph TD
    subgraph Presentation
        UI["UI Components"]
        VM["ViewModels"]
        Model["Presentation Models"]
    end
    
    subgraph Domain
        UC["Use Cases"]
        DMod["Domain Models"]
        Repo["Repository Interfaces"]
    end
    
    subgraph Data
        RepoImpl["Repository Implementations"]
        DB["Database (Room)"]
        API["External APIs"]
        EMod["Entity Models"]
    end
    
    UI --> VM
    VM --> UC
    VM --> Model
    Model --> DMod
    UC --> Repo
    UC --> DMod
    Repo --> DMod
    RepoImpl --> Repo
    RepoImpl --> DB
    RepoImpl --> API
    RepoImpl --> EMod
    EMod --> DMod
    
    classDef presentation fill:#f1f8e9, stroke:#2e7d32, stroke-width:2px, color:#000;
    classDef domain fill:#e3f2fd, stroke:#1565c0, stroke-width:2px, color:#000;
    classDef data fill:#ffebee, stroke:#c62828, stroke-width:2px, color:#000;
    
    class UI,VM,Model presentation;
    class UC,DMod,Repo domain;
    class RepoImpl,DB,API,EMod data;
```
#### Key points
1. **Clean Architecture** with clear separation across presentation, domain, and data layers, ensuring a unidirectional dependency flow.

2. **MVVM Pattern** in the presentation layer, using ViewModels to manage UI state and business logic, keeping Composables focused on rendering.

3. **State-Based UI** with immutable state objects flowing from ViewModels to Composables, triggering automatic recomposition when state changes.

4. **Use Case Pattern** isolating specific business operations, making business logic reusable and easily testable.

5. **Repository Pattern** abstracting data sources behind interfaces, allowing the domain layer to remain independent of data implementation details.

6. **Reactive Programming** with Kotlin Flow for asynchronous data streams, state management, and UI updates.

7. **Modular Feature Design** organizing code by features (Garden, Tasks, Plant ID, Catalog) rather than layers, improving maintainability.

8. **Navigation Component** implementing nested navigation graphs for each main feature accessed through bottom navigation.

9. **Dependency Injection** with Hilt throughout the app, facilitating testing and decoupling component creation from usage.

10. **Jetpack Compose UI** using a declarative approach with reusable composables that respond to state changes, creating a responsive and modern UI.

### Component interactions
#### General flow
```mermaid
sequenceDiagram
    participant Composable as Composable UI
    participant State as UI State
    participant VM as ViewModel
    participant UC as UseCase
    participant Repo as Repository
    
    Composable->>VM: User Action (Method Call)
    VM->>UC: Execute Business Logic
    UC->>Repo: Data Operation
    Repo-->>UC: Data Response
    UC-->>VM: Domain Result
    VM->>State: Update State
    State-->>Composable: State Flow Collection
    Composable->>Composable: Recomposition
```
#### Detailed flow for 'Plant Id' feature
```mermaid
sequenceDiagram
    participant User
    participant PIS as PlantIdScreen
    participant PIVM as PlantIdViewModel
    participant IPUC as IdentifyPlantUseCase
    participant CFUC as CreateTempPhotoFileUseCase
    participant PIR as PlantIdRepository
    participant PR as PlantRepository
    participant API as External Plant ID API
    participant DB as Local Plant Database

    User->>PIS: Take photo button click
    
    PIS->>PIVM: createPhotoFile()
    PIVM->>CFUC: invoke(NoParams)
    CFUC-->>PIVM: Uri?
    PIVM->>PIVM: Update state with URI
    PIVM-->>PIS: Updated state with PhotoUri
    
    PIS->>User: Launch camera intent
    User->>PIS: Take photo & confirm
    
    PIS->>PIVM: identifyPlant()
    PIVM->>PIVM: Update state (Loading)
    PIVM-->>PIS: Updated state (Loading)
    PIS->>PIS: Show loading indicator
    
    PIVM->>IPUC: invoke(IdentifyPlantsParams)
    IPUC->>PIR: identifyPlant(image, apiKey)
    PIR->>API: HTTP request
    API-->>PIR: Plant identification response
    PIR-->>IPUC: Plant ID response
    
    IPUC->>PR: searchPlants(scientificName)
    PR->>DB: Query for matching plants
    DB-->>PR: Local plant matches
    PR-->>IPUC: Plant details
    
    IPUC-->>PIVM: Combined results with local plants
    
    PIVM->>PIVM: Update state (Success/Error)
    PIVM-->>PIS: Updated state with results
    
    PIS->>PIS: Display plant results
    PIS->>User: Show identification results

    User->>PIS: Select identified plant
    PIS->>User: Navigate to plant details

    Note over PIS,PIVM: State maintained and observed using StateFlow
```

This application implements a fully interactive **Producer--Consumer simulation** using Java Swing.

It visualizes how multiple worker threads interact with a shared bounded
buffer (`Storage`) under mutual-exclusion constraints, using color-coded
states and real-time UI updates. It is a demo of:

-   Locks and condition variables (`ReentrantLock`, `Condition`)
-   Producer/Consumer patterns
-   Multi-threading and thread lifecycle visualization
-   UI--model synchronization via `PropertyChangeSupport`
-   Real-time table and grid rendering in Swing
-   Correct handling of Swing threading (EDT) via
    `SwingUtilities.invokeLater`

## ✨ Features

### 🔄 Real-Time Simulation

-   Multiple **Producer** and **Consumer** threads\
-   Each worker transitions through:
    -   **BORN**
    -   **RUNNING**
    -   **WAITING** (buffer full/empty)
    -   **IN_EXCLUSIVE_ACCESS** (inside critical section)
    -   **DEAD**

### 🧮 Bounded Storage Buffer

A classic Producer--Consumer implementation with: 
- A fixed-size synchronized buffer
    - `ReentrantLock` for mutual exclusion
- Two condition variables:
    - `fullBuffer` → Producers wait when buffer is full
    - `emptyBuffer` → Consumers wait when buffer is empty

Producers add items to the tail; consumers remove from the head.


## 🖥️ Interactive GUI Components

### `PanelTable`

Displays a table of all workers with: 
- ID
- Type (Producer or Consumer)
- Current state
- Color-coded rows based on their state

### `PanelGrid`

A 2×N grid visualization showing: - Producers on top - Consumers on
bottom - Colors changing according to worker state

### `PanelControl`

Allows configuration of: 
- Number of workers
- Buffer size
- Worker sleep time
- Start/Stop simulation

### `Workplace`

Controller class that: 
- Manages worker lifecycle
- Broadcasts worker state changes with `PropertyChangeSupport`
- Sends UI cleanup signals on reset

### `Storage`

Thread-safe buffer implementing: 
- Locking
- Conditions
- Blocking behavior
- Exclusive access simulation


## 🎛️ Controls

-   **Storage Size** → Capacity of buffer
-   **Number of Workers** → Producers + Consumers
-   **Sleep Time** → Worker processing time

Buttons: 

- ▶️ **Run** --- Starts all workers
- ⏹️ **Stop** --- Stops simulation, resets UI



## 🧵 Thread Coordination & Swing Safety

Worker threads run in parallel, but UI updates happen on the **Swing
Event Dispatch Thread (EDT)** using:

``` java
SwingUtilities.invokeLater(() -> updateModel(...));
```

This prevents: 
- Race conditions
- Out-of-range table updates
- Random UI exceptions

All table modifications (`setRowCount`, `addRow`, `setValueAt`) are
performed on the EDT.


## 🛠 Future Improvements

-   Logging and replay mode\
-   Add graphs for buffer usage\
-   Adjustable proportion of producers vs consumers\
-   Thread priority simulation\
-   Pausing/resuming\


------------------------------------------------------------------------

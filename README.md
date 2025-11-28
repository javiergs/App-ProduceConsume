This project implements a fully interactive **Producer--Consumer simulation** using Java Swing.\
It visualizes how multiple worker threads interact with a shared bounded
buffer (`Storage`) under mutual-exclusion constraints, using color-coded
states and real-time UI updates. 

The application demo of:

-   Locks and condition variables (`ReentrantLock`, `Condition`)
-   Producer/Consumer patterns
-   Multi-threading
-   UI--model synchronization via `PropertyChangeSupport`
-   Real-time table and grid rendering in Swing

## ✨ Features

### 🔄 Real-Time Simulation

-   Multiple **Producer** and **Consumer** threads\
-   Each worker switches between: BORN, RUNNING, WAITING,
    IN_EXCLUSIVE_ACCESS, DEAD

### 🧮 Bounded Storage Buffer

-   A fixed-size buffer managed using:
    -   `ReentrantLock`
    -   `fullBuffer` and `emptyBuffer` conditions

### 🖥️ Interactive GUI

Components include PanelTable, PanelGrid, PanelControl, Storage,
Workplace, and worker classes.

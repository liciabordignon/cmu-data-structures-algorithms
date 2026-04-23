# Data Structures and Algorithms

This repository contains five Java-based projects built for the Data Structures and Algorithms course at Carnegie Mellon University (CMU). The projects focus on implementing fundamental CS algorithms and data structures to parse, secure, and process real-world data efficiently.

## 📂 Projects Overview

### Project 1: Cryptography and Merkle Trees
Implemented various data structures (Singly Linked Lists, Ordered Linked Lists) from scratch, leading up to an application of the Merkle-Hellman Knapsack Cryptosystem. The project culminated in building a **Merkle Tree** from a set of mocked data structures to ensure data integrity and cryptographic verification.

### Project 2: 2D Trees & Spatial Queries (K-D Trees)
Created a spatial data structure known as a **2-D Tree** (a specific case of a K-D Tree) to perform efficient two-dimensional range queries and nearest-neighbor searches. The project utilized Pittsburgh crime data spanning 1990 to visualize geographic patterns using generated KML outputs on Google Earth.

### Project 3: Red-Black Trees
Focused on self-balancing binary search trees by implementing a **Red-Black Tree**. Handled dynamic insertions and deletions while strictly maintaining Red-Black invariants to guarantee $O(\log n)$ worst-case time complexity for operations.

### Project 4: Traveling Salesperson Problem (TSP) & Minimum Spanning Trees (MST)
Modeled real-world crime coordinates as graph nodes to study the **Traveling Salesperson Problem**. 
- **Part 1-2:** Developed and compared algorithms to find optimal solutions (via backtracking) versus approximate solutions.
- **Part 3:** Built an approximate TSP tour using a Minimum Spanning Tree (MST) approach guarantees bounds on the exact solution, visually tracking paths generated via KML files onto maps.

### Project 5: LZW Compression
Developed a lossless data compression algorithm utilizing the **Lempel-Ziv-Welch (LZW)** dictionary-based approach. The project implemented custom Hash Tables along with proper collision resolution to construct the dictionary dynamically during compression and decompression stages.

## 🛠 Tech Stack
- **Language:** Java 
- **Tools:** IntelliJ IDEA, Object-Oriented Design
- **Concepts:** Trees (K-D, Red-Black, Merkle), Graphs (MST, TSP), Linked Lists, Hash Tables, Data Compression.

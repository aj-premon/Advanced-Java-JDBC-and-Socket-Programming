# Java Student Management Systems: Sockets & JDBC

## Overview
This repository contains two distinct Java applications designed to process, manage, and store student information. The projects demonstrate the practical application of networking (Socket Programming), in-memory data structures (Java Collections), and database integration (JDBC). 

There are two primary components in this repository:
1. A Student Info Transfer System (Client-Server Architecture)
2. A Student Result System (Database Persistence)

---

## 1. Scenario: (JDBC)

You are required to develop a "Student Result System" using Java Collections and JDBC. The system should store student data (ID, Name, and Marks), calculate total and grade, then store the data into a database.

**Assignment Task: 1. Coding Task (Provide Screenshot of the Code & Output, Database, Github Link)**

* Use an Array to store student names.
* Use a `HashMap<Integer, Integer[]>` to store student IDs and their marks (3 subjects).
* Add logic to calculate total marks and assign grades.
* Use JDBC to insert student data (ID, Name, Marks, Total, Grade).
* Add a method to display all records from the database.
## 2. Scenario: (Socket Programming)

You are developing a Student Info Transfer System using Java Socket Programming and Collections. The client collects student details (ID, Name, Marks) and sends them to the server, which stores and displays them using Java Collections.

**Assignment Task: 1. Programming Task (Provide Screenshot of the Code & Output, Github Link)**

* **On the Client Side:**
    * Use Scanner to take input for Student ID, Name, and Marks.
    * Send this data using Socket to the server.
* **On the Server Side:**
    * Use HashMap to store Student ID and Marks.
    * Use an Array to store Student Names.
    * Display all received student records on the console.

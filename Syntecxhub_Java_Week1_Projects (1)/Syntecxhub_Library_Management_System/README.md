# Syntecxhub_Library_Management_System

A command-line Library Management System written in Java (Syntecxhub Java Programming internship, Week 1, Project 1).

## Features
- Add a book (title and author; duplicates are rejected)
- Remove a book by ID (with a y/n confirmation)
- Search books by title or author (not case-sensitive)
- View all books
- Books are saved to `books.txt`, so they are still there next time you run the program
- Input validation and error handling: empty text, non-numeric IDs, unknown IDs, damaged data file, file write errors

## Classes
- `Book` - one book (ID, title, author)
- `Library` - keeps the list of books, saves and loads the file
- `LibraryApp` - the menu and user input

## Requirements
Java 11 or newer (JDK).

## Compile and run
```
javac -d out src/*.java
java -cp out LibraryApp
```
To use a different data file: `java -cp out LibraryApp mybooks.txt`

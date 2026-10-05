# Syntecxhub_Hotel_Booking_System

A command-line Hotel Booking Management System written in Java (Syntecxhub Java Programming internship, Week 1, Project 2).

## Features
- View available rooms
- Book a room (guest name and number of nights, total price shown)
- Cancel a booking (with a y/n confirmation)
- View booked rooms
- Rooms are stored in an array (10 rooms: Single, Double, Deluxe, Suite)
- Edge cases handled: invalid or non-numeric room number, room that does not exist, room already booked, cancelling a room that is not booked, empty guest name, nights outside 1-30, no rooms available

Bookings are kept in memory only, so they reset when the program exits.

## Classes
- `Room` - one room and its booking
- `Hotel` - the array of rooms and search methods
- `HotelApp` - the menu and user input

## Requirements
Java 11 or newer (JDK).

## Compile and run
```
javac -d out src/*.java
java -cp out HotelApp
```
Room prices are set in `Hotel.java`. The currency symbol (`$`) is `CURRENCY` in `HotelApp.java`.

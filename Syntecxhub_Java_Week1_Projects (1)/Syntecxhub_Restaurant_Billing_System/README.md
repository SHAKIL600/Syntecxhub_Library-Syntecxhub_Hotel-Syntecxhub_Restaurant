# Syntecxhub_Restaurant_Billing_System

A console-based Restaurant Billing System written in Java (Syntecxhub Java Programming internship, Week 1, Project 3).

## Features
- View the menu (starts with 6 sample items)
- Add and remove menu items while the program is running
- Create a bill: enter item IDs and quantities, repeat items to add more
- Itemized receipt with subtotal, GST (5%) and total
- Money is calculated with `BigDecimal` and rounded half-up to 2 decimals
- Input validation: invalid IDs and quantities, duplicate item names, prices with more than 2 decimals, empty menu, empty order

## Classes
- `MenuItem` - one menu item
- `Menu` - the list of items (add, remove, find)
- `OrderLine` - one line on a bill
- `Bill` - items, GST calculation and receipt text
- `RestaurantApp` - the menu and user input

## Requirements
Java 11 or newer (JDK).

## Compile and run
```
javac -d out src/*.java
java -cp out RestaurantApp
```
To change the tax rate, edit `TAX_RATE` in `Bill.java`. To change the currency symbol, edit `CURRENCY` in `Bill.java`.

package org.zr.shudian.service;

import org.zr.shudian.model.Book;
import org.zr.shudian.model.Order;
import org.zr.shudian.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final Map<String, Book> books = new ConcurrentHashMap<>();
    private static final Map<String, Order> orders = new ConcurrentHashMap<>();

    static {
        // Initialize admin account
        User admin = new User();
        admin.setId("1");
        admin.setName("Admin");
        admin.setGender("Other");
        admin.setAge(30);
        admin.setIdentity("Admin");
        admin.setIdNumber("admin");
        admin.setPhone("13800000000");
        admin.setEmail("admin@bookstore.com");
        admin.setPassword("admin");
        users.put(admin.getIdNumber(), admin);

        // Initialize sample books
        Book[] sampleBooks = {
                new Book("1", "The Great Gatsby", 29.99, 100),
                new Book("2", "To Kill a Mockingbird", 24.99, 150),
                new Book("3", "1984", 17.99, 200),
                new Book("4", "Pride and Prejudice", 15.99, 120),
                new Book("5", "The Catcher in the Rye", 21.99, 80),
                new Book("6", "Brave New World", 17.99, 95),
                new Book("7", "Moby Dick", 23.99, 70),
                new Book("8", "War and Peace", 35.99, 60),
                new Book("9", "Les Misérables", 32.99, 55),
                new Book("10", "The Lord of the Rings", 27.99, 110),
                new Book("11", "Harry Potter and the Sorcerer's Stone", 16.99, 250),
                new Book("12", "The Hobbit", 14.99, 130),
                new Book("13", "Fahrenheit 451", 12.99, 180),
                new Book("14", "Animal Farm", 9.99, 220),
                new Book("15", "Jane Eyre", 18.99, 90)
        };

        for (Book book : sampleBooks) {
            books.put(book.getId(), book);
        }
    }

    // User operations
    public static User getUser(String idNumber) {
        return users.get(idNumber);
    }

    public static Collection<User> getAllUsers() {
        return users.values();
    }

    public static void addUser(User user) {
        users.put(user.getIdNumber(), user);
    }

    public static void updateUser(User user) {
        users.put(user.getIdNumber(), user);
    }

    public static void deleteUser(String idNumber) {
        users.remove(idNumber);
    }

    // Book operations
    public static Book getBook(String id) {
        return books.get(id);
    }

    public static Collection<Book> getAllBooks() {
        return books.values();
    }

    public static void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public static void updateBook(Book book) {
        books.put(book.getId(), book);
    }

    public static void deleteBook(String id) {
        books.remove(id);
    }

    // Order operations
    public static Order getOrder(String id) {
        return orders.get(id);
    }

    public static Collection<Order> getAllOrders() {
        return orders.values();
    }

    public static void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public static void updateOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public static void deleteOrder(String id) {
        orders.remove(id);
    }
} 
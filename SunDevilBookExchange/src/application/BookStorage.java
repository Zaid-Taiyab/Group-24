package application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {
    private List<Book> books;
    private File file;

    public BookStorage() {
        this.books = new ArrayList<>();
        this.file = new File("books.txt");
        loadBooksFromFile();  // Load books when the class is instantiated
    }

    // Method to load books from the file
    private void loadBooksFromFile() {
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                while (line != null && !line.trim().isEmpty()) {
                    Book book = Book.fromTextLine(line);
                    books.add(book);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to save a book to the file
    public void saveBookToFile(Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // If file is not empty, add a new line
            if (file.length() > 0) {
                writer.newLine();
            }
            writer.write(book.toTextLine());  // Convert the book to a text line and write it
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get the list of books
    public List<Book> getBooks() {
        return books;
    }

    // Method to find a book by its ID
    public Book getBookById(String bookId) {
        for (Book book : books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    // Method to remove a book from the list and update the file
    public void removeBook(String bookId) {
        Book bookToRemove = getBookById(bookId);
        if (bookToRemove != null) {
            books.remove(bookToRemove);
            updateFile();  // Rewrite the file without the removed book
        }
    }

    // Method to update the file by rewriting all books
    private void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Book book : books) {
                writer.write(book.toTextLine());
                writer.newLine();  // Write each book on a new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

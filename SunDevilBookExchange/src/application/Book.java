package application;

public class Book {
    private String id;
    private String name;
    private String author;
    private String category;
    private String condition;
    private double originalPrice;
    private double salePrice;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    // Method to update book details
    public void updateBook(String id, String name, String author, String category, String condition, double originalPrice, double salePrice) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.condition = condition;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
    }

    // Method to convert a book's details to a single line of text (for saving in a file)
    public String toTextLine() {
        return id + "," + name + "," + author + "," + category + "," + condition + "," + originalPrice + "," + salePrice;
    }

    // Static method to create a Book object from a line of text (for loading from a file)
    public static Book fromTextLine(String textLine) {
        String[] parts = textLine.split(",");
        Book book = new Book();
        book.setId(parts[0]);
        book.setName(parts[1]);
        book.setAuthor(parts[2]);
        book.setCategory(parts[3]);
        book.setCondition(parts[4]);
        book.setOriginalPrice(Double.parseDouble(parts[5]));
        book.setSalePrice(Double.parseDouble(parts[6]));
        return book;
    }
}

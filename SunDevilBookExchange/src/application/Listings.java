package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class Listings {

    private final BookStorage bookStorage;

    public Listings() {
        this.bookStorage = new BookStorage(); // init book storage
    }

    public void start(Stage stage) {
        // Create header label
        Label headerLabel = new Label("My Listings");
        headerLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.web("#F5DEB3"));

        // Make back button
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));
        backButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        backButton.setOnAction(e -> navigateBack(stage));

        // Add hover effect to the back button
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: #e6cfa5; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));

        // Arrange header components in HBox
        HBox headerBox = new HBox(10, backButton, headerLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setStyle("-fx-background-color: #801f33;");
        headerBox.setPadding(new Insets(10));

        // Gets books from storage
        List<Book> bookListings = bookStorage.getBooks();

        // Container for book listings
        VBox listingsContainer = new VBox(20);
        listingsContainer.setPadding(new Insets(20));
        listingsContainer.setStyle("-fx-background-color: #801f33;");

        // Add each book as a card to the listings container
        for (Book book : bookListings) {
            VBox bookCard = createBookCard(book, listingsContainer);
            listingsContainer.getChildren().add(bookCard);
        }

        // Make the listings scrollable
        ScrollPane scrollPane = new ScrollPane(listingsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #801f33; -fx-background: #801f33; " +
                "-fx-control-inner-background: #801f33;");
        scrollPane.setPadding(new Insets(10));

        // Set up the main layout using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(scrollPane);
        mainLayout.setStyle("-fx-background-color: #801f33;");

        // Create and set the scene
        Scene scene = new Scene(mainLayout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Listings");
        stage.show();
    }

    private void navigateBack(Stage stage) {
        SellerPage sellerPage = new SellerPage();
        sellerPage.start(stage);
    }

    private VBox createBookCard(Book book, VBox listingsContainer) {
        // GridPane for book details alignment
        GridPane bookInfoGrid = new GridPane();
        bookInfoGrid.setHgap(10);
        bookInfoGrid.setVgap(5);
        bookInfoGrid.setPadding(new Insets(10));

        // Labels with book details
        Label titleLabel = new Label("Title:");
        Label authorLabel = new Label("Author:");
        Label categoryLabel = new Label("Category:");
        Label conditionLabel = new Label("Condition:");
        Label priceLabel = new Label("Generated Price:");
        Label statusLabel = new Label("Status:");

        // Values for book details
        Label titleValue = new Label(book.getName());
        Label authorValue = new Label(book.getAuthor());
        Label categoryValue = new Label(book.getCategory());
        Label conditionValue = new Label(book.getCondition());
        Label priceValue = new Label(String.format("$%.2f", book.getSalePrice()));
        Label statusValue = new Label("Available");

        // Set font for labels and values
        Font labelFont = Font.font("Times New Roman", FontWeight.BOLD, 16);
        Font valueFont = Font.font("Times New Roman", FontWeight.NORMAL, 16);

        titleLabel.setFont(labelFont);
        authorLabel.setFont(labelFont);
        categoryLabel.setFont(labelFont);
        conditionLabel.setFont(labelFont);
        priceLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);

        titleValue.setFont(valueFont);
        authorValue.setFont(valueFont);
        categoryValue.setFont(valueFont);
        conditionValue.setFont(valueFont);
        priceValue.setFont(valueFont);
        statusValue.setFont(valueFont);

        // Set text color to black
        titleLabel.setStyle("-fx-text-fill: black");
        authorLabel.setStyle("-fx-text-fill: black");
        categoryLabel.setStyle("-fx-text-fill: black");
        conditionLabel.setStyle("-fx-text-fill: black");
        priceLabel.setStyle("-fx-text-fill: black");
        statusLabel.setStyle("-fx-text-fill: black");

        titleValue.setStyle("-fx-text-fill: black");
        authorValue.setStyle("-fx-text-fill: black");
        categoryValue.setStyle("-fx-text-fill: black");
        conditionValue.setStyle("-fx-text-fill: black");
        priceValue.setStyle("-fx-text-fill: black");
        statusValue.setStyle("-fx-text-fill: black");

        // Add labels and values to GridPane
        bookInfoGrid.add(titleLabel, 0, 0);
        bookInfoGrid.add(titleValue, 1, 0);
        bookInfoGrid.add(authorLabel, 0, 1);
        bookInfoGrid.add(authorValue, 1, 1);
        bookInfoGrid.add(categoryLabel, 0, 2);
        bookInfoGrid.add(categoryValue, 1, 2);
        bookInfoGrid.add(conditionLabel, 0, 3);
        bookInfoGrid.add(conditionValue, 1, 3);
        bookInfoGrid.add(priceLabel, 0, 4);
        bookInfoGrid.add(priceValue, 1, 4);
        bookInfoGrid.add(statusLabel, 0, 5);
        bookInfoGrid.add(statusValue, 1, 5);

        // Create card container
        VBox bookCard = new VBox(10, bookInfoGrid);
        bookCard.setStyle("-fx-background-color: #F5DEB3; " +
                "-fx-border-color: black; " +
                "-fx-border-radius: 10px; " +
                "-fx-background-radius: 10px;");
        bookCard.setPadding(new Insets(10));

        // Add remove button if the book is not sold
        if (!"Sold".equalsIgnoreCase(book.getCondition())) {
            Button removeButton = new Button("Remove Listing");
            removeButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
            removeButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;");

            // Hover effect for remove button
            removeButton.setOnMouseEntered(e -> removeButton.setStyle("-fx-background-color: #e6cfa5; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
            removeButton.setOnMouseExited(e -> removeButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));

            // Handle remove action
            removeButton.setOnAction(e -> removeBookListing(book, bookCard, listingsContainer));

            bookCard.getChildren().add(removeButton);
        }

        return bookCard;
    }

    private void removeBookListing(Book book, VBox bookCard, VBox listingsContainer) {
        listingsContainer.getChildren().remove(bookCard);
        bookStorage.removeBook(book.getId());
    }
}

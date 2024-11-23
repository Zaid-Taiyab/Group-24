package application;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.UUID;

public class SellerPage {

    public void start(Stage stage) {
        // Header
        Label header = new Label("List a Book");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.WHITE);
        header.setStyle("-fx-padding: 10px;");

        StackPane headerPane = new StackPane(header);
        headerPane.setStyle("-fx-background-color: #801f33;");

        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #801f33; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(200);
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        logoutButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        logoutButton.setOnAction(e -> goToLoginPage(stage));

        // Listings Button
        Button viewListingsButton = new Button("View Listings");
        viewListingsButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        viewListingsButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        viewListingsButton.setOnAction(e -> {
            Listings listingsPage = new Listings();
            listingsPage.start(stage); 
        });
        StackPane.setAlignment(viewListingsButton, Pos.TOP_LEFT);
        StackPane.setMargin(viewListingsButton, new Insets(10, 0, 0, 10)); 
        headerPane.getChildren().add(viewListingsButton);

        // Book Fields
        TextField bookTitle = new TextField();
        TextField author = new TextField();
        String[] categories = Category.getAllValuesAsStrings();
        final ComboBox<String> categoryList = new ComboBox<>(FXCollections.observableArrayList(categories));
        String[] conditions = Condition.getAllValuesAsStrings();
        final ComboBox<String> conditionList = new ComboBox<>(FXCollections.observableArrayList(conditions));
        TextField originalPrice = new TextField();
        TextField sellPrice = new TextField();
        sellPrice.setDisable(true);
        Button generatePrice = new Button("Generate Price");
        Button listBook = new Button("List My Book");

        // Add hover effect to the logout button and listings button
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #e6cfa5; -fx-text-fill: black; -fx-border-radius: 10px; -fx-background-radius: 10px;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 10px; -fx-background-radius: 10px;"));

        viewListingsButton.setOnMouseEntered(e -> viewListingsButton.setStyle("-fx-background-color: #e6cfa5; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));
        viewListingsButton.setOnMouseExited(e -> viewListingsButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black; -fx-border-radius: 8px; -fx-background-radius: 8px;"));

        // Add a spacer before the logout button to keep it at the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS); // Allows the spacer to take up remaining space
        sidebar.getChildren().add(spacer);
        sidebar.getChildren().add(logoutButton);

        // Form Layout using GridPane for alignment
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(15));
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setStyle("-fx-background-color: #F5DEB3;");
        formGrid.setAlignment(Pos.CENTER);

        // Labels with consistent width
        Label titleLabel = new Label("Book Title:");
        Label authorLabel = new Label("Author:");
        Label categoryLabel = new Label("Category:");
        Label conditionLabel = new Label("Condition:");
        Label originalPriceLabel = new Label("Original Price:");
        Label sellPriceLabel = new Label("Sale Price:");

        // Set labels to have the same alignment and size
        titleLabel.setMinWidth(100);
        authorLabel.setMinWidth(100);
        categoryLabel.setMinWidth(100);
        conditionLabel.setMinWidth(100);
        originalPriceLabel.setMinWidth(100);
        sellPriceLabel.setMinWidth(100);
        titleLabel.setAlignment(Pos.CENTER_RIGHT);
        authorLabel.setAlignment(Pos.CENTER_RIGHT);
        categoryLabel.setAlignment(Pos.CENTER_RIGHT);
        conditionLabel.setAlignment(Pos.CENTER_RIGHT);
        originalPriceLabel.setAlignment(Pos.CENTER_RIGHT);
        sellPriceLabel.setAlignment(Pos.CENTER_RIGHT);

        // Add elements to the GridPane
        formGrid.add(titleLabel, 0, 0);
        formGrid.add(bookTitle, 1, 0);
        formGrid.add(authorLabel, 0, 1);
        formGrid.add(author, 1, 1);
        formGrid.add(categoryLabel, 0, 2);
        formGrid.add(categoryList, 1, 2);
        formGrid.add(conditionLabel, 0, 3);
        formGrid.add(conditionList, 1, 3);
        formGrid.add(originalPriceLabel, 0, 4);
        formGrid.add(originalPrice, 1, 4);
        formGrid.add(sellPriceLabel, 0, 5);
        formGrid.add(sellPrice, 1, 5);

        // Buttons HBox
        HBox buttonBox = new HBox(10, generatePrice, listBook);
        buttonBox.setAlignment(Pos.CENTER);
        formGrid.add(buttonBox, 0, 6, 2, 1); // Span across two columns

        // Main Content VBox
        VBox mainContent = new VBox(formGrid);
        mainContent.setPadding(new Insets(15));
        mainContent.setStyle("-fx-background-color: #F5DEB3;");

        // Layout
        BorderPane layout = new BorderPane();
        layout.setTop(headerPane);
        layout.setLeft(sidebar);
        layout.setCenter(mainContent);

        // Scene
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Seller Page");
        stage.show();

        // Enable/Disable Buttons Based on Field Input
        enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook);

        // Listeners for input fields to enable/disable buttons dynamically
        bookTitle.textProperty().addListener((observable, oldValue, newValue) -> enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook));
        author.textProperty().addListener((observable, oldValue, newValue) -> enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook));
        categoryList.valueProperty().addListener((observable, oldValue, newValue) -> enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook));
        conditionList.valueProperty().addListener((observable, oldValue, newValue) -> enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook));
        originalPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            validateOriginalPrice(originalPrice);
            enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook);
        });
        sellPrice.textProperty().addListener((observable, oldValue, newValue) -> enableDisableButtons(bookTitle, author, categoryList, conditionList, originalPrice, sellPrice, generatePrice, listBook));

        // When Generate Price Button is clicked
        generatePrice.setOnAction(e -> {
            if (isValidDouble(originalPrice.getText())) {
                double originalPriceValue = Double.parseDouble(originalPrice.getText());
                int categoryIndex = categoryList.getSelectionModel().getSelectedIndex();
                int conditionIndex = conditionList.getSelectionModel().getSelectedIndex();

                // Create PriceGenerator object
                PriceGenerator priceGenerator = new PriceGenerator();
                // Generate the price
                priceGenerator.generatePrice(categoryIndex, conditionIndex, originalPriceValue);

                // Set the generated price in the sellPrice field
                sellPrice.setText(String.format("%.2f", priceGenerator.displayPrice()));
            }
        });

        // When List My Book Button is clicked
        listBook.setOnAction(e -> {
            if (isValidDouble(originalPrice.getText()) && isValidDouble(sellPrice.getText())) {
                String title = bookTitle.getText();
                String bookAuthor = author.getText();
                String category = categoryList.getSelectionModel().getSelectedItem().toString();
                String condition = conditionList.getSelectionModel().getSelectedItem().toString();
                double originalPriceValue = Double.parseDouble(originalPrice.getText());
                double salePriceValue = Double.parseDouble(sellPrice.getText());

                // Create new Book object
                Book book = new Book();
                book.updateBook(UUID.randomUUID().toString(), title, bookAuthor, category, condition, originalPriceValue, salePriceValue);

                // Save Book to file
                saveBookToFile(book);

                // Clear the inputs
                bookTitle.clear();
                author.clear();
                categoryList.getSelectionModel().clearSelection();
                conditionList.getSelectionModel().clearSelection();
                originalPrice.clear();
                sellPrice.clear();
            }
        });
    }

    private void enableDisableButtons(TextField bookTitle, TextField author, ComboBox<String> categoryList, ComboBox<String> conditionList, TextField originalPrice, TextField sellPrice, Button generatePrice, Button listBook) {
        // Enable/Disable Generate Price Button
        boolean isGeneratePriceEnabled = !bookTitle.getText().isEmpty() && !author.getText().isEmpty() && categoryList.getValue() != null && conditionList.getValue() != null && !originalPrice.getText().isEmpty() && isValidDouble(originalPrice.getText());
        generatePrice.setDisable(!isGeneratePriceEnabled);

        // Enable/Disable List My Book Button
        boolean isListBookEnabled = !bookTitle.getText().isEmpty() && !author.getText().isEmpty() && categoryList.getValue() != null && conditionList.getValue() != null && !originalPrice.getText().isEmpty() && !sellPrice.getText().isEmpty() && isValidDouble(originalPrice.getText());
        listBook.setDisable(!isListBookEnabled);
    }

    private boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void validateOriginalPrice(TextField originalPrice) {
        String text = originalPrice.getText();

        // If the text is empty, we do not need to do anything
        if (text.isEmpty()) {
            return;
        }

        try {
            // Attempt to parse the text as a double
            Double.parseDouble(text);
        } catch (NumberFormatException e) {
            // If parsing fails, remove the last character (invalid input)
            originalPrice.setText(text.substring(0, text.length() - 1));
        }
    }

    private void goToLoginPage(Stage stage) {
        Main main = new Main();
        main.start(stage);  // Calls the login page
    }

    private void saveBookToFile(Book book) {
        BookStorage bookStorage = new BookStorage();
        bookStorage.saveBookToFile(book);
    }
}

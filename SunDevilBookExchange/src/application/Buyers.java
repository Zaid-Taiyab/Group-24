// Integration of Buyers Page with the rest of the application

package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.stream.Collectors;

public class Buyers extends Application {

    private ObservableList<Book> cartItems = FXCollections.observableArrayList();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private Label cartContentsLabel;
    private final BookStorage bookStorage = new BookStorage(); // Shared data source

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sun Devil Book Exchange - Buyer's Page");

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #801f33;");

        // Page title
        Label pageTitle = new Label("Welcome to the Sun Devil Book Exchange");
        pageTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        HBox titleBox = new HBox(pageTitle);
        titleBox.setAlignment(Pos.CENTER);
        mainLayout.setTop(titleBox);

        // Filter section
        VBox filterBox = createFilterSection();
        mainLayout.setLeft(filterBox);

        // Book details section
        VBox bookDetailsBox = createBookDetailsSection();
        mainLayout.setCenter(bookDetailsBox);

        // Cart section
        VBox cartBox = createCartSection();
        mainLayout.setRight(cartBox);

        // Back button to return to login or role selection
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigateToLoginPage(primaryStage));
        backButton.setStyle("-fx-background-color: #F5DEB3; -fx-font-weight: bold; -fx-border-radius: 10px;");

        VBox bottomSection = new VBox(backButton);
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(10));
        mainLayout.setBottom(bottomSection);

        // Set up the scene
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Populate sample books (or load from storage)
        populateBooksFromStorage();
    }

    private VBox createFilterSection() {
        VBox filterBox = new VBox(20);
        filterBox.setPadding(new Insets(20));
        filterBox.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #ffffff; -fx-border-width: 2px;");

        Label filterTitle = new Label("Filters");
        filterTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("All Categories", "Natural Science", "Computer Science", "Math", "English Language", "Other");
        categoryComboBox.setValue("All Categories");
        categoryComboBox.setStyle("-fx-background-color: #f5f5dc;");

        ComboBox<String> conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll("All Conditions", "New", "Used Like New", "Moderately Used", "Heavily Used");
        conditionComboBox.setValue("All Conditions");
        conditionComboBox.setStyle("-fx-background-color: #f5f5dc;");

        categoryComboBox.setOnAction(event -> filterBooks(categoryComboBox.getValue(), conditionComboBox.getValue()));
        conditionComboBox.setOnAction(event -> filterBooks(categoryComboBox.getValue(), conditionComboBox.getValue()));

        filterBox.getChildren().addAll(filterTitle, new Label("Browse by Category:"), categoryComboBox, new Label("Filter by Condition:"), conditionComboBox);
        return filterBox;
    }

    private VBox createBookDetailsSection() {
        VBox bookDetailsBox = new VBox(10);
        bookDetailsBox.setPadding(new Insets(20));
        bookDetailsBox.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #ffffff; -fx-border-width: 2px;");

        Label bookListTitle = new Label("Available Books");
        bookListTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");

        TableView<Book> bookTable = new TableView<>(bookList);
        bookTable.setStyle("-fx-selection-bar: #801f33;");
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAuthor()));

        TableColumn<Book, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCondition()));

        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSalePrice()).asObject());

        TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<Book, Void>() {
            private final Button addButton = new Button("Add to Cart");

            {
                addButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    cartItems.add(book);
                    updateCartLabel();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : addButton);
            }
        });

        bookTable.getColumns().addAll(titleCol, authorCol, conditionCol, priceCol, actionCol);
        bookDetailsBox.getChildren().addAll(bookListTitle, bookTable);
        return bookDetailsBox;
    }

    private VBox createCartSection() {
        VBox cartBox = new VBox(20);
        cartBox.setPadding(new Insets(20));
        cartBox.setStyle("-fx-background-color: #F5DEB3;");

        Label cartTitle = new Label("Shopping Cart");
        cartTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");

        cartContentsLabel = new Label("Cart is empty.");
        cartContentsLabel.setStyle("-fx-font-size: 16px;");

        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(event -> viewCart());

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(event -> checkout());

        cartBox.getChildren().addAll(cartTitle, cartContentsLabel, viewCartButton, checkoutButton);
        return cartBox;
    }

    private void filterBooks(String category, String condition) {
        bookList.setAll(bookStorage.getBooks().stream()
                .filter(book -> ("All Categories".equals(category) || book.getCategory().equals(category)) &&
                               ("All Conditions".equals(condition) || book.getCondition().equals(condition)))
                .collect(Collectors.toList())
);
    }

    private void populateBooksFromStorage() {
        bookList.setAll(bookStorage.getBooks());
    }

    private void updateCartLabel() {
        cartContentsLabel.setText(cartItems.isEmpty() ? "Cart is empty." : "Items in Cart: " + cartItems.size());
    }

    private void viewCart() {
        Stage cartStage = new Stage();
        cartStage.setTitle("Your Cart");

        VBox cartLayout = new VBox(10);
        cartLayout.setPadding(new Insets(20));
        cartLayout.setStyle("-fx-background-color: #F5DEB3;");

        Label cartTitle = new Label("Shopping Cart");
        cartTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");

        TableView<Book> cartTable = new TableView<>(cartItems);
        cartTable.setStyle("-fx-selection-bar: #801f33;");

        // Create table columns
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));

        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        TableColumn<Book, Void> removeCol = new TableColumn<>("Action");
        removeCol.setCellFactory(param -> new TableCell<Book, Void>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    cartItems.remove(book);
                    updateCartLabel(); // Update the cart label
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        cartTable.getColumns().addAll(titleCol, authorCol, conditionCol, priceCol, removeCol);

        cartLayout.getChildren().addAll(cartTitle, cartTable);

        Scene scene = new Scene(cartLayout, 600, 400);
        cartStage.setScene(scene);
        cartStage.show();
    }


    private void checkout() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Checkout");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty. Please add items to proceed.");
            alert.showAndWait();
        } else {
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your purchase! Your items have been successfully checked out.");
            alert.showAndWait();

            // Remove checked-out books from listings
            for (Book book : cartItems) {
                bookList.remove(book); // Remove from the UI list
                bookStorage.removeBook(book.getId()); // Remove from storage
            }

            // Clear the cart
            cartItems.clear();
            updateCartLabel(); // Update the cart label
        }
    }


    private void navigateToLoginPage(Stage stage) {
        Main mainPage = new Main();
        mainPage.start(stage); // Navigate back to the main login page
    }

    public static void main(String[] args) {
        launch(args);
    }
}



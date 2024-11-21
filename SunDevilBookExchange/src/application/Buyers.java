package application;

import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;

public class Buyers extends Application {
    private ObservableList<Book> cartItems = FXCollections.observableArrayList();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();
    private Label cartContentsLabel;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SunDevil Book Buying System - Buyer's Page");
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #801f33;");
        Label pageTitle = new Label("Welcome to the SunDevil Book Exchange");
        pageTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        HBox titleBox = new HBox(pageTitle);
        titleBox.setPadding(new Insets(10, 0, 20, 0));
        titleBox.setAlignment(Pos.CENTER);
        VBox filterBox = createFilterSection();
        VBox bookDetailsBox = createBookDetailsSection();
        VBox cartBox = createCartSection();
        mainLayout.setTop(titleBox);
        mainLayout.setLeft(filterBox);
        mainLayout.setCenter(bookDetailsBox);
        mainLayout.setRight(cartBox);
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        populateSampleBooks();
    }

    private VBox createFilterSection() {
        VBox filterBox = new VBox(20);
        filterBox.setPadding(new Insets(20));
        filterBox.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #ffffff; -fx-border-width: 2px;"
                + "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        Label filterTitle = new Label("Filters");
        filterTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");
        Label categoryLabel = new Label("Browse by Category:");
        categoryLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #801f33;");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("All Categories", "Natural Science", "Computer Science", "Math",
                "English Language", "Other");
        categoryComboBox.setValue("All Categories");
        categoryComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #f5f5dc; -fx-border-color: #801f33;"
                + "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        Label conditionLabel = new Label("Filter by Condition:");
        conditionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #801f33;");
        ComboBox<String> conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll("All Conditions", "New", "Used Like New", "Moderately Used", "Heavily Used");
        conditionComboBox.setValue("All Conditions");
        conditionComboBox.setStyle("-fx-font-size: 14px; -fx-background-color: #f5f5dc; -fx-border-color: #801f33;"
                + "-fx-border-radius: 5px; -fx-background-radius: 5px;");
        categoryComboBox.setOnAction(event -> filterBooks(categoryComboBox.getValue(), conditionComboBox.getValue()));
        conditionComboBox.setOnAction(event -> filterBooks(categoryComboBox.getValue(), conditionComboBox.getValue()));
        filterBox.getChildren().addAll(filterTitle, categoryLabel, categoryComboBox, conditionLabel, conditionComboBox);
        return filterBox;
    }

    private VBox createBookDetailsSection() {
        VBox bookDetailsBox = new VBox(10);
        bookDetailsBox.setPadding(new Insets(20));
        bookDetailsBox.setPrefWidth(500);
        bookDetailsBox.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #ffffff; -fx-border-width: 2px;"
                + "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        Label bookListTitle = new Label("Available Books");
        bookListTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");
        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefWidth(500);
        bookTable.setStyle("-fx-selection-bar: #801f33; -fx-focus-color: transparent;");
        TableColumn<Book, ImageView> imageCol = new TableColumn<>("Cover");
        imageCol.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        imageCol.setPrefWidth(80);
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);
        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(100);
        TableColumn<Book, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(new PropertyValueFactory<>("condition"));
        conditionCol.setPrefWidth(100);
        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(90);
        TableColumn<Book, Void> addToCartCol = new TableColumn<>("Action");
        addToCartCol.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("Add");
            {
                addButton.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff;");
                addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #a8324c; -fx-text-fill: #ffffff;"));
                addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff;"));
                addButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    cartItems.add(book);
                    animateAddToCart(addButton);
                    updateCartLabel();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : addButton);
            }
        });
        bookTable.getColumns().addAll(imageCol, titleCol, authorCol, conditionCol, priceCol, addToCartCol);
        bookTable.setItems(bookList);
        bookDetailsBox.getChildren().addAll(bookListTitle, bookTable);
        return bookDetailsBox;
    }
  
    private VBox createCartSection() {
        VBox cartBox = new VBox(20);
        cartBox.setPadding(new Insets(20));
        cartBox.setPrefWidth(200);
        cartBox.setStyle("-fx-background-color: #F5DEB3; -fx-border-color: #ffffff; -fx-border-width: 2px;"
                + "-fx-border-radius: 10px; -fx-background-radius: 10px;");
        Label cartTitle = new Label("Shopping Cart");
        cartTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #801f33;");
        cartContentsLabel = new Label("Cart is empty.");
        cartContentsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #801f33;");
        Button viewCartButton = new Button("View Cart");
        Button checkoutButton = new Button("Checkout");
        Button clearCartButton = new Button("Clear Cart");
        styleCartButton(viewCartButton);
        styleCartButton(checkoutButton);
        styleCartButton(clearCartButton)
        viewCartButton.setOnAction(event -> viewCart());
        checkoutButton.setOnAction(event -> checkout());
        clearCartButton.setOnAction(event -> clearCart());
        cartBox.getChildren().addAll(cartTitle, cartContentsLabel, viewCartButton, checkoutButton, clearCartButton);
        return cartBox;
    }

    private void populateSampleBooks() {
        Image placeholderImage = new Image("https://via.placeholder.com/60");
        bookList.addAll(
                new Book("Book A", "Author A", "New", 9.99, "Natural Science", placeholderImage),
                new Book("Book B", "Author B", "Moderately Used", 8.99, "Computer Science", placeholderImage),
                new Book("Book C", "Author C", "Heavily Used", 5.99, "Math", placeholderImage),
                new Book("Book D", "Author D", "New", 11.99, "English Language", placeholderImage)
        );
    }

    private void filterBooks(String category, String condition) {
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
        for (Book book : bookList) {
            boolean matchesCategory = category.equals("All Categories") || book.getCategory().equals(category);
            boolean matchesCondition = condition.equals("All Conditions") || book.getCondition().equals(condition);
            if (matchesCategory && matchesCondition) {
                filteredBooks.add(book);
            }
        }
        bookList.setAll(filteredBooks);
    }

    private void viewCart() {
        Stage cartStage = new Stage();
        cartStage.setTitle("Your Cart");
        VBox cartLayout = new VBox(10);
        cartLayout.setPadding(new Insets(20));
        cartLayout.setStyle("-fx-background-color: #F5DEB3;");
        TableView<Book> cartTable = new TableView<>();
        cartTable.setItems(cartItems);
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);
        TableColumn<Book, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(90);
        TableColumn<Book, Void> removeCol = new TableColumn<>("Remove");
        removeCol.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");
            {
                removeButton.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff;");
                removeButton.setOnMouseEntered(e -> removeButton.setStyle("-fx-background-color: #a8324c; -fx-text-fill: #ffffff;"));
                removeButton.setOnMouseExited(e -> removeButton.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff;"));
                removeButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    cartItems.remove(book);
                    updateCartLabel();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });
        cartTable.getColumns().addAll(titleCol, priceCol, removeCol);
        double totalPrice = cartItems.stream().mapToDouble(Book::getPrice).sum();
        Label totalLabel = new Label("Total Price: $" + String.format("%.2f", totalPrice));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #801f33;");
        cartLayout.getChildren().addAll(cartTable, totalLabel);
        Scene scene = new Scene(cartLayout, 400, 300);
        cartStage.setScene(scene);
        cartStage.show();
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Checkout");
            alert.setHeaderText(null);
            alert.setContentText("Your cart is empty.");
            alert.showAndWait();
        } else {
            ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefWidth(200);
            VBox vbox = new VBox(10, new Label("Processing..."), progressBar);
            vbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(vbox, 300, 150);
            Stage progressStage = new Stage();
            progressStage.setScene(scene);
            progressStage.show();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
                progressStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Checkout");
                alert.setHeaderText(null);
                alert.setContentText("Purchase successful!");
                alert.showAndWait();
                cartItems.clear();
                updateCartLabel();
            }));
            timeline.play();
        }
    }

    private void clearCart() {
        cartItems.clear();
        updateCartLabel();
    }

    private void updateCartLabel() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), cartContentsLabel);
        ft.setFromValue(0);
        ft.setToValue(1);
        cartContentsLabel.setText(cartItems.isEmpty() ? "Cart is empty." : "Items in Cart: " + cartItems.size());
        ft.play();
    }

    private void animateAddToCart(Button button) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), button);
        tt.setByX(10);
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.play();
    }

    private void styleCartButton(Button button) {
        button.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff; -fx-font-size: 14px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #a8324c; -fx-text-fill: #ffffff; -fx-font-size: 14px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #801f33; -fx-text-fill: #ffffff; -fx-font-size: 14px;"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Book {
        private final String title;
        private final String author;
        private final String condition;
        private final double price;
        private final String category;
        private final ImageView imageView;
        public Book(String title, String author, String condition, double price, String category, Image image) {
            this.title = title;
            this.author = author;
            this.condition = condition;
            this.price = price;
            this.category = category;
            this.imageView = new ImageView(image);
            this.imageView.setFitHeight(60);
            this.imageView.setFitWidth(40);
        }
        public String getTitle() {
            return title;
        }
        public String getAuthor() {
            return author;
        }
        public String getCondition() {
            return condition;
        }
        public double getPrice() {
            return price;
        }
        public String getCategory() {
            return category;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }
}

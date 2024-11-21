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
    	//create header label
        Label headerLabel = new Label("My Listings");
        headerLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.web("#F5DEB3"));

        //make back buttno
        Button backButton = new Button("Back");
        backButton.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 14));
        backButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black;");
        backButton.setOnAction(e -> navigateBack(stage));

        //arrange header components in hbox
        HBox headerBox = new HBox(10, backButton, headerLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setStyle("-fx-background-color: #801f33;");
        headerBox.setPadding(new Insets(10));

        //gets books from storage
        List<Book> bookListings = bookStorage.getBooks();

        //container for book listings
        VBox listingsContainer = new VBox(20);
        listingsContainer.setPadding(new Insets(20));
        listingsContainer.setStyle("-fx-background-color: #801f33;");

        //add each book as a card to the listings container
        for (Book book : bookListings) {
            VBox bookCard = createBookCard(book, listingsContainer);
            listingsContainer.getChildren().add(bookCard);
            
        }

        //make the listings scrollable
        ScrollPane scrollPane = new ScrollPane(listingsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #801f33; -fx-background: #801f33; " +
                "-fx-control-inner-background: #801f33;");
        scrollPane.setPadding(new Insets(10));

        //set up the main layout using borderpane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBox);
        mainLayout.setCenter(scrollPane);
        mainLayout.setStyle("-fx-background-color: #801f33;");

         //create+set the scene
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
    	
        // labels with book details
        Label title = new Label("Title: " + book.getName());
        Label author = new Label("Author: " + book.getAuthor());
        Label category = new Label("Category: " + book.getCategory());
        Label condition = new Label("Condition: " + book.getCondition());
        Label price = new Label(String.format("Generated Price: $%.2f", book.getSalePrice()));
        Label status = new Label("Status: Available");

        // set font for labels
        Font labelFont = Font.font("Times New Roman", FontWeight.NORMAL, 16);
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));
        author.setFont(labelFont);
        category.setFont(labelFont);
        condition.setFont(labelFont);
        price.setFont(labelFont);
        status.setFont(labelFont);

        // set text color to black for all listing labels
        title.setTextFill(Color.BLACK);
        author.setTextFill(Color.BLACK);
        category.setTextFill(Color.BLACK);
        condition.setTextFill(Color.BLACK);
        price.setTextFill(Color.BLACK);
        status.setTextFill(Color.BLACK);

        // arrange book info
        VBox bookInfoBox = new VBox(5, title, author, category, condition, price, status);
        bookInfoBox.setPadding(new Insets(10));

        // creates card container
        VBox bookCard = new VBox(10, bookInfoBox);
        bookCard.setStyle("-fx-background-color: #F5DEB3; " +
                          "-fx-border-color: black; " +
                          "-fx-border-radius: 10px; " +
                          "-fx-background-radius: 10px;");
        bookCard.setPadding(new Insets(10));

        // add rmeove button if the book is not sold
        if (!"Sold".equalsIgnoreCase(book.getCondition())) {
            Button removeButton = new Button("Remove Listing");
            removeButton.setFont(Font.font("Times New Roman", FontWeight.BOLD, 12));
            removeButton.setStyle("-fx-background-color: #f54242; -fx-text-fill: white;");

            // handle remove action
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

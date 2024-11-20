package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {
        // Login page setup
        Image logoImage = new Image("Assets/logo.png"); 
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200); 
        logoImageView.setPreserveRatio(true);

        Label headerLabel = new Label("Sun Devil Book Exchange");
        headerLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 38));
        headerLabel.setTextFill(Color.WHITE);

        Label asuriteLabel = new Label("ASU ID:");
        asuriteLabel.setTextFill(Color.WHITE);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setTextFill(Color.WHITE);

        TextField asuriteField = new TextField();
        asuriteField.setPromptText("Enter your ASU ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your Password");

        Button loginButton = new Button("Log In");
        loginButton.setPrefWidth(300);
        loginButton.setStyle("-fx-background-color: grey; -fx-text-fill: black;");
        loginButton.setDisable(true);

        asuriteField.textProperty().addListener((observable, oldValue, newValue) -> backgroundAdjust(asuriteField, passwordField, loginButton));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> backgroundAdjust(asuriteField, passwordField, loginButton));

        loginButton.setOnAction(e -> login(asuriteField, passwordField, primaryStage));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.add(asuriteLabel, 0, 0);
        gridPane.add(asuriteField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);

        VBox background = new VBox(30, logoImageView, headerLabel, gridPane);
        background.setAlignment(Pos.TOP_CENTER);
        background.setStyle("-fx-background-color: #801f33;");
        background.setPadding(new Insets(80, 50, 50, 50));

        Scene loginPage = new Scene(background, 500, 600);
        primaryStage.setTitle("Sun Devil Book Exchange Login");
        primaryStage.setScene(loginPage);
        primaryStage.show();
    }

    private void backgroundAdjust(TextField asuriteField, PasswordField passwordField, Button loginButton) {
        if (asuriteField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            loginButton.setDisable(true);
            loginButton.setStyle("-fx-background-color: grey; -fx-text-fill: black;");
        } else {
            loginButton.setDisable(false);
            loginButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black;");
        }
    }

    private void login(TextField asuriteField, PasswordField passwordField, Stage stage) {
        String asurite = asuriteField.getText();
        String password = passwordField.getText();

        if (asurite.equals("123456") && password.equals("admin")) {
            AdminPage adminPage = new AdminPage();  // Create an instance of AdminPage
            adminPage.start(stage);  // Launch Admin Page
        } else if (asurite.equals("654321") && password.equals("seller")) {
            SellerPage sellerPage = new SellerPage();
            sellerPage.start(stage);
        } else {
            loginError(Alert.AlertType.ERROR, "Login Failed", "Incorrect ASU ID or password.");
        }
    }

    private void loginError(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

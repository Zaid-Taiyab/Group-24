package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {
	
    public void start(Stage primaryStage) {
        // Login page setup
    	Image logoImage = new Image(getClass().getResource("/Assets/logo.png").toExternalForm());
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
        
        Button createAccountButton = new Button("Create Account");
        createAccountButton.setPrefWidth(300);
        createAccountButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black;");

        asuriteField.textProperty().addListener((observable, oldValue, newValue) -> backgroundAdjust(asuriteField, passwordField, loginButton));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> backgroundAdjust(asuriteField, passwordField, loginButton));

        loginButton.setOnAction(e -> login(asuriteField, passwordField, primaryStage));
        createAccountButton.setOnAction(e -> {
        	UserAccount userAccount = new UserAccount();
        	userAccount.start(primaryStage);
        });

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
        gridPane.add(createAccountButton, 1, 3);

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

    	if (Files.exists(Paths.get("accounts.txt"))) {
    		try {
    			boolean validUser = false;
    			String roles = null;

    			// Read and validate credentials
    			for (String line : Files.readAllLines(Paths.get("accounts.txt"))) {
    				String[] parts = line.split(",");
    				if (parts.length >= 3 && parts[0].equals(asurite) && parts[1].equals(password)) {
    					validUser = true;
    					roles = parts[2];
    					break;
    				}
    			}

    			if (validUser) {
    				promptUserRole(stage, roles);
    				return;
    			}
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	loginError(Alert.AlertType.ERROR, "Login Failed", "Incorrect ASU ID or password.");
    }
    
    private void promptUserRole(Stage stage, String roles) {
        String[] roleOptions = roles.split(" ");
        if (roleOptions.length == 1) {
            handleRoleNavigation(stage, roleOptions[0]);
        } else {
            // Create a choice dialog for role selection
            ChoiceDialog<String> roleDialog = new ChoiceDialog<>(roleOptions[0], roleOptions);
            roleDialog.setTitle("Select Role");
            roleDialog.setHeaderText("Multiple Roles Detected");
            roleDialog.setContentText("Choose your role for this session:");
            
            // Apply custom styling to the dialog
            DialogPane dialogPane = roleDialog.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #801f33; -fx-text-fill: white;");
            
            // Customize text and content
            dialogPane.getContent().setStyle("-fx-text-fill: white;");
            dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #801f33; -fx-text-fill: white;");

            roleDialog.showAndWait().ifPresent(selectedRole -> handleRoleNavigation(stage, selectedRole));
        }
    }
    
    private void handleRoleNavigation(Stage stage, String roles) {
        if (roles == null) return;

        // Navigate based on roles
        if (roles.contains("Admin")) {
            AdminPage adminPage = new AdminPage();
            adminPage.start(stage);
        } else if (roles.contains("Seller")) {
            SellerPage sellerPage = new SellerPage();
            sellerPage.start(stage);
        } else if (roles.contains("Buyer")) {
            Buyers buyerPage = new Buyers();
            buyerPage.start(stage);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No valid role found for this account.");
            alert.showAndWait();
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

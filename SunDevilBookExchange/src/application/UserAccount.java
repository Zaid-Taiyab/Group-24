package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class UserAccount {
	
    public void start(Stage stage) {
        Stage createAccountStage = new Stage();
        createAccountStage.setTitle("Create Account");

        //label
        Label headerLabel = new Label("Create an Account");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.WHITE);

        //text field
        TextField asuriteField = new TextField();
        asuriteField.setPromptText("Enter your ASU ID");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your Password");

        //checkbox
        CheckBox adminCheckBox = new CheckBox("Admin");
        adminCheckBox.setStyle("-fx-text-fill: white;");
        CheckBox buyerCheckBox = new CheckBox("Buyer");
        buyerCheckBox.setStyle("-fx-text-fill: white;");
        CheckBox sellerCheckBox = new CheckBox("Seller");
        sellerCheckBox.setStyle("-fx-text-fill: white;");

        //save button and action
        Button saveButton = new Button("Create Account");
        saveButton.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: black;");
        saveButton.setOnAction(e -> saveAccount(asuriteField, passwordField, adminCheckBox, buyerCheckBox, sellerCheckBox, createAccountStage));

        //layout
        VBox layout = new VBox(20, headerLabel, asuriteField, passwordField, adminCheckBox, buyerCheckBox, sellerCheckBox, saveButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #801f33;");
        Scene createAccountScene = new Scene(layout, 400, 400);

        createAccountStage.setScene(createAccountScene);
        createAccountStage.show();
    }
    
    //function to save account in a file
    private void saveAccount(TextField asuriteField, PasswordField passwordField, CheckBox adminCheckBox, CheckBox buyerCheckBox, CheckBox sellerCheckBox, Stage createAccountStage) {
        String asurite = asuriteField.getText();
        String password = passwordField.getText();

        //make sure ASU ID and password are not empty
        if (asurite.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Both fields are required.");
            alert.showAndWait();
            return;
        }

        //choose roles for account
        StringBuilder roles = new StringBuilder();
        if (adminCheckBox.isSelected()) roles.append("Admin ");
        if (buyerCheckBox.isSelected()) roles.append("Buyer ");
        if (sellerCheckBox.isSelected()) roles.append("Seller");
        
        //at least 1 role must be selected
        if (roles.toString().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "At least one role must be selected.");
            alert.showAndWait();
            return;
        }

        //save data to accounts.txt file and close it
        try (FileWriter writer = new FileWriter("accounts.txt", true)) {
            writer.write(asurite + "," + password + "," + roles.toString().trim() + "\n");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Account Created Successfully!");
            alert.showAndWait();
            createAccountStage.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save account. Please try again.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}

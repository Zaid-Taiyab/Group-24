package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminPage {

    public void start(Stage stage) {
        // Header
        Label header = new Label("Admin Dashboard");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setTextFill(Color.WHITE);
        header.setStyle("-fx-padding: 10px;");

        StackPane headerPane = new StackPane(header);
        headerPane.setStyle("-fx-background-color: #801f33;");

        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #801f33; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button viewTransactionsButton = createSidebarButton("View All Transactions");
        Button generateReportButton = createSidebarButton("Generate Sales Report");
        Button manageAccountsButton = createSidebarButton("Manage User Accounts");
        Button viewStatsButton = createSidebarButton("View System Statistics");

        // Logout Button
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(200);
        logoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        logoutButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        logoutButton.setOnAction(e -> goToLoginPage(stage));

        // Add hover effect to the logout button
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #cc0000; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-radius: 10px;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-radius: 10px;"));

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(viewTransactionsButton, generateReportButton, manageAccountsButton, viewStatsButton);

        // Add a spacer before the logout button to keep it at the bottom
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS); // Allows the spacer to take up remaining space
        sidebar.getChildren().add(spacer);
        sidebar.getChildren().add(logoutButton);

        // Transactions Bar Chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Days of the Week");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Books Sold");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Books Sold per Day");
        barChart.setLegendVisible(false);
        barChart.setStyle("-fx-background-color: #F5DEB3; -fx-padding: 10px; -fx-border-radius: 5px;");

        // Add data to the chart
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.getData().add(new XYChart.Data<>("Monday", 2));
        dataSeries.getData().add(new XYChart.Data<>("Tuesday", 5));
        dataSeries.getData().add(new XYChart.Data<>("Wednesday", 3));
        dataSeries.getData().add(new XYChart.Data<>("Thursday", 1));
        dataSeries.getData().add(new XYChart.Data<>("Friday", 4));
        dataSeries.getData().add(new XYChart.Data<>("Saturday", 2));
        dataSeries.getData().add(new XYChart.Data<>("Sunday", 6));
        barChart.getData().add(dataSeries);

        // Ensure bar color is maroon by applying color after the scene is rendered
        Platform.runLater(() -> {
            barChart.lookupAll(".default-color0.chart-bar").forEach(bar -> bar.setStyle("-fx-bar-fill: #801f33;"));
        });

        // Recent Transactions Table
        TableView<Transaction> transactionTable = new TableView<>();
        transactionTable.setStyle("-fx-background-color: #F5DEB3; -fx-padding: 10px; -fx-border-radius: 5px;");

        TableColumn<Transaction, String> bookNameCol = new TableColumn<>("Book Name");
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        TableColumn<Transaction, String> orderDateCol = new TableColumn<>("Order Date");
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Transaction, String> bookNumberCol = new TableColumn<>("Book Number");
        bookNumberCol.setCellValueFactory(new PropertyValueFactory<>("bookNumber"));

        TableColumn<Transaction, String> buyerNameCol = new TableColumn<>("Buyer Name");
        buyerNameCol.setCellValueFactory(new PropertyValueFactory<>("buyerName"));

        TableColumn<Transaction, String> sellerNameCol = new TableColumn<>("Seller Name");
        sellerNameCol.setCellValueFactory(new PropertyValueFactory<>("sellerName"));

        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        transactionTable.getColumns().addAll(bookNameCol, orderDateCol, bookNumberCol, buyerNameCol, sellerNameCol, amountCol);

        // Layout for Main Content
        VBox mainContent = new VBox(20, barChart, new Label("Recent Transactions"), transactionTable);
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
        stage.setTitle("Admin Dashboard");
        stage.show();
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: #801f33; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #d4b48c; -fx-text-fill: #801f33; -fx-border-radius: 10px; -fx-background-radius: 10px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #F5DEB3; -fx-text-fill: #801f33; -fx-border-radius: 10px; -fx-background-radius: 10px;"));
        
        return button;
    }

    private void goToLoginPage(Stage stage) {
        Main main = new Main();
        main.start(stage);  // Calls the login page
    }

    // Placeholder class for Transaction data
    public static class Transaction {
        private String bookName;
        private String orderDate;
        private String bookNumber;
        private String buyerName;
        private String sellerName;
        private String amount;

        public Transaction(String bookName, String orderDate, String bookNumber, String buyerName, String sellerName, String amount) {
            this.bookName = bookName;
            this.orderDate = orderDate;
            this.bookNumber = bookNumber;
            this.buyerName = buyerName;
            this.sellerName = sellerName;
            this.amount = amount;
        }

        public String getBookName() { return bookName; }
        public String getOrderDate() { return orderDate; }
        public String getBookNumber() { return bookNumber; }
        public String getBuyerName() { return buyerName; }
        public String getSellerName() { return sellerName; }
        public String getAmount() { return amount; }
    }

    // Placeholder methods for admin actions
    private void showTransactions() {
        System.out.println("Viewing transactions...");
    }

    private void generateSalesReport() {
        System.out.println("Generating sales report...");
    }

    private void manageUserAccounts() {
        System.out.println("Managing user accounts...");
    }

    private void viewSystemStats() {
        System.out.println("Viewing system statistics...");
    }
}

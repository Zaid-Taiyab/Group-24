package application;

public class PriceGenerator {
    private double price;

    // Constructor to initialize the price
    public PriceGenerator() {
        this.price = 0.0;
    }

    // Generate the price based on category, condition, and original price
    public void generatePrice(int categoryIndex, int conditionIndex, double originalPrice) {
        // Base price adjustment factors for each category
        double categoryMultiplier = getCategoryMultiplier(categoryIndex);
        double conditionMultiplier = getConditionMultiplier(conditionIndex);

        // Calculate the final price
        this.price = originalPrice * categoryMultiplier * conditionMultiplier;
    }

    // Get the calculated price
    public double displayPrice() {
        return this.price;
    }

    // Define category price multipliers (example values)
    private double getCategoryMultiplier(int categoryIndex) {
        return 0.9 - (categoryIndex * 0.005);
    }

    // Define condition price multipliers (example values)
    private double getConditionMultiplier(int conditionIndex) {
        return 0.9 - (conditionIndex * 0.08);
    }
}

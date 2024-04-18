package client.services;

import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class StatisticsService {

    /**
     * Creates a new StatisticsService.
     */
    @Inject
    public StatisticsService() {}

    /**
     * Calculates and displays the total cost of all expenses in the event.
     */
    public String getTotalNumber(Event event) {
        double totalCost = event.getExpenses().stream()
                .mapToDouble(Expense::getAmount).sum();
        return String.format("%.2f", totalCost);
    }

    /**
     * Calculates and displays the total cost of all expenses in the event.
     */
    public Map<String, Double> populateExpensesPerTag(Event event) {
        Map<String, Double> expensesPerTag = new HashMap<>();

        for (Expense expense : event.getExpenses()) {
            Tag tag = expense.getTag();
            String tagName;
            if (tag != null) {
                tagName = tag.getName();
            } else {
                tagName = "Other";
            }
            double amount = expense.getAmount();
            expensesPerTag.put(tagName, expensesPerTag.getOrDefault(tagName, 0.0) + amount);
        }
        return expensesPerTag;
    }

    /**
     * Calculates and displays the total cost of all expenses in the event.
     */
    public Map<String, Color> populateTagColors(Event event) {
        Map<String, Color> tagColors = new HashMap<>();

        for (Expense expense : event.getExpenses()) {
            Tag tag = expense.getTag();
            String tagName;
            if (tag != null) {
                tagName = tag.getName();
            } else {
                tagName = "Other";
                tag = new Tag("Other", 255, 255, 255);
            }
            tagColors.putIfAbsent(tagName, Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
        }
        return tagColors;
    }

    /**
     * Returns the symbol of the currency used in the event.
     *
     * @return The symbol of the currency used in the event.
     */
    public String getDataAmount(String tagName, double amount) {
        return tagName + String.format("\n%.2f ", amount);
    }

    /**
     * Returns the symbol of the currency used in the event.
     *
     * @return The symbol of the currency used in the event.
     */
    public String getDataRelative(double relativeValue) {
        return  String.format("(%.2f%%)", relativeValue * 100);
    }
}

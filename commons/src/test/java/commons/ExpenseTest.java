package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    /**
     * Tests the constructor of the Expense class.
     * It should construct an Expense with the specified title, amount, payer, involved participants, and date.
     */
    @Test
    public void testConstructor() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
        Date date = new Date();
        Tag tag = new Tag();
        String currency = "EUR";

        Expense expense = new Expense("Groceries", 50.0, paidBy, involvedParticipants, date, tag, currency);
        assertEquals("Groceries", expense.getTitle());
        assertEquals(50.0, expense.getAmount());
        assertEquals(paidBy, expense.getPaidBy());
        assertEquals(involvedParticipants, expense.getInvolvedParticipants());
        assertEquals(date, expense.getDate());
        assertEquals(tag, expense.getTag());
        assertEquals(currency, expense.getCurrency());
    }

    /**
     * Tests the getId method of the Expense class.
     * It should return the ID of the expense.
     */
    @Test
    public void testGetId() {
        Expense expense = new Expense();
        expense.setId(1L);
        assertEquals(1L, expense.getId());
    }

    /**
     * Tests the getTitle method of the Expense class.
     * It should return the title of the expense.
     */
    @Test
    public void testGetTitle() {
        Expense expense = new Expense();
        expense.setTitle("Groceries");
        assertEquals("Groceries", expense.getTitle());
    }

    /**
     * Tests the getAmount method of the Expense class.
     * It should return the amount of the expense.
     */
    @Test
    public void testGetAmount() {
        Expense expense = new Expense();
        expense.setAmount(50.0);
        assertEquals(50.0, expense.getAmount());
    }

    /**
     * Tests the getPaidBy method of the Expense class.
     * It should return the participant who paid for the expense.
     */
    @Test
    public void testGetPaidBy() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        Expense expense = new Expense();
        expense.setPaidBy(paidBy);
        assertEquals(paidBy, expense.getPaidBy());
    }

    /**
     * Tests the getInvolvedParticipants method of the Expense class.
     * It should return the list of participants involved in the expense.
     */
    @Test
    public void testGetInvolvedParticipants() {
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
        Expense expense = new Expense();
        expense.setInvolvedParticipants(involvedParticipants);
        assertEquals(involvedParticipants, expense.getInvolvedParticipants());
    }

    /**
     * Tests the getDate method of the Expense class.
     * It should return the date of the expense.
     */
    @Test
    public void testGetDate() {
        Date date = new Date();
        Expense expense = new Expense();
        expense.setDate(date);
        assertEquals(date, expense.getDate());
    }

    /**
     * Tests the getCurrency method of the Expense class.
     * It should return the currency of the expense.
     */
    @Test
    public void testGetCurrency() {
        Expense expense = new Expense();
        expense.setCurrency("HRK");
        assertEquals("HRK", expense.getCurrency());
    }

    /**
     * Tests the equals method of the Expense class.
     * It should check if two expenses are equal.
     */
    @Test
    public void testEquals() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
        Tag tag = new Tag("Test Tag", 120, 180, 220);

        Expense expense1 = new Expense("Groceries", 50.0, paidBy, involvedParticipants, new Date(), new Tag(), "EUR");
        expense1.setId(1L);
        expense1.setTag(tag);
        Expense expense2 = new Expense("Groceries", 50.0, paidBy, involvedParticipants, new Date(), new Tag(), "EUR");
        expense2.setId(1L);
        expense2.setTag(tag);

        assertEquals(expense1, expense2);
    }

    /**
     * Tests if the hashcode of two expenses will be equal if the
     * expenses are equal.
     */
    @Test
    public void testHashCodeEquals() {
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setTitle("Expense 1");
        Expense expense2 = new Expense();
        expense2.setId(1L);
        expense2.setTitle("Expense 1");

        assertEquals(expense1, expense2);
        assertEquals(expense1.hashCode(), expense2.hashCode());
    }

    /**
     * Tests whether the hashcode creates is unique, and thus if the expenses are different,
     * if the hashcode is different to.
     */
    @Test
    public void testHashCodeNotEquals() {
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setTitle("Expense 1");
        Expense expense2 = new Expense();
        expense2.setId(1L);
        expense2.setTitle("Expense 2");

        assertNotEquals(expense1, expense2);
        assertNotEquals(expense1.hashCode(), expense2.hashCode());
    }

    /**
     * Tests the toString method of the Expense class.
     */
    @Test
    public void testToString() {
        var actual = new Expense("Groceries", 50.0, new Participant(), new ArrayList<>(), new Date(),
                null, "EUR").toString();
        assertEquals("Groceries 50.0 EUR, no tag", actual);
    }
}

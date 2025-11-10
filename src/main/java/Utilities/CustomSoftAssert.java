package Utilities;

import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class CustomSoftAssert extends SoftAssert {

    private final List<String> errorMessages = new ArrayList<>();

    @Override
    public void assertTrue(boolean condition, String message) {
        super.assertTrue(condition, message);
        if (!condition) {
            errorMessages.add(message);
        }
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        super.assertFalse(condition, message);
        if (condition) {
            errorMessages.add(message);
        }
    }

    @Override
    public void assertEquals(String actual, String expected, String message) {
        super.assertEquals(actual, expected, message);
        if (!actual.equals(expected)) {
            errorMessages.add(message);
        }
    }

    @Override
    public void assertEquals(int actual, int expected, String message) {
        super.assertEquals(actual, expected, message);
        if (actual != expected) {
            errorMessages.add(message);
        }
    }

    public void assertNotNull(Object object, String message) {
        if (object == null) {
            errorMessages.add(message);
            super.fail(message);
        }
    }

    @Override
    public void assertAll() {
        super.assertAll();
        if (!errorMessages.isEmpty()) {
            throw new AssertionError("Soft assertions failed: " + String.join(", ", errorMessages));
        }
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
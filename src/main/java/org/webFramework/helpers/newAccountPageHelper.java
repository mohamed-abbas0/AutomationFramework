package org.webFramework.helpers;

import com.microsoft.playwright.ElementHandle;
import org.testng.asserts.SoftAssert;

import static org.webFramework.cosntants.WebTestData.*;

public class newAccountPageHelper {

    public void assertOnFont(String elementName, ElementHandle element, SoftAssert softAssert) {
        String family = element.evaluate("el => getComputedStyle(el).fontFamily").toString();
        family = family.replace("\"", "");
        String size = element.evaluate("el => getComputedStyle(el).fontSize").toString();
        String weight = element.evaluate("el => getComputedStyle(el).fontWeight").toString();
        String color = element.evaluate("el => getComputedStyle(el).color").toString();
        softAssert.assertEquals(family, fontFamily, elementName + " font-family should be " + fontFamily);
        softAssert.assertEquals(size, fontSize, elementName + " font-size should be " + fontSize);
        softAssert.assertEquals(weight, fontWeight, elementName + " font-weight should be " + fontWeight);
        softAssert.assertEquals(color, fontColor, elementName + " color should be " + fontColor);
    }
}

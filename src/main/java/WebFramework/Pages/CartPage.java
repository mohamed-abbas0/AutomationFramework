package WebFramework.Pages;

import Utilities.CustomSoftAssert;
import com.microsoft.playwright.Page;

import static WebFramework.Constants.CartPageElementLocators.itemName;
import static WebFramework.Constants.CartPageElementLocators.itemPrice;


public class CartPage {
    public void assertOnItemData(Page page, String name, String price, CustomSoftAssert softAssert){
        page.waitForSelector(itemName);
        softAssert.assertEquals(page.innerText(itemName), name,
                "Expected [" + name + "] but found [" + page.innerText(itemName) + "]");
        softAssert.assertEquals(page.innerText(itemPrice), price,
                "Expected [" + price + "] but found [" + page.innerText(itemPrice) + "]");
    }
}

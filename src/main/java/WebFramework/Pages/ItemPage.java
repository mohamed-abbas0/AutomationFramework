package WebFramework.Pages;

import com.microsoft.playwright.Page;

import static WebFramework.Constants.ItemPageElementLocators.*;

public class ItemPage {
    String itemName = "";
    String itemPrice = "";

    public void saveItemDataAndAddToCart(Page page){
        itemName = page.innerText(itemNameText);
        itemPrice = page.innerText(itemPriceText).replaceAll("\\s+", "");
        page.click(addToCardBtn);
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }
}

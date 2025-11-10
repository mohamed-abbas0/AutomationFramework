package WebFramework.Pages;

import com.microsoft.playwright.Page;

import static WebFramework.Constants.HomePageElementLocators.*;

public class HomePage {
    public void searchForItem(Page page, String keyword){
        page.fill(searchTextbox, keyword);
        page.keyboard().press("Enter");
    }

    public void sortItems(Page page, String sortBy){
        page.waitForSelector(sortByDropdownList);
        page.click(sortByDropdownList);
        switch (sortBy){
            case "Featured":
                page.click(sortFeaturedOption);
                break;
            case "PriceLowHigh":
                page.click(sortPriceLowHighOption);
                break;
            case "PriceHighLow":
                page.click(sortPriceHighLowOption);
                break;
            case "CustomerReview":
                page.click(sortCustomerReviewOption);
                break;
            case "NewArrivals":
                page.click(sortNewArrivalsOption);
                break;
            case "BestSellers":
                page.click(sortBestSellersOption);
                break;
        }
    }
}

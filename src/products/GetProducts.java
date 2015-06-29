package products;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import utility.Utils;

public class GetProducts extends Utils {
	
	@Test
	public void getProducts() throws Exception {
		
		// driver setup
		setUp();
		
		// navigate to Mitek home page
		driver.get("http://www.miteksystems.com/");
		
		// get context names and switch to WEBVIEW
		for (String contextName : driver.getContextHandles()) {
			//System.out.println(contextName);
			if (contextName.contains("WEBVIEW")) {
				driver.context(contextName);
				break;
			}
		}
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
		
		// locating and scrolling to the "Solutions" menu
		WebElement solutions = driver.findElement(By.xpath("//ul[@class='menu jquerymenu jquerymenu-processed']/li[1]/span"));
		
		Point point = solutions.getLocation();
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		jse.executeScript("window.scrollBy(0,"+point.getY()+")", "");
		
		// click on the "+" button at the "Solutions" menu
		solutions.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@style='display: block;']/li[1]/span")));
		
		// click on the "+" button at the "Products" menu item
		driver.findElement(By.xpath("//ul[@style='display: block;']/li[1]/span")).click();
		
		List<WebElement> products = driver.findElements(By.xpath("//section[@id='block-jquerymenu-1']/div/ul/li[1]/ul/li[1]/ul/li"));
		
		// add the names to the "expected names" list and print them
		for (int i = 0; i < products.size(); i++) {
			String product = products.get(i).findElement(By.tagName("a")).getText();
			System.out.println(product);
		}
				
		driver.quit();
		
	}

}

package products;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;
import org.testng.annotations.Test;

import utility.Utils;

public class VerifyProducts extends Utils {
	
	@Test
	public void verifyName() throws Exception {
		
		xlPath = "excel-input//Products.xlsx";
		xlSheetName = "VerifyName";
		xlWritePath = "excel-output//"+xlSheetName+".xls";
		fail = false;
		
		// read the excel file
		xlRead(xlPath, xlSheetName);
		
		// add expected products' names to the "expected names" list
		//System.out.println("Expected names:");
		for (int i = 1; i < xlRows; i++) {
			//System.out.println(localArray[i][1]);
			expNames.add(localArray[i][1]);
		}
		
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@style='display: block;']/li[1]/a")));
		
		// click on "Products" menu item
		driver.findElement(By.xpath("//ul[@style='display: block;']/li[1]/a")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
		
		// add actual products' names to the "expected names" list
		List<WebElement> actualProducts = driver.findElements(By.xpath("//article[@id='node-1038']/div/ul/li/a"));
		//System.out.println("Actual names:");
		for (int i = 0; i < actualProducts.size(); i++) {
			String actualProduct = actualProducts.get(i).getText();
			//System.out.println(actualProduct);
			actNames.add(actualProduct);
		}
		
		/*System.out.println("Actual names:");
		for (int i = 1; i < xlRows; i++) {
			//System.out.println(localArray[i][1]);
			actNames.add(localArray[i][1]);
		}*/
		
		/*actNames.add("Mobile Deposit®");
		actNames.add("Commercial Mobile Deposit Capture™");
		actNames.add("third product");
		actNames.add("Photo Verify™");
		actNames.add("fifth product");
		actNames.add("sixth product");
		actNames.add("Mobile Balance Transfer™");*/
		
		// compare the two list
		if (expNames.size() != actNames.size()) {
			Reporter.log("------------------------FAILED TESTS------------------------", true);
			Reporter.log("Number of products on the page doesn't equal to a total number of products", true);
			driver.quit();
			throw new Exception("Look at \"FAILED TESTS\" section above");
		}
		else {
			for (int i = 0; i < expNames.size(); i++) {
				if (!expNames.get(i).equals(actNames.get(i))) {
					localArray[i+1][2] = actNames.get(i);
					localArray[i+1][3] = "FAIL";
					fail = true;
					failName.add((i+1) + ") Expected product name: \"" + expNames.get(i) + "\", but actual: \"" + actNames.get(i) + "\"");
				}
				else {
					localArray[i+1][2] = actNames.get(i);
					localArray[i+1][3] = "Pass";
				}
			}
		}
		
		// write the result into the excel file
		xlWrite(xlWritePath, xlSheetName, localArray);
		
		// verify fails
		if (fail) {
			Reporter.log("------------------------FAILED TESTS------------------------", true);
			for (int i = 0; i < failName.size(); i++) {
				Reporter.log(failName.get(i), true);
			}
			driver.quit();
			throw new Exception("Look at \"FAILED TESTS\" section above");
		}
		else {
			driver.quit();
		}
		
	}

}

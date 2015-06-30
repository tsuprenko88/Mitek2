package products;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import utility.Utils;

public class WebVerifyProducts extends Utils {
	
	@Test
	public void verifyNameWeb() throws Exception {
		
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, 25);
		xlPath = "excel-input//Products.xlsx";
		xlSheetName = "VerifyName";
		xlWritePath = "excel-output//"+xlSheetName+"Web.xls";
		fail = false;
		
		// read the excel file
		xlRead(xlPath, xlSheetName);
		
		// add expected products' names to the "expected names" list
		for (int i = 1; i < xlRows; i++) {
			//System.out.println(localArray[i][1]);
			expNames.add(localArray[i][1]);
		}
		//System.out.println();
		
		// navigate to Mitek home page
		driver.get("http://www.miteksystems.com/");
		//driver.manage().window().maximize();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
		
		// click on "SITEMAP"
		driver.findElement(By.id("sitemap-tab")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='block-menu_block-4']/div/div/div/ul/li[1]/div/ul/li[1]/a/span")));
		
		// click on "Products" menu item
		driver.findElement(By.xpath("//*[@id='block-menu_block-4']/div/div/div/ul/li[1]/div/ul/li[1]/a/span")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
		
		List<WebElement> products = driver.findElements(By.xpath("//*[@id='node-1038']/div/ul/li/a/span"));
		
		// add actual products' names to the "actual names" list
		for (int i = 0; i < products.size(); i++) {
			String actProduct = products.get(i).getText();
			//System.out.println(actProduct);
			actNames.add(actProduct);
		}
		//System.out.println();
		
		List<Integer> replaceExp = new ArrayList<Integer>();
		List<Integer> replaceAct = new ArrayList<Integer>();
		
		// get the indexes of the products with the same names
		for (int i = 0; i < expNames.size(); i++) {
			for (int j = 0; j < actNames.size(); j++) {
				//System.out.println(expNames.get(i) + " : " + actNames.get(j));
				if (expNames.get(i).equals(actNames.get(j))) {
					replaceExp.add(i);
					replaceAct.add(j);
					break;
				}
			}
		}
		
		// replace at the "expected names" list the products with the same names
		if (replaceExp.size() != 0) {
			for (int i = 0; i < replaceExp.size(); i++) {
				//System.out.println(replaceExp.get(i));
				expNames.set(replaceExp.get(i),""+(replaceExp.get(i)+1)+"");
			}
		}
		
		// replace at the "actual names" list the products with the same names
		if (replaceAct.size() != 0) {
			for (int i = 0; i < replaceAct.size(); i++) {
				//System.out.println(replcaeAct.get(i));
				actNames.set(replaceAct.get(i),""+(replaceAct.get(i)+1)+"");
			}
		}
		
		// verify if "expected names" list contains only digits
		boolean printExp = false;
		for (int i = 0; i < expNames.size(); i++) {
			char chars[] = expNames.get(i).toCharArray();
			for (int j = 0; j < chars.length; j++) {
				int ascii = (int) chars[j];
				if (ascii < 48 || ascii > 57) {
					printExp = true;
				}
				//System.out.println(chars[j] + " " + ascii);
			}
		}
		
		// verify if "actual names" list contains only digits
		boolean printAct = false;
		for (int i = 0; i < actNames.size(); i++) {
			char chars[] = actNames.get(i).toCharArray();
			for (int j = 0; j < chars.length; j++) {
				int ascii = (int) chars[j];
				if (ascii < 48 || ascii > 57) {
					printAct = true;
				}
				//System.out.println(chars[j] + " " + ascii);
			}
		}
		
		// print the missed products or products with incorrect names at the menu section
		if (printExp) {
			Reporter.log("Menu - Missed products or products with incorrect names:", true);
			for (int i = 0; i < expNames.size(); i++) {
				Reporter.log(expNames.get(i), true);
			}
			Reporter.log("", true);
			fail = true;
		}
		
		// print the missed products or products with incorrect names on the "Products" page
		if (printAct) {
			Reporter.log("\"Products\" page - Missed products or products with incorrect names:", true);
			for (int i = 0; i < actNames.size(); i++) {
				Reporter.log(actNames.get(i), true);
			}
			fail = true;
			Reporter.log("", true);
		}
		
		if (fail) {
			driver.close();
			Thread.sleep(3000);
			throw new Exception("Look at logs");
		}
		else {
			Thread.sleep(3000);
			driver.close();
		}
		
	}

}


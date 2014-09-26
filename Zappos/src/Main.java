import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Main
{
	private static HashMap<Integer, ArrayList<Product>> productMap;
	public static void main(String[] args)
	{
		/*Combinations test = new Combinations(5, 25);
		test.solve();*/
		
		int numGifts = 3;
		int totalPrice = 20;
		
		APIClient api = new APIClient();
	
		System.out.println("Bear with us, we're downloading a lot of data");
		productMap = new HashMap<Integer, ArrayList<Product>>(api.getProducts(numGifts, totalPrice));
		
		for(int pricePoint : productMap.keySet())
		{
			generateProductOptionPage(pricePoint);
		}
		
		System.out.println("Okay we got it, finding your gift giving options now!");
		Combinations2 priceCombinator = new Combinations2(numGifts, totalPrice, productMap);
		HashSet<ArrayList<Integer>> priceCombinations = priceCombinator.getAllLists();
		
		

		System.out.println("There's a lot of results, so we're trying to make it a little easier on the eyes");

	}
	private static void generateProductOptionPage(int pricePoint)
	{
		File htmlOutput = new File(pricePoint+".html");
		FileWriter html;
		try {
			html = new FileWriter(htmlOutput);
			html.write("<h1>Product Choices for Approx. $"+pricePoint+"</h1>");
			
			for(Product choice : productMap.get(pricePoint))
			{
				html.write("<a href=\""+choice.productPageUrl+"\">");
				html.write("<div style=\"float:left;width:150px; height: 200px; padding: 20px;text-align:center\">");
				html.write(choice.productName+"<br>");
				html.write("<img src=\""+choice.productImageUrl+"\" />");
				html.write("</div>");
				html.write("</a>");
			}
			
			html.flush();
			html.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	private static boolean atLeastOneTrue(boolean[] options)
	{
		for(boolean option : options)
		{
			if(option)
				return true;
		}
		return false;
	}
}

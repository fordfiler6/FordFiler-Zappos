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
	static HashSet<ArrayList<Integer>> priceCombinations;
	public static void main(String[] args)
	{
		/*Combinations test = new Combinations(5, 25);
		test.solve();*/
		
		int numGifts = 5;
		int totalPrice = 150;
		
		APIClient api = new APIClient();
	
		System.out.println("Bear with us, we're downloading a lot of data");
		productMap = new HashMap<Integer, ArrayList<Product>>(api.getProducts(numGifts, totalPrice));
		System.out.println();
		for(int pricePoint : productMap.keySet())
		{
			generateProductOptionPage(pricePoint);
		}
		
		System.out.println("Okay we got it, finding your gift giving options now!");
		Combinations2 priceCombinator = new Combinations2(numGifts, totalPrice, productMap);
		priceCombinations = priceCombinator.getAllLists();
		System.out.println(priceCombinations.size());
		System.out.println("There's a lot of results, so we're trying to make it a little easier on the eyes");
		generatePriceChoicePage();

		

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
	private static void generatePriceChoicePage()
	{
		File htmlOutput = new File("giftChoices.html");
		
		FileWriter html;
		try {
			html = new FileWriter(htmlOutput);
			html.write("<h1>You can purchase gifts in the following ways</h1>");
			html.write("<ol style=\"font-size:18px;line-height:1.5em\">");
			for(ArrayList<Integer> priceCombo : priceCombinations)
			{
				
				html.write("<li>");
				for(int i = 0; i < priceCombo.size(); i++)
				{
					
					html.write("<a style=\"color:#0645AD\" href=\""+priceCombo.get(i) +".html\" target=\"_blank\" >$"+priceCombo.get(i)+" item</a>, ");
					if(i == priceCombo.size()-2)
					{
						html.write("and ");
					}
				}
				html.write("</li>");
				
			}
			html.write("</ol>");
			html.flush();
			html.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("All Done! Copy paste the following URL into your browser to view the results");
		System.out.println(htmlOutput.getAbsolutePath());
		
	}

}

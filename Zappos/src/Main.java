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
	public static void main(String[] args)
	{
		/*Combinations test = new Combinations(5, 25);
		test.solve();*/
		
		int numGifts = 3;
		int totalPrice = 50;
		
		APIClient api = new APIClient();
	
		System.out.println("Bear with us, we're downloading a lot of data");
		HashMap<Integer, ArrayList<Product>> productMap = new HashMap<Integer, ArrayList<Product>>(api.getProducts(numGifts, totalPrice));
		
		System.out.println("Okay we got it, finding your gift giving options now!");
		Combinations2 priceCombinator = new Combinations2(numGifts, totalPrice, productMap);
		HashSet<ArrayList<Integer>> priceCombinations = priceCombinator.getAllLists();

		System.out.println("There's a lot of results, so we're trying to make it a little easier on the eyes");
		File output = new File("output.csv");
		File htmlOutput = new File("output.html");

		// creates a FileWriter Object
		FileWriter writer;
		FileWriter html;
		try {
			writer = new FileWriter(output);
			html = new FileWriter(htmlOutput);
			html.write("<table>");
			for(ArrayList<Integer> priceCombo : priceCombinations)
			{
				ArrayList<ArrayList<Product>> arrayOfCombos = new ArrayList<ArrayList<Product>>();
				boolean[] hasMoreChoices = new boolean[priceCombo.size()];
				for(int i = 0; i < hasMoreChoices.length; i++ )
				{
					hasMoreChoices[i] = true;
				}
				html.write("<tr>");
				for(int pricePoint : priceCombo)
				{
					html.write("<td>~$"+pricePoint+"</td><td></td>");
					writer.write("~$" + pricePoint + ",,");
					arrayOfCombos.add(productMap.get(pricePoint));
				}
				html.write("</tr>");
				writer.write("\n");
				int count = 0;
				while(atLeastOneTrue(hasMoreChoices))
				{
					html.write("<tr>");
					for(int i = 0; i < priceCombo.size(); i++)
					{	
						if(hasMoreChoices[i])
						{
							Product choice = arrayOfCombos.get(i).get(count);
							html.write("<td>$"+choice.price+"</td><td>"+choice.productId+"</td>");
							writer.write("$"+choice.price + "," + choice.productId + ",");
						}
						else
						{
							writer.write("," + ",");
							html.write("<td></td><td></td>");
						}
						if(arrayOfCombos.get(i).size()-1 < (count+1))
						{
							hasMoreChoices[i] = false;
						}
					}
					count++;
					html.write("</tr>");
					writer.write("\n");
					
				}
				
				writer.write("\n");
			}
			html.write("</table>");
			html.flush();
			html.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// Writes the content to the file
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

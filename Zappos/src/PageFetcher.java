import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PageFetcher implements Runnable
{

	private String url;
	private static int pageCount = 0;
	private ConcurrentHashMap<Integer, ArrayList<Product>> productMap;
	double maxPricePerItem;
	public PageFetcher(String url, ConcurrentHashMap<Integer, ArrayList<Product>> productMap, double maxPricePerItem) {
		this.url = url;
		this.productMap = productMap;
		this.maxPricePerItem = maxPricePerItem;
	}
	@Override
	public void run() {
		String result = APIClient.getContents(url);
		
		pageCount++;
		if(pageCount % 10 == 0)
		{
			System.out.print(".");
		}
		else
		{
			try
				{
					Thread.sleep(20);	//help prevent threads from backing up
				}catch(Exception e){} // bury the exception - it wont break if the thread doesn't sleep
		}
		JSONObject responseObj;
		try
		{
			responseObj = new JSONObject(result);
		}catch (JSONException e)
		{
			System.out.println("Some of the data came back malformed, the program will continue to run, but you may not see all possible products");
			return;
		}

		JSONArray results = responseObj.getJSONArray("results");
		for(int i = 0; i < results.length(); i++ )
		{
			String productId = results.getJSONObject(i).getString("productId");
			double price = APIClient.getPriceFromString(results.getJSONObject(i).getString("price"));
			String productUrl = results.getJSONObject(i).getString("productUrl");
			String productImageUrl = results.getJSONObject(i).getString("thumbnailImageUrl");
			String productName = results.getJSONObject(i).getString("productName");
			
			if(price > maxPricePerItem)
				break;
			
			int intPrice = (int)Math.round(price);
			//If price point is in the hashmap, add this product to the ArrayList, if not construct an array list containing this product
			if(productMap.containsKey(intPrice))
			{
				synchronized (productMap) {
					productMap.get(intPrice).add(new Product(productId, price, productUrl, productImageUrl, productName));
				}
			}
			else
			{
				synchronized (productMap) {
					if(!productMap.containsKey(intPrice))
					{
						ArrayList<Product> temp = new ArrayList<Product>();
						temp.add(new Product(productId, price, productUrl, productImageUrl, productName));
						productMap.put(intPrice, temp);
					}
					else
					{
						productMap.get(intPrice).add(new Product(productId, price, productUrl, productImageUrl, productName));
					}
				
				}
			}
			
		}
	}
	
}

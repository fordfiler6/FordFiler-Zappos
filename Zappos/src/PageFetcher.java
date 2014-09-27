import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;


public class PageFetcher implements Runnable
{

	private String url;
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
		System.out.print(".");
		JSONObject responseObj = new JSONObject(result);

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

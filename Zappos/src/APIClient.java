import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;


public class APIClient 
{
	private final int resultsPerPage = 100;
	private final String urlBase = "http://api.zappos.com/Search?key=52ddafbe3ee659bad97fcce7c53592916a6bfd73";
	int resultCount = 0;
	double lowestPrice = Double.MAX_VALUE;
	double maxPricePerItem = 0;
	public APIClient()
	{
		String url = urlBase + "&limit=1&excludes=[\"styleId\",\"originalPrice\",\"productUrl\",\"colorId\",\"productName\",\"brandName\",\"thumbnailImageUrl\",\"percentOff\"]&sort={\"price\":\"asc\"}";
		String result = getContents(url);
		
        
		JSONObject responseObj = new JSONObject(result);
		resultCount = responseObj.getInt("totalResultCount");
		lowestPrice = getPriceFromString(responseObj.getJSONArray("results").getJSONObject(0).getString("price"));
	}
	
	public HashMap<Integer, ArrayList<Product>> getProducts(int numGifts, double totalPrice)
	{
		ConcurrentHashMap<Integer, ArrayList<Product>> productMap = new ConcurrentHashMap<Integer, ArrayList<Product>>();;
		maxPricePerItem = totalPrice - ((numGifts-1) * lowestPrice);
		int endPage = getEndPage(numGifts, totalPrice);
		String[] pageUrls = new String[endPage];
		for(int i = 0; i < endPage; i++ )
		{
			String tempUrl = urlBase + "&page=" + (i+1) + "&limit=" + resultsPerPage + "&sort={\"price\":\"asc\"}";
			pageUrls[i] = tempUrl;
		}

		ExecutorService es = Executors.newCachedThreadPool();
		for(String url : pageUrls)
		{
			es.execute(new PageFetcher(url, productMap, maxPricePerItem));
		}
		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashMap<Integer, ArrayList<Product>>(productMap);
	}
	
	private int getEndPage(int numGifts, double totalPrice)
	{

		int numPages = (int)Math.ceil((double)resultCount/resultsPerPage);
		int low = 0, high = numPages, mid = ceil((high+low)/2.0);
		int endPage = -1;
		while(endPage == -1)
		{
			String url = urlBase + "&page=" + mid + "&limit=" + resultsPerPage + "&sort={\"price\":\"asc\"}";
			String result = getContents(url);
			JSONObject responseObj = new JSONObject(result);
			JSONArray results = responseObj.getJSONArray("results");
			
			double lowPrice = getPriceFromString(results.getJSONObject(0).getString("price"));
			double highPrice = getPriceFromString(results.getJSONObject(results.length()-1).getString("price"));
			
			if(maxPricePerItem <= lowPrice)
			{
				high = mid;
				mid = ceil((high+low)/2.0);
			}
			else if(maxPricePerItem > highPrice)
			{
				low = mid;
				mid = ceil((high+low)/2.0);
			}
			else
			{
				endPage = mid;
			}
		}
		return endPage;
	}
	public static int ceil(double input)
	{
		return (int)Math.ceil(input);
	}
	
	public static double getPriceFromString(String input)
	{
		return Double.parseDouble(input.substring(1));
	}
	
	public static String getContents(String url)
	{
		String result = "";
		try {
			URL urlObj = new URL(url);
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlObj.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result += inputLine;
            in.close();

		} catch (MalformedURLException e) {
			return result;
		} catch (IOException e) {
			return result;
		}
		return result;
	}
}

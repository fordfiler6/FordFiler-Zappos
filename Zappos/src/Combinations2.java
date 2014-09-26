import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class Combinations2 
{
	private StringBuilder output = new StringBuilder();
    private int numGifts, totalPrice;
    private HashSet<ArrayList<Integer>> products;
    private HashMap<Integer, Boolean> prices;
    private int lowestPrice;
    private static int runCount = 0;
    public Combinations2(int numGifts, int totalPrice, HashMap<Integer, ArrayList<Product>> productMap)
	{
    	
		this.numGifts = numGifts;
		this.totalPrice = totalPrice;
		products = new HashSet<ArrayList<Integer>>();
		prices = new HashMap<Integer, Boolean>();
		
		for(int price : productMap.keySet())
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(price);
			products.add(temp);
			prices.put(price, true);
		}
	}
   

    public HashSet<ArrayList<Integer>> getAllLists()
    {
    	return getAllLists(numGifts);
    }
    public HashSet<ArrayList<Integer>> getAllLists(int lengthOfList)
    {
    	HashSet<ArrayList<Integer>> elements = products;
        //initialize our returned list with the number of elements calculated above
    	HashSet<ArrayList<Integer>> allLists = new HashSet<ArrayList<Integer>>();
        
        //lists of length 1 are just the original elements
        if(lengthOfList == 1) return elements;
        else
        {
            //the recursion--get all lists of length 3, length 2, all the way up to 1
        	HashSet<ArrayList<Integer>> allSublists = getAllLists(lengthOfList-1);
            
    		for(ArrayList<Integer> sublist : allSublists)
            {
    			//if only one more is needed - check hash map for availilibit
    			// 
    			if(sublist.size() == numGifts-1)
    			{
    				 int sum = 0;
                     for(Integer price : sublist)
                     {
                     	sum += price;
                     }
                     int last = totalPrice - sum;
                     if(prices.containsKey(last))
                     {
                    	 ArrayList<Integer> temp = new ArrayList<Integer>();
                         temp.add(last);
                         temp.addAll(sublist);
                         Collections.sort(temp);
                         allLists.add(temp);
                     }
    			}
    			else
    			{
		            for(ArrayList<Integer> curElements : elements)
		            {
	                
	                	runCount++;
	                    ArrayList<Integer> temp = new ArrayList<Integer>();
	                    temp.addAll(curElements);
	                    temp.addAll(sublist);
	                    int sum = 0;
	                    for(Integer price : temp)
	                    {
	                    	sum += price;
	                    }
	                    if(sum + ((numGifts-temp.size())*lowestPrice)  <= totalPrice && temp.size() != numGifts)
	                    {
	                		Collections.sort(temp);
	                		allLists.add(temp);	
	                    }
	                    else if(sum == totalPrice && temp.size() == numGifts)
	                    {
	                    	Collections.sort(temp);
	                    	allLists.add(temp);
	                    }
	            	}
    			}
            }

            return allLists;
        }
    }

}

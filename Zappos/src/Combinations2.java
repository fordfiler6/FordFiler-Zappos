import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;


public class Combinations2 
{
    private int numGifts, totalPrice;
    private HashSet<ArrayList<Integer>> products;
    private HashMap<Integer, Boolean> prices;
    private int lowestPrice;
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
    	HashSet<ArrayList<Integer>> allLists = new HashSet<ArrayList<Integer>>();
        
        //lists of length 1 are just the original elements
        if(lengthOfList == 1) return elements;
        else
        {
        	HashSet<ArrayList<Integer>> allSublists = getAllLists(lengthOfList-1);
            
    		for(ArrayList<Integer> sublist : allSublists)
            {
    			//if only one more is needed - check hash map for price point (rather than finding all combinations)
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
	                    ArrayList<Integer> temp = new ArrayList<Integer>();
	                    temp.addAll(curElements);
	                    temp.addAll(sublist);
	                    int sum = 0;
	                    for(Integer price : temp)
	                    {
	                    	sum += price;
	                    }
	                    //short circuit if the combination is already too expensive
	                    if(sum + ((numGifts-temp.size())*lowestPrice)  <= totalPrice && temp.size() != numGifts)
	                    {
	                		Collections.sort(temp);
	                		allLists.add(temp);	
	                    }
	                    //only add combinations with the right total to the final list
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

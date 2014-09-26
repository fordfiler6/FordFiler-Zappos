import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Combinations 
{
	private int numGifts, totalPrice;
	private ArrayList<Integer> products;
	private int[] gifts;
	private ArrayList<int[]> combinations;
	private int stepper = 1;
	private int stepperBase;
	private int possibleCombinationCount = 0;
	public Combinations(int numGifts, int totalPrice)
	{
		this.numGifts = numGifts;
		this.totalPrice = totalPrice;
		gifts = new int[numGifts];
		for(int i = 0; i< gifts.length;i++)
		{
			gifts[i] = 0;
		}
		combinations = new ArrayList<int[]>();
	}
	
	public void solve()
	{
		stepperBase = 1;
		int reset = 1;
		generatePricePoints();
		while(sumAll() < totalPrice)
		{
			for(int i = stepper+1; i < gifts.length; i++)	// each pointer
			{
				//step the current pointer one until...
				while(sumAll() < totalPrice)
				{
					do										// moves the stepper along
					{
						solveForX();
						printStatus();
						step();
					}while(isStepperLowest());
					gifts[stepper] = stepperBase++;
					gifts[i]++;
				}
				gifts[stepper] = reset;
				gifts[i] = reset;
			}
			do
			{
				solveForX();
				//printStatus();
				step();
			}while(isStepperLowest());
			stepperBase = reset++;
			for(int i = 1; i<gifts.length;i++)
			{
				gifts[i] = stepperBase;
			}
		}
		if(sumAll() == totalPrice)
			solveForX();
		//combinations.remove(combinations.size()-1);
		printCombinations();
	}
	
	public boolean isStepperLowest()
	{
		if(gifts[stepper] >= gifts[0])
		{
			return false;
		}
		for(int i = 1; i < stepper;i++)
		{
			if(gifts[stepper] > gifts[i])
				return false;
		}
		return true;
	}
	public int sumAll()
	{
		int sum = 0;
		for(int i = 0; i < gifts.length; i++)
		{
			if(gifts[i] < 0 || gifts[i] > products.get(products.size()-1))
				return Integer.MAX_VALUE;
			sum += products.get(gifts[i]);
		}
		return sum;
	}
	
	public boolean solveForX()
	{
		int sum = 0;
		for(int i = 1; i < gifts.length; i++)
		{
			sum += products.get(gifts[i]);
		}
		gifts[0] = products.indexOf(totalPrice - sum);
		if(gifts[0] == -1)
			return false;
		int[] newCombo = gifts.clone();
		possibleCombinationCount++;
		Arrays.sort(newCombo);
		combinations.add(newCombo);
		return true;
	}
	
	public void step()
	{
		gifts[stepper] = gifts[stepper]+1;
	}
	
	public void generatePricePoints()
	{
		products = new ArrayList<Integer>();
		for(int i = 3; i <= totalPrice - (3*(numGifts-1));i++)
		{
			products.add(i);
		}
	}
	
	public void printStatus()
	{
		for(int price : products)
		{
			System.out.print(price+"\t");
		}
		System.out.println();
		for(int index : gifts)
		{
			for(int i = 0; i < products.size(); i++)
			{
				if(index == i)
				{
					System.out.print("x\t");
				}
				else	
				{
					System.out.print("\t");
				}
			}
			System.out.println();
		}
	}
	
	public void printCombinations()
	{
		for(int[] combo : combinations)
		{
			for(int i = 0; i< combo.length; i++)
			{
				System.out.print(products.get(combo[i])+" ");
			}
			System.out.println();
		}
	}
}

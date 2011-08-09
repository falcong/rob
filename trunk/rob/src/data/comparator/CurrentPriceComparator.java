package data.comparator;

import java.util.Comparator;

import data.Solution;
import data.Supplier;

public class CurrentPriceComparator implements Comparator<Supplier> {
	Solution solution;
	int product;
	
	public CurrentPriceComparator(Solution sol, int product) {
		this.solution = sol;
		this.product = product;
	}
	
	@Override
	public int compare(Supplier s1, Supplier s2) {
		double priceS1 = s1.getDiscountedPrice(product, solution.totalQuantityBought(s1.getId()));
		if(priceS1<0)
			priceS1=Double.MAX_VALUE;
		double priceS2 = s2.getDiscountedPrice(product, solution.totalQuantityBought(s2.getId()));
		if(priceS2<0)
			priceS2=Double.MAX_VALUE;

		
		if (priceS1<priceS2)
			return -1;
		else if (priceS1==priceS2)
			return 0;
		else
			return 1;
	}
	
	public void setSolution(Solution sol){
		this.solution=sol;
		}

}

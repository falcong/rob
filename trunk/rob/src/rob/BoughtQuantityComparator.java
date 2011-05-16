package rob;

import java.util.Comparator;

public class BoughtQuantityComparator implements Comparator<Supplier> {
	
	Solution solution;
	
	public BoughtQuantityComparator(Solution sol) {
		this.solution = sol;
	}
	@Override
	public int compare(Supplier s1, Supplier s2) {
		int totalS1=solution.totalQuantityBought(s1.getId());
		int totalS2=solution.totalQuantityBought(s2.getId());
		if(totalS1<totalS2)
			return 1;
		else if (totalS1==totalS2)
			return 0;
		else
			return -1;
	}

}

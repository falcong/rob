package rob;

import java.util.Comparator;

public class ImprovableComparator implements Comparator<Supplier> {

	Solution solution;
	
	@Override
	public int compare(Supplier s1, Supplier s2) throws Error {
		if (solution==null) {
			Error e = new Error("[ImprovableComparator] Errore: non è stato impostata la soluzione corrente nel comparatore.");
			e.printStackTrace();
			throw e;
		}
		
		int gapS1=s1.quantityToIncreaseSegment(solution);
		int gapS2=s2.quantityToIncreaseSegment(solution);
		
		if (gapS1>gapS2)
			return 1;
		else if (gapS1==gapS2)
			return 0;
		else
			return -1;
	}
	
	public void setSolution(Solution sol){
		this.solution=sol;
	}

}

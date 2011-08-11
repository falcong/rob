package neighbourgenerator;

import data.Problem;
import data.Solution;

public abstract class SupplierOrderStrategy {
	
	protected Problem problem;
	
	public SupplierOrderStrategy(Problem problem){
		this.problem=problem;
	}
	
	abstract IdList createList(Solution solution, int distance);
}

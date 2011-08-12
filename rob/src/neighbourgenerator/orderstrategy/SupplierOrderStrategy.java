package neighbourgenerator.orderstrategy;

import data.IdList;
import data.Problem;
import data.Solution;

public abstract class SupplierOrderStrategy {
	
	protected Problem problem;
	
	public SupplierOrderStrategy(Problem problem){
		this.problem=problem;
	}
	
	abstract public IdList createList(Solution solution, int distance);
}

package neighbourgenerator.bansupplier.orderstrategy;

import data.IdList;
import data.Problem;
import data.Solution;

public abstract class SupplierSelectionStrategy {
	
	protected Problem problem;
	
	public SupplierSelectionStrategy(Problem problem){
		this.problem=problem;
	}
	
	abstract public IdList createList(Solution solution, int distance);
}

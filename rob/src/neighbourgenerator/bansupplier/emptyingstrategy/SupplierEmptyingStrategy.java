package neighbourgenerator.bansupplier.emptyingstrategy;

import data.IdList;
import data.Problem;
import data.Solution;

public abstract class SupplierEmptyingStrategy {
	
	protected Problem problem;
	protected Solution solution;
	protected IdList list;
	
	public SupplierEmptyingStrategy(Problem problem){
		this.problem=problem;
	}
	
	abstract public void emptySuppliers(IdList list, Solution solution);
	
}

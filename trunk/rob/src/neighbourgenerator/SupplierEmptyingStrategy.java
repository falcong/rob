package neighbourgenerator;

import data.Problem;
import data.Solution;

public abstract class SupplierEmptyingStrategy {
	
	protected Problem problem;
	protected Solution solution;
	protected IdList list;
	
	public SupplierEmptyingStrategy(Problem problem){
		this.problem=problem;
	}
	
	abstract void emptySuppliers(IdList list, Solution solution);
	
}

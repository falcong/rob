package neighbourgenerator;

import java.util.HashSet;

import data.Problem;
import data.Solution;

public abstract class SupplierOrderStrategy {
	
	protected HashSet<Integer> chosenSupplier;
	protected int[] chosenSupplierArray;
	protected Problem problem;
	protected Solution solution;
	
	public SupplierOrderStrategy(Problem problem){
		this.problem=problem;
	}
	
	boolean isChosen(int supplierId){
		return chosenSupplier.contains(supplierId);
	}
	
	public void setSolution(Solution solution){
		this.solution=solution;
	}
	
	abstract void createList(int distance);
	
	
}

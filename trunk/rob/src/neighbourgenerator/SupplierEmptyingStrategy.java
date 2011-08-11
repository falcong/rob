package neighbourgenerator;

import java.util.HashSet;

import data.Solution;

public abstract class SupplierEmptyingStrategy {
	protected Solution solution;
	protected SupplierOrderStrategy chosenSupplier;
	
	abstract void emptySupplier();
	
}

package neighbourgenerator;

import data.Problem;
import data.Solution;

public class RandomSupplierOrderStrategy extends SupplierOrderStrategy {

	public RandomSupplierOrderStrategy(Problem problem) {
		super(problem);
	}

	@Override
	void createList(int distance) {
		
	}
	
	public void setSolution(Solution solution){
		this.solution=solution;
	}

}

package neighbourgenerator;

import data.Problem;
import data.Solution;
import data.Supplier;

public class FullestFirstOrderStrategy extends SupplierOrderStrategy {
	
	protected double POOL_PORTION =0.5;

	public FullestFirstOrderStrategy(Problem problem) {
		super(problem);
	}

	@Override
	IdList createList(Solution solution, int size) {
		Supplier [] orderedSuppliers=problem.sortByBoughtQuantity(solution);
		int suppliersPoolSize=(int)(problem.getDimension()*POOL_PORTION);
		IdList list = new IdList(size);
		for (int i=0;i<size;i++) {
			boolean ok=false;
			do{
				int supplierIdx = (int)(Math.random()*suppliersPoolSize+1);
				int supplierId=orderedSuppliers[supplierIdx].getId();
				if(!list.contains(supplierId)) {
					list.add(supplierId,i);
					ok=true;
				}				
			}while(!ok);
		}
		return list;
	}

}

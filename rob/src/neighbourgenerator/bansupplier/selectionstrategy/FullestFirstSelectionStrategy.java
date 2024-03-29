package neighbourgenerator.bansupplier.selectionstrategy;

import data.IdList;
import data.Problem;
import data.Solution;
import data.Supplier;

public class FullestFirstSelectionStrategy extends SupplierSelectionStrategy {
	protected double POOL_PORTION =0.5;
	/**
	 * 
	 * @param problem il problema in cui applicare la strategia FullestFirstSelectionStrategy per un certo 
	 * BanSupplierNeighbourGenerator.
	 */
	public FullestFirstSelectionStrategy(Problem problem) {
		super(problem);
	}


	
	/**
	 * Precondizione: size <= dimensione_problema/2
	 * @return una lista contenente di size fornitori scelti casualmente fra la metà dei fornitori più pieni.
	 */
	@Override
	public IdList createList(Solution solution, int size) {
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
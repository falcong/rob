/*
 * m = n° tot fornitori
 * ordina i fornitori in base al numero totale di prodotti acquistati (decrescente)
 * fra i primi m/2 fornitori ne estrae k casuali
 * ogni fornitore viene svuotato così:
 * scarico tutti i prodotti in un fornitore casuale, se non sufficiente continuo con i successivi
 */

package neighbourgenerator;

import java.util.HashSet;

import data.Problem;
import data.Solution;
import data.Supplier;


public class BanFullNeighbourGenerator extends BanSupplierNeighbourGenerator{
	protected Problem problem;
	
	public BanFullNeighbourGenerator(Problem problem) {
		super(problem);
		this.problem=problem;
	}
	
	@Override
	public Solution generate(Solution solution, int distance){
		//copia di solution da modificare
		Solution result=new Solution(solution);
		
		Supplier [] orderedSuppliers=problem.sortByBoughtQuantity(solution);
		int suppliersPoolSize=(int)(problem.getDimension()/2);
		//Costruisco una lista di distance fornitori casuali che voglio "bandire"
		HashSet<Integer> banned = new HashSet<Integer>();
		int [] banArray=new int[distance];
		for (int i=0;i<distance;i++) {
			boolean ok=false;
			do{
				int supplierIdx = (int)(Math.random()*suppliersPoolSize+1);
				int supplierId=orderedSuppliers[supplierIdx].getId();
				if(!banned.contains(supplierId)) {
					banArray[i]=supplierId;
					banned.add(supplierId);
					ok=true;
				}				
			}while(!ok);
		}
		
		for(int i=0;i<distance;i++){
			emptySupplier(banArray[i],banned,result);
		}
		
		return result;
	}

}

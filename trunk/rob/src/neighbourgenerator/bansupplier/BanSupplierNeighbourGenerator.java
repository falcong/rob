/*
 * pesca un numero distance di fornitori casuali da bandire(presso cui quantità totale comprata >= 1)
 * ogni fornitore viene svuotato così:
 * scarico tutti i prodotti in un fornitore casuale, se non sufficiente continuo con i successivi
 */

package neighbourgenerator.bansupplier;

import neighbourgenerator.DistancedNeighbourGenerator;
import neighbourgenerator.NeighbourGenerator;
import neighbourgenerator.bansupplier.emptyingstrategy.SupplierEmptyingStrategy;
import neighbourgenerator.orderstrategy.SupplierOrderStrategy;

import data.IdList;
import data.Problem;
import data.Solution;


public class BanSupplierNeighbourGenerator extends NeighbourGenerator implements DistancedNeighbourGenerator{
	
	protected Problem problem;
	SupplierEmptyingStrategy empStrategy;
	SupplierOrderStrategy ordStrategy;
	
	public BanSupplierNeighbourGenerator(Problem problem, SupplierEmptyingStrategy empStrategy, SupplierOrderStrategy ordStrategy) {
		this.problem=problem;
		this.empStrategy=empStrategy;
		this.ordStrategy=ordStrategy;		
	}
	
	public Solution generate(Solution solution){
		final int DISTANCE = 1;
		return generate(solution, DISTANCE);
	}
	
	@Override
	public Solution generate(Solution solution, int distance){
		//copia di solution da modificare
		Solution solutionCopy=new Solution(solution);
		
		//Costruisco una lista di distance fornitori casuali che voglio "bandire"
		IdList list= ordStrategy.createList(solution, distance);
		//Modifico solutionCopy usando la strategia di empStrategy
		empStrategy.emptySuppliers(list, solutionCopy);
		
		return solutionCopy;
	}
	
}

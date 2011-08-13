/*
 * m = n° tot fornitori
 * ordina i fornitori in base al numero totale di prodotti acquistati (decrescente)
 * fra i primi m/2 fornitori ne estrae k casuali
 * ogni fornitore viene svuotato così:
 * scarico tutti i prodotti in un fornitore casuale, se non sufficiente continuo con i successivi
 */

package neighbourgenerator.bansupplier;

import neighbourgenerator.bansupplier.emptyingstrategy.RandomEmptyingStrategy;
import neighbourgenerator.bansupplier.orderstrategy.FullestFirstSelectionStrategy;
import data.Problem;


public class BanFullNeighbourGenerator extends BanSupplierNeighbourGenerator{
	
	
	public BanFullNeighbourGenerator(Problem problem) {
		super(problem, new FullestFirstSelectionStrategy(problem), new RandomEmptyingStrategy(problem));
		}
}






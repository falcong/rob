/*
 * m = n° tot fornitori
 * ordina i fornitori in base al numero totale di prodotti acquistati (decrescente)
 * fra i primi m/2 fornitori ne estrae k casuali
 * ogni fornitore viene svuotato così:
 * considero un prodotto alla volta e cerco di spostare il prodotto dove costa meno
 * (fascio di sconto attualmente attiva)
 */

package neighbourgenerator.bansupplier;

import neighbourgenerator.bansupplier.emptyingstrategy.LowestPriceEmptyingStrategy;
import neighbourgenerator.bansupplier.selectionstrategy.FullestFirstSelectionStrategy;
import data.Problem;

/**
 * 
 * Questo generatore per creare i vicini di una soluzione svuota i fornitori più pieni va a riempire
 * i fornitori con i prezzi più vantaggiosi.
 *
 */
public class DirectionedBanNeighbourGenerator extends BanSupplierNeighbourGenerator {
		
	public DirectionedBanNeighbourGenerator(Problem problem) {
		super(problem, new FullestFirstSelectionStrategy(problem), new LowestPriceEmptyingStrategy(problem));
		}
}











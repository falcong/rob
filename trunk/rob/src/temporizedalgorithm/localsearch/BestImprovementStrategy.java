package temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;

public class BestImprovementStrategy extends ExplorationStrategy {

	@Override
	public Solution explore(Solution solution, int maxNeighboursNumber, NeighbourGenerator generator) {
			Solution best=solution;
			for (int created=0;created<maxNeighboursNumber;created++){
				//controllo di non avere superato tempo max
				/*if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
					return best;
				}*/
				
				Solution neighbour=generator.generate(solution, 1);
				if(neighbour.getObjectiveFunction()<best.getObjectiveFunction()){
					best=neighbour;
				}	
			}
			if(best!=solution){ 
				//ritorno best solo se è cambiato rispetto a solution
				//stampa n° vicini generati
				//printNumNeighbours(maxNeighboursNumber);
				return best;
			}else{
				//stampa n° vicini generati
				//printNumNeighbours(maxNeighboursNumber);
				return null;
			}
		}
}

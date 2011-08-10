package solvingalgorithm.temporizedalgorithm.localsearch;

import neighbourgenerator.NeighbourGenerator;
import data.Solution;

public class FirstImprovementStrategy extends ExplorationStrategy {

	@Override
	public Solution explore(Solution solution, int maxNeighboursNumber,
			NeighbourGenerator generator) {
		int visited;
		for(visited=1;visited<=maxNeighboursNumber;visited++) {
			//controllo di non avere superato tempo max
			/*if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return solution;
			}*/
			
			//stampa vicino
			//printNeighbour(visited);
			Solution neighbour=generator.generate(solution);
			if (neighbour.getObjectiveFunction()<solution.getObjectiveFunction()){
				//stampa n° vicini generati
				//printNumNeighbours(visited);
				//#n° vicini generati
				return neighbour; //Ho trovato un vicino migliore della soluzione corrente
			}
		}
		//stampa n° vicini generati
		//printNumNeighbours(visited-1);
		//#n° vicini generati
		return null;
	}

}

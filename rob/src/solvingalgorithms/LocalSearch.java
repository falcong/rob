package solvingalgorithms;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.NeighbourGenerator;

public class LocalSearch extends Algorithm {
	public enum SuccessorChoiceMethod{
		FIRST_IMPROVEMENT, BEST_IMPROVEMENT 
	}
	
	private int maxNeighboursNumber; //Numero massimo di vicini da esplorare
	private int maxStepsNumber; //numero massimo di passi prima di arrestare
	private SuccessorChoiceMethod successorChoice;
	private NeighbourGenerator generator;
	/*
	 * info indica quali statistiche verranno stampate in statisticsn.txt
	 * info = 0 non verrà stampata alcuna statistica
	 * info = 1 n°passi e n° vicini
	 */
	int info = 0;
	String statisticsFile;
	
	
	public LocalSearch(int maxNeighboursNumber, int maxStepsNumber, SuccessorChoiceMethod successorChoice,NeighbourGenerator generator, Problem problem){
		this.problem=problem;
		this.maxNeighboursNumber=maxNeighboursNumber;
		this.maxStepsNumber=maxStepsNumber;
		this.successorChoice=successorChoice;
		this.generator= generator;
	}
	
	public Solution execute(Solution startSolution){
		Solution currentSolution=startSolution;
		int step;
		for (step=1;step<=maxStepsNumber;step++) {
			//stampa step
			printStep(step);
			Solution successor;
			switch(successorChoice) {
			case FIRST_IMPROVEMENT: successor=firstImprovementExploration(currentSolution);break;
			case BEST_IMPROVEMENT: successor=bestImprovementExploration(currentSolution);break;
			default: successor=firstImprovementExploration(currentSolution);break;
			}
			if(successor!=null) //ho un successore, continuo la ricerca
				currentSolution=successor;
			else{
				//l'esplorazione del vicinato ha ritornato null, siamo in un minimo locale
				//stampa passi totali eseguiti
				printTotalSteps(step);
				return currentSolution;
			}
		}
		//stampa passi totali eseguiti
		printTotalSteps2(step);
		return currentSolution; //arrivo qui se esaurisco il numero di passi
	}
	

	
	private Solution bestImprovementExploration(Solution solution) {
		Solution best=solution;
		for (int created=0;created<maxNeighboursNumber;created++){
			Solution neighbour=generator.generate(solution, 1);
			if(neighbour.getObjectiveFunction()<best.getObjectiveFunction())
				best=neighbour;
		}
		if(best!=solution) //ritorno best solo se è cambiato rispetto a solution
			return best;
		else
			return null;
	}

	
	private Solution firstImprovementExploration(Solution solution) {
		int visited;
		for(visited=1;visited<=maxNeighboursNumber;visited++) {
			//stampa vicino
			printNeighbour(visited);
			Solution neighbour=generator.generate(solution, 1);
			if (neighbour.getObjectiveFunction()<solution.getObjectiveFunction()){
				//stampa n° vicini generati
				printNumNeighbours(visited);
				return neighbour; //Ho trovato un vicino migliore della soluzione corrente
			}
		}
		//stampa n° vicini generati
		//TODO
		if(info==1){
			//generati visited-1 vicini
			Utility.write(statisticsFile, visited-1+"\t");
		}
		return null;
	}

	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		generator.setProblem(problem);
	}


	public void setStatistics(int info,int statistic){
		this.info = info;
		statisticsFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
	}
	
	
	private void printStep(int step){
		switch(info){
			case 0:
				break;
			case 1:
				System.out.println("step "+step);
				break;
		}
	}
	
	
	private void printTotalSteps(int steps){
		switch(info){
			case 0:
				break;
			case 1:
				if(info==1){
					//eseguiti steps-1 passi
					//metto 1 tab per i maxStepsNumber-(steps-1) passi non eseguiti
					for(int i=1; i<=maxStepsNumber-steps+1; i++){
						Utility.write(statisticsFile, "\t");
					}
					Utility.write(statisticsFile, steps-1+"\t");
				}
				break;
		}
	}
	
	
	private void printTotalSteps2(int step){
		switch(info){
		case 0:
			break;
		case 1:
			//eseguiti step-1 passi (maxStepsNumber)
			//metto 1 tab perchè al passo maxStepsNumber non genero i figli
			Utility.write(statisticsFile, "\t");
			Utility.write(statisticsFile, step-1+"\t");
			break;
		}
	}
	
	
	private void printNeighbour(int neighbour){
		switch(info){
		case 0:
			break;
		case 1:
			System.out.println(neighbour);
			break;
		}
	}
	
	
	private void printNumNeighbours(int visited){
		switch(info){
		case 0:
			break;
		case 1:
			//generati visited vicini
			Utility.write(statisticsFile, visited+"\t");
			break;
		}
	}
}














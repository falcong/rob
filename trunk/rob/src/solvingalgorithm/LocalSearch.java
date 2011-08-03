package solvingalgorithm;

import neighbourgenerator.NeighbourGenerator;
import data.Problem;
import data.Solution;
import util.Utility;

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
	 * info = 2 vari stati, n° vicini per ogni passo, passi totali (usato in statistiche 5)
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
		//# 2 tab + fo(currentSolution) + 2 tab
		printS0(currentSolution, false);
		int step;
		for (step=1;step<=maxStepsNumber;step++) {
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return currentSolution;
			}
			
			//stampa step
			printStep(step);
			Solution successor;
			//TODO eliminare switch con polimorfismo (state o strategy?)
			switch(successorChoice) {
			case FIRST_IMPROVEMENT: successor=firstImprovementExploration(currentSolution);break;
			case BEST_IMPROVEMENT: successor=bestImprovementExploration(currentSolution);break;
			default: successor=firstImprovementExploration(currentSolution);break;
			}
			if(successor!=null){ 
				//ho un successore, continuo la ricerca
				currentSolution=successor;
				//# new line
				//# 2 tab current solution + fo(currentSolution) + 2 tab
				printS0(currentSolution, true);
			}
			else{
				//l'esplorazione del vicinato ha ritornato null, siamo in un minimo locale
				//stampa passi totali eseguiti
				printTotalSteps(step);
				//# n° passi fatti + new line
				return currentSolution;
			}
		}
		//stampa passi totali eseguiti
		printTotalSteps2(step);
		//# 3 tab + numero passi
		return currentSolution; //arrivo qui se esaurisco il numero di passi
	}
	

	
	private Solution bestImprovementExploration(Solution solution) {
		Solution best=solution;
		for (int created=0;created<maxNeighboursNumber;created++){
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return best;
			}
			
			Solution neighbour=generator.generate(solution, 1);
			if(neighbour.getObjectiveFunction()<best.getObjectiveFunction()){
				best=neighbour;
			}	
		}
		if(best!=solution){ 
			//ritorno best solo se è cambiato rispetto a solution
			//stampa n° vicini generati
			printNumNeighbours(maxNeighboursNumber);
			return best;
		}else{
			//stampa n° vicini generati
			printNumNeighbours(maxNeighboursNumber);
			return null;
		}
	}

	
	private Solution firstImprovementExploration(Solution solution) {
		int visited;
		for(visited=1;visited<=maxNeighboursNumber;visited++) {
			//controllo di non avere superato tempo max
			if(finalTime!=-1  && System.currentTimeMillis()>finalTime){
				return solution;
			}
			
			//stampa vicino
			printNeighbour(visited);
			Solution neighbour=generator.generate(solution, 1);
			if (neighbour.getObjectiveFunction()<solution.getObjectiveFunction()){
				//stampa n° vicini generati
				printNumNeighbours(visited);
				//#n° vicini generati
				return neighbour; //Ho trovato un vicino migliore della soluzione corrente
			}
		}
		//stampa n° vicini generati
		printNumNeighbours(visited-1);
		//#n° vicini generati
		return null;
	}

	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		generator.setProblem(problem);
	}


	public void setStatistics(int info,String outputFile){
		this.info = info;
		this.statisticsFile = outputFile;
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
				//eseguiti steps-1 passi
				//metto 1 tab per i maxStepsNumber-(steps-1) passi non eseguiti
				for(int i=1; i<=maxStepsNumber-steps+1; i++){
					Utility.write(statisticsFile, "\t");
				}
				Utility.write(statisticsFile, steps-1+"\t");
				break;
			case 2:
				//eseguiti steps-1 passi
				Utility.write(statisticsFile, steps-1+"\t"+System.getProperty("line.separator"));
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
		case 2:
			//eseguiti step-1 passi (maxStepsNumber)
			Utility.write(statisticsFile, "\t"+(step-1)+"\t"+System.getProperty("line.separator"));
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
		case 2:
			Utility.write(statisticsFile, visited+"\t");
			break;
		}
	}
	
	
	private void printS0(Solution sol, boolean newline){
		switch(info){
		case 0:
			break;
		case 1:
			break;
		case 2:
			String text = "";
			if(newline){
				text += System.getProperty("line.separator");
			}
			text += "\t\t"+sol.getObjectiveFunction()+"\t\t";
			Utility.write(statisticsFile, text);
			//console
			System.out.println("\t"+sol.getObjectiveFunction());
			break;
		}
	}
	
	public void setFinalTime(long finalTime){
		this.finalTime = finalTime;
	}
	
	
	public void setStartTime(long startTime){
		this.startTime = startTime;
	}
}














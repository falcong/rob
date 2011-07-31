package rob;

import io.ProblemParser;

import java.io.PrintStream;

import solutionhandlers.AdvancedNeighbourGenerator;
import solutionhandlers.EmptyCellsRandomNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.VNS;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;

public class Rob {
		
	public static void main(String[] args){
		int statistic = 5;
		final int FILE_NUMBER = 15;
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+"_"+FILE_NUMBER+".txt";
		//prob 62
		String problemName = "Cap.50.100.3.2.10.2.ctqd";
		//prob 70
		//String problemName = "Cap.100.100.5.1.80.2.ctqd";
		//prob 63
		//String problemName = "Cap.50.100.3.2.10.1.ctqd";
		
		ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = null;
		//TODO vedere 3
		try {
			problem = probParser.parse(problemName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Si è verificato un errore il programma verrà terminato\n"+
								"(il parser non è stato in grado di leggere il problema).");
			System.exit(1);
		}
		
		//ottimo
/*		Solution cplex = new Solution(Utility.getConfigParameter("input")+
				System.getProperty("file.separator")+"cplex_solution_"+problemName+".txt", problem);*/
		
		//local search
		int maxNeighboursNumber = 50;
		int maxStepsNumber = 1000;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.BEST_IMPROVEMENT;
		AdvancedNeighbourGenerator neighGenerator = new AdvancedNeighbourGenerator(problem);
      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
      			neighGenerator, problem);
      	locSearch.setStatistics(2, outputFile);
		
		//vns interna
      	double ratio = 12/50;
		EmptyCellsRandomNeighbourGenerator intShaking = new EmptyCellsRandomNeighbourGenerator(problem, ratio);
		int lMax = 10;
		int kIncrement = 1;
		String intLabel = "i";
		VNS intVNS = new VNS(lMax, locSearch, intShaking, problem, -1, 2000);
		intVNS.setStatistics(1, outputFile, intLabel);
		//intVNS.setCplex(cplex);
		intVNS.setIncrement(kIncrement);
		
		//vns esterna
      	//s0 = soluzione lines
      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
      	Solution lineSol = linesGenerator.generate();
      	
      	//s0 = soluzione random
/*		RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
      	Solution s0 = randomGenerator.generate();*/
      	
      	//s0 = soluzione trivial
      	TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
      	Solution s0 = trivialGenerator.generate();
      	
/*      	int kMax = 6;
      	BanFullNeighbourGenerator extShaking = new BanFullNeighbourGenerator(problem);
      	int numRestarts = -1;
      	int time = 3600;
      	String extLabel = "e";
      	VNS extVNS = new VNS(kMax, intVNS, extShaking, problem, numRestarts, time);
      	extVNS.setStatistics(1, outputFile, extLabel);*/
      	//extVNS.setCplex(cplex);
		
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		/*
		 * lancio VNS: verrà interrotta manualmente!
		 */
		//extVNS.execute(s0);
		intVNS.execute(lineSol);
		
	}
}

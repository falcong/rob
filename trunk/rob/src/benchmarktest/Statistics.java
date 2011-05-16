package benchmarktest;

import io.ProblemParser;

import java.awt.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.AdvancedNeighbourGenerator;
import solutionhandlers.AdvancedNeighbourGenerator2;
import solutionhandlers.BanSupplierNeighbourGenerator;
import solutionhandlers.EmptyCellsNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;
import solvingalgorithms.VNS;

public class Statistics {
	@Test
	public void statistic1(){
		int statistic = 1;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 20;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione trivial
	      	TrivialSolutionGenerator trivGenerator = new TrivialSolutionGenerator(problem);
	      	Solution s0 = trivGenerator.generate();
	      	

			AdvancedNeighbourGenerator neighGenerator = new AdvancedNeighbourGenerator(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
	
	
	@Test
	public void statistic2(){
		int statistic = 2;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 30;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione trivial
	      	TrivialSolutionGenerator trivGenerator = new TrivialSolutionGenerator(problem);
	      	Solution s0 = trivGenerator.generate();
	      	

			AdvancedNeighbourGenerator2 neighGenerator = new AdvancedNeighbourGenerator2(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa2(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
	
	
	@Test
	public void statistic3(){
		int statistic = 3;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 20;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione lines
	      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
	      	Solution s0 = linesGenerator.generate();
	      	

			AdvancedNeighbourGenerator neighGenerator = new AdvancedNeighbourGenerator(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
	
	
	@Test
	public void statistic4(){
		int statistic = 4;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 20;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione lines
	      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
	      	Solution s0 = linesGenerator.generate();
	      	

			AdvancedNeighbourGenerator2 neighGenerator = new AdvancedNeighbourGenerator2(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa2(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
	
	
	@Test
	public void statistic5(){
		int statistic = 5;
		final int FILE_NUMBER = 9;
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+"_"+FILE_NUMBER+".txt";
		//prob 62
		String problemName = "Cap.50.100.3.2.10.2.ctqd";
		ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = probParser.parse(problemName);
		
		//ottimo
/*		Solution cplex = new Solution(Utility.getConfigParameter("input")+
				System.getProperty("file.separator")+"cplex_solution_"+problemName+".txt", problem);*/
		
		//local search
		int maxNeighboursNumber = 100;
		int maxStepsNumber = 15;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.BEST_IMPROVEMENT;
		AdvancedNeighbourGenerator2 neighGenerator = new AdvancedNeighbourGenerator2(problem);
      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
      			neighGenerator, problem);
      	locSearch.setStatistics(2, outputFile);
		
		//vns interna
		EmptyCellsNeighbourGenerator intShaking = new EmptyCellsNeighbourGenerator(problem);
		int lMax = 13;
		int kIncrement = 3;
		String intLabel = "i";
		VNS intVNS = new VNS(lMax, locSearch, intShaking, problem);
		intVNS.setStatistics(1, outputFile, intLabel);
		//intVNS.setCplex(cplex);
		intVNS.setIncrement(kIncrement);
		
		//vns esterna
      	//s0 = soluzione lines
      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
      	Solution s0 = linesGenerator.generate();
      	int kMax = 5;
      	BanSupplierNeighbourGenerator extShaking = new BanSupplierNeighbourGenerator(problem);
      	int numRestarts = 1000000;
      	int time = -1;
      	String extLabel = "e";
      	VNS extVNS = new VNS(kMax, intVNS, extShaking, problem, numRestarts, time);
      	extVNS.setStatistics(1, outputFile, extLabel);
      	//extVNS.setCplex(cplex);
		
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		/*
		 * lancio VNS: verrà interrotta manualmente!
		 */
		extVNS.execute(s0);
	}
	
	@Test
	public void statistic6(){
		int statistic = 6;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 20;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione lines
	      	LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
	      	Solution s0 = linesGenerator.generate();
	      	

			AdvancedNeighbourGenerator2 neighGenerator = new AdvancedNeighbourGenerator2(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa2(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
	
	
	@Test
	public void statistic7(){
		int statistic = 7;
		String file = "campione2.txt";
		String outputFile = Utility.getConfigParameter("statistics")+"\\statistics"+statistic+".txt";
		int maxNeighboursNumber = 20;
		int maxStepsNumber = 20;
		SuccessorChoiceMethod successorChoice = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//considero tutti le istanze di campione2
		List problems = new List();
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null){
				problems.add(line);
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		//cancello eventuale contenuto file
		PrintStream out = Utility.openOutFile(outputFile, false);
		out.print("");
		out.close();
		
		//eseguo una istanza alla volta
		for(int p = 0; p<problems.getItemCount(); p++){
			String problemName = problems.getItem(p);
			ProblemParser probParser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
			Problem problem = probParser.parse(problemName);
			
			//STAMPA nome istanza
			out = Utility.openOutFile(outputFile, true);
			out.print(problemName+"\t");
	      	out.close();
	      	System.out.println(problemName);
	      	
	      	//s0 = soluzione trivial
	      	TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
	      	Solution s0 = trivialGenerator.generate();
	      	

			AdvancedNeighbourGenerator2 neighGenerator = new AdvancedNeighbourGenerator2(problem);
	      	LocalSearch locSearch = new LocalSearch(maxNeighboursNumber, maxStepsNumber, successorChoice,
	      			neighGenerator, problem);
	      	locSearch.setStatistics(1, outputFile);
	      	
	      	//s1 = lsa2(s0)
	      	Solution s1 = locSearch.execute(s0);
	      	//STAMPA fo di s1
			out = Utility.openOutFile(outputFile, true);
			out.print(s1.getObjectiveFunction()+"\t");
	      	
	      	//STAMPA fine riga [\n]
			out.print(System.getProperty("line.separator"));
	      	out.close();
		}
	}
}

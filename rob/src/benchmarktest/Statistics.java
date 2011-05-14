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
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;

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
	      	locSearch.setStatistics(1, statistic);
	      	
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
	      	locSearch.setStatistics(1, statistic);
	      	
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
	      	locSearch.setStatistics(1, statistic);
	      	
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
	      	locSearch.setStatistics(1, statistic);
	      	
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

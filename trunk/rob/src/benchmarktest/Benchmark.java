package benchmarktest;

import static org.junit.Assert.*;
import io.ProblemParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import rob.Problem;
import rob.Solution;
import rob.Utility;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.Cplex;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;
import solvingalgorithms.VNS;

public class Benchmark {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public final void testBenchmark() {
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 236;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("results2.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file results.txt");
		}
		//parametri VNS
		final int K_MAX = 50;
		//parametri Local Search
		final int MAX_NEIGHBOURS		= 1000;
		final int MAX_STEPS_NUMBER		= 1000;
		final SuccessorChoiceMethod successorChoice	= SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t\t\t");
			
			//n_fornitori*n_prodotti
			int problemSize = problem.getDimension()*problem.getNumProducts();
			outputFile.print(problemSize+"\t\t\t");
				
			//classe del problema
			outputFile.print(problem.getProblemClass()+"\t\t\t");
			
			//cplex
			Cplex cplex 				= new Cplex(problem);
			long cplexStart			= System.currentTimeMillis();
			Solution cplexSolution		= cplex.execute(null);
			long cplexEnd				= System.currentTimeMillis();
			long cplexTime			= cplexEnd-cplexStart;
			
			
			double cplexObjFunction	= cplexSolution.getObjectiveFunction();
			
			//ottimo (cplex)
			outputFile.print(cplexObjFunction+"\t\t\t");
			
			//tempo cplex
			outputFile.print(cplexTime+"\t\t\t");
			
			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			Solution trivialSolution = trivialGenerator.generate();
			
			//soluzione trivial
			double trivialObjFunction = trivialSolution.getObjectiveFunction();
			outputFile.print(trivialObjFunction+"\t\t\t");
			
			//VNS
			LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS, MAX_STEPS_NUMBER, successorChoice, null, problem);
			VNS vns = new VNS(K_MAX, localSearch, null, problem);
			
			long vnsStart				= System.currentTimeMillis();
			Solution vnsSolution		= vns.execute(trivialSolution);
			long vnsEnd					= System.currentTimeMillis();
			long vnsTime				= vnsEnd-vnsStart;
			double vnsObjFunction	= vnsSolution.getObjectiveFunction();
			
			//k_max
			outputFile.print(K_MAX+"\t\t\t");
			
			//scelta successore
			outputFile.print(successorChoice+"\t\t\t");
			
			//n° max vicini
			outputFile.print(MAX_NEIGHBOURS+"\t\t\t");
			
			//n° max passi
			outputFile.print(MAX_STEPS_NUMBER+"\t\t\t");
			
			//sub-ottimo VNS
			outputFile.print(vnsObjFunction+"\t\t\t");
			
			//tempo VNS
			outputFile.print(vnsTime+"\t\t\t");
			
			//fine riga
			outputFile.print("\n");

		}
		
		
	}

	/*
	 * Riceve in ingresso la lista di tutti i file contenenti i problemi da eseguire.
	 * Ordina tale lista in senso crescente in base al valore di dimension*numProducts
	 */
	private String[] orderFileList(String[] fileList) {
		int lenght = fileList.length;
		String orderedFileList[] = new String[lenght];
		
		//per ogni problema calcolo size = dimension*numProducts
		int size[] = new int[lenght];
		for(int i=0; i<lenght; i++){
			int dimension		= getProblemParameters(fileList[i])[0];
			int numProducts	= getProblemParameters(fileList[i])[1];
			size[i]			= dimension*numProducts;
		}
		
		//indica quali problemi sono già stati inseriti in orderedList
		boolean alreadyChosen[] = new boolean[lenght];
		Arrays.fill(alreadyChosen, false);
		
		for(int i=0; i<lenght; i++){
			//devo riempire orderedList[i]
			//cerco fra tutti i problemi non ancora scelti quello con la minor size 
			int bestSize = Integer.MAX_VALUE;
			int bestProblem = -1;
			for(int j=0; j<lenght; j++){
				if(!alreadyChosen[j]	&&	size[j]<bestSize){
					bestSize = size[j];
					bestProblem = j;
				}
			}
			orderedFileList[i]			= fileList[bestProblem];
			alreadyChosen[bestProblem]	= true;
			
		}
		
		return orderedFileList;
	}

	/*
	 * Riceve in ingresso il nome di un problema; restituisce un array di int result contenente i valori
	 * dimension e numProducts del problema
	 * Ese:	(Cap.10.40.3.1.10.1.ctqd, dimension)	-> result con
	 * result[0] = 10 (dimension)
	 * result[1] = 40 (numProducts)
	 */
	//t
	private int[] getProblemParameters(String problemName) {
		//posizione del primo punto
		int firstDot = 3;
		//cerco il secondo punto
		int i;
		for(i = firstDot+1; i<problemName.length(); i++){
			if(problemName.charAt(i)=='.'){
				break;
			}
		}
		int secondDot = i;
		//cerco il terzo punto
		for(i++; i<problemName.length(); i++){
			if(problemName.charAt(i)=='.'){
				break;
			}
		}
		int thirdDot = i;
		
		int result[] = new int[2];
		//dimension
		result[0]	= Integer.parseInt(problemName.substring(firstDot+1, secondDot));
		//numProducts
		result[1]	= Integer.parseInt(problemName.substring(secondDot+1, thirdDot));
		return result;
	}
	
	
	@Test
	public final void testGetProblemParameters() {
		int result[] = getProblemParameters("Cap.10.40.3.1.10.1.ctqd");
		assertEquals(10, result[0]);
		assertEquals(40, result[1]);
	}
	

	@Test
	public void prova2(){
		//sub-ottimo trivial di Cap.50.100.3.2.10.2.ctqd
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
			
		//trivial
		TrivialSolutionGenerator trivial = new TrivialSolutionGenerator(problem);
		Solution trivialSolution	= trivial.generate();
		trivialSolution.export("soluzione_trivial.txt");
	}
	
	@Test
	public void printNumOfferedProducts(){
		PrintStream ps = null;
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter("problemsPath"));
		Problem problem = parser.parse("Cap.50.100.3.2.80.4.ctqd");
		try {
			ps = new PrintStream(new FileOutputStream("numOfferedProducts.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.setOut(ps);
		
		for (int i=1;i<=problem.getDimension();i++) {
			System.out.println(problem.getSupplier(i).getNumOfferedProducts());
		}
		System.setOut(System.out);
	}
	
	
	@Test
	public final void trivialVsLine() {
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("resultsTrivialVSLine.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file resultsTrivialVSLine.txt");
		}
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t\t\t");
			
			//n_fornitori*n_prodotti
			int problemSize = problem.getDimension()*problem.getNumProducts();
			outputFile.print(problemSize+"\t\t\t");
				
			//classe del problema
			outputFile.print(problem.getProblemClass()+"\t\t\t");
			

			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			Solution trivialSolution = trivialGenerator.generate();
			
			//soluzione trivial
			double trivialObjFunction = trivialSolution.getObjectiveFunction();
			outputFile.print(trivialObjFunction+"\t\t\t");
			
			//Line
			LinesSolutionGenerator lineGenerator = new LinesSolutionGenerator(problem);
			Solution lineSolution = lineGenerator.generate();
			
			//soluzione Line
			double lineSolutionObj = lineSolution.getObjectiveFunction();
			outputFile.print(lineSolutionObj+"\t\t\t");
			

			//fine riga
			outputFile.print("\n");

		}
	}
	
	
	@Test
	public final void lineVNS() {
		//parametri VNS
		final int K_MAX = 50;
		//parametri Local Search
		final int MAX_NEIGHBOURS		= 100;
		final int MAX_STEPS_NUMBER		= 1000;
		final SuccessorChoiceMethod successorChoice	= SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("lineVNS.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file lineVNS.txt");
		}
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t");
			
			//Line
			LinesSolutionGenerator lineGenerator = new LinesSolutionGenerator(problem);
			Solution lineSolution = lineGenerator.generate();
			
			//soluzione Line
			double lineSolutionObj = lineSolution.getObjectiveFunction();
			outputFile.print(lineSolutionObj+"\t");
			
			//VNS
			LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS, MAX_STEPS_NUMBER, successorChoice, null, problem);
			VNS vns = new VNS(K_MAX, localSearch, null, problem);
			
			long vnsStart				= System.currentTimeMillis();
			Solution vnsSolution		= vns.execute(lineSolution);
			long vnsEnd					= System.currentTimeMillis();
			long vnsTime				= vnsEnd-vnsStart;
			double vnsObjFunction	= vnsSolution.getObjectiveFunction();

			//sub-ottimo VNS
			outputFile.print(vnsObjFunction+"\t");
			
			//tempo VNS
			outputFile.print(vnsTime+"\t");

			//fine riga
			outputFile.print("\n");

		}
	}
	
	
	/*
	 * tempi trivial del blocco 1
	 */
	@Test
	public final void testBlock1() {
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 6;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "block1";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("results_block1.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file results_block1.txt.txt");
		}

		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t\t\t");

			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			long trivialStart				= System.nanoTime();
			Solution trivialSolution	= trivialGenerator.generate();
			long trivialEnd				= System.nanoTime();
			long trivialTime				= trivialEnd-trivialStart;
			
			//tempo trivial
			outputFile.print(trivialTime+"\n");
		}
		
		
	}
	
	
	/*
	 * tempi trivial del blocco 2
	 */
	@Test
	public final void testBlock2() {
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 6;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "block2";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("results_block2.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file results_block2.txt.txt");
		}

		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t");

			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			long trivialStart				= System.nanoTime();
			Solution trivialSolution	= trivialGenerator.generate();
			long trivialEnd				= System.nanoTime();
			long trivialTime				= trivialEnd-trivialStart;
			
			//tempo trivial
			outputFile.print(trivialTime+"\n");
		}
	}
	
	
	/*
	 * tempi trivial dei blocchi rimanenti
	 */
	@Test
	public final void testBlocks() {
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 100000000;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "restanti";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("results_blocks.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file results_blocks.txt.txt");
		}

		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t");

			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			long trivialStart				= System.nanoTime();
			Solution trivialSolution	= trivialGenerator.generate();
			long trivialEnd				= System.nanoTime();
			long trivialTime				= trivialEnd-trivialStart;
			
			//tempo trivial
			outputFile.print(trivialTime+"\n");
		}
	}
	
	
	@Test
	public void remainingColumns(){
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("remainingColumns.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file remainingColumns.txt");
		}
		//parametri VNS
		final int K_MAX = 50;
		//parametri Local Search
		final int MAX_NEIGHBOURS		= 100;
		final int MAX_STEPS_NUMBER		= 1000;
		final SuccessorChoiceMethod successorChoice	= SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t");
			
			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			long trivialStart = System.nanoTime();
			Solution trivialSolution = trivialGenerator.generate();
			long trivialEnd = System.nanoTime();
			double trivialTime = (double)(trivialEnd-trivialStart)/1000000;
			//double trivialTime = (trivialEnd-trivialStart);
			outputFile.print(trivialTime+"\t");
			
			//soluzione trivial
/*			double trivialObjFunction = trivialSolution.getObjectiveFunction();
			outputFile.print(trivialObjFunction+"\t");*/
			
			//VNS(trivial)
/*			LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS, MAX_STEPS_NUMBER, successorChoice, problem);
			VNS vns = new VNS(K_MAX, localSearch, problem);
			
			long vnsStart				= System.currentTimeMillis();
			Solution vnsSolution		= vns.execute(trivialSolution);
			long vnsEnd					= System.currentTimeMillis();
			long vnsTime				= vnsEnd-vnsStart;
			double vnsObjFunction	= vnsSolution.getObjectiveFunction();
			
			//sub-ottimo VNS(trivial)
			outputFile.print(vnsObjFunction+"\t");
			
			//tempo VNS(trivial)
			outputFile.print(vnsTime+"\t");*/
			
			//Lines
			LinesSolutionGenerator lineGenerator = new LinesSolutionGenerator(problem);
			long linesStart			= System.nanoTime();
			Solution lineSolution	= lineGenerator.generate();
			long linesEnd				= System.nanoTime();
			double linesTime				= (double)(linesEnd-linesStart)/1000000;
			//double linesTime				= (linesEnd-linesStart);
			
			//soluzione lines
/*			double lineSolutionObj = lineSolution.getObjectiveFunction();
			outputFile.print(lineSolutionObj+"\t");*/
			
			//tempo lines
			outputFile.print(linesTime+"\t");
			
			//VNS(lines)
/*			long linesVnsStart					= System.currentTimeMillis();
			Solution linesVnsSolution			= vns.execute(lineSolution);
			long linesVnsEnd						= System.currentTimeMillis();
			long linesVnsTime						= linesVnsEnd-linesVnsStart;
			double linesVnsObjFunction			= linesVnsSolution.getObjectiveFunction();

			//sub-ottimo VNS(lines)
			outputFile.print(linesVnsObjFunction+"\t");
			
			//tempo VNS(lines)
			outputFile.print(linesVnsTime+"\t");*/
			
			//fine riga
			outputFile.print("\n");

		}
	}
	
	
	/*
	 * stampa sol ottimo, sol trivial e distanza dei problemi in toDoProblemsPath
	 */
	@Test
	public void distance(){
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		//
		final String SOLUTIONS_FOLDER = Utility.getConfigParameter("exportedSolutionsFolder");
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			System.out.println("esecuzione del problema: "+problem.getName());
			
			//trivial
			TrivialSolutionGenerator trivialGenerator = new TrivialSolutionGenerator(problem);
			Solution trivialSolution = trivialGenerator.generate();
			trivialSolution.export(SOLUTIONS_FOLDER+"trivial_solution_"+problem.getName()+".txt");
			
			//cplex
			Cplex cplex 					= new Cplex(problem);
			Solution cplexSolution		= cplex.execute(null);
			cplexSolution.export(SOLUTIONS_FOLDER+"cplex_solution_"+problem.getName()+".txt");
			
			//distanza
			System.out.println("distanza\t=\t" + cplexSolution.calcDistance(trivialSolution));
			}
	}
	
	
	/*
	 * stampa sol lines dei problemi in toDoProblemsPath e le distanze dagli ottimi
	 */
	@Test
	public void printLinesSolutions(){
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		//
		final String SOLUTIONS_FOLDER = Utility.getConfigParameter("exportedSolutionsFolder");
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			System.out.println("esecuzione del problema: "+problem.getName());
			
			//lines
			LinesSolutionGenerator linesGenerator = new LinesSolutionGenerator(problem);
			Solution linesSolution = linesGenerator.generate();
			linesSolution.export("lines_solution_"+problem.getName()+".txt");
			
			//importo l'ottimo
			Solution cplexSolution = new Solution("cplex_solution_"+problem.getName()+".txt", problem);

			
			//distanza
			System.out.println("distanza\t=\t" + cplexSolution.calcDistance(linesSolution));
			}
	}
	
	
	@Test
	public final void randomVNS() {
		//parametri VNS
		final int K_MAX = 50;
		//parametri Local Search
		final int MAX_NEIGHBOURS		= 100;
		final int MAX_STEPS_NUMBER		= 1000;
		final SuccessorChoiceMethod successorChoice	= SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		
		//numero di problemi da eseguire
		final int NUM_PROBLEMS	= 76;
		final int START_PROBLEM	= 0;
		//
		final String PROBLEMS_FOLDER = "toDoProblemsPath";
		
		File folder = new File(Utility.getConfigParameter(PROBLEMS_FOLDER));
		String fileList[] = folder.list();
		//creo una copia ordinata di fileList
		String orderedFileList[] = orderFileList(fileList);
		
		
		ProblemParser parser = new ProblemParser(Utility.getConfigParameter(PROBLEMS_FOLDER));
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new FileOutputStream("randomVNS.txt"));
		} catch (FileNotFoundException e) {
			throw new Error("Errore: non e presente in rob2 il file randomVNS.txt");
		}
		for(int i=START_PROBLEM; i<NUM_PROBLEMS+START_PROBLEM && i<fileList.length; i++){
			String file = orderedFileList[i];
			Problem problem = parser.parse(file);
			
			//nome del problema
			outputFile.print(problem.getName()+"\t");
			
			//Line
			RandomSolutionGenerator randomGenerator = new RandomSolutionGenerator(problem);
			Solution randomSolution = randomGenerator.generate();
			
			//soluzione Line
			double randomSolutionObj = randomSolution.getObjectiveFunction();
			outputFile.print(randomSolutionObj+"\t");
			
			//SolutionGenerator
			BasicNeighbourGenerator nGenerator = new BasicNeighbourGenerator(problem);
			
			//VNS
			LocalSearch localSearch = new LocalSearch(MAX_NEIGHBOURS, MAX_STEPS_NUMBER, successorChoice, nGenerator, problem);
			VNS vns = new VNS(K_MAX, localSearch, nGenerator, problem);
			
			
			Solution vnsSolution		= vns.execute(randomSolution);
			
			//sub-ottimo VNS
			outputFile.print(vnsSolution.getObjectiveFunction()+"\t");

			//fine riga
			outputFile.print("\n");

		}
	}
}

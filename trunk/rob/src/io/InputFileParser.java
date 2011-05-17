package io;

import java.io.File;
import java.io.FileInputStream;
//import java.io.IOException;
import java.util.Properties;

import rob.Problem;
import rob.Run;
import solutionhandlers.BasicNeighbourGenerator;
import solutionhandlers.FileSolutionGenerator;
import solutionhandlers.LinesSolutionGenerator;
import solutionhandlers.NeighbourGenerator;
import solutionhandlers.RandomSolutionGenerator;
import solutionhandlers.SolutionGenerator;
import solutionhandlers.TrivialSolutionGenerator;
import solvingalgorithms.Algorithm;
import solvingalgorithms.Cplex;
import solvingalgorithms.LocalSearch;
import solvingalgorithms.LocalSearch.SuccessorChoiceMethod;
import solvingalgorithms.VNS;

public class InputFileParser extends Parser {
	
	/*
	 * DEFAULTS
	 * Qui sono specificati tutti i valori di default da usare qualora ci siano errori od omissioni nel file di input
	 */
	
	private static final String DEFAULT_ALGORITHM = "CPLEX";
	private static final SuccessorChoiceMethod DEFAULT_TYPE = SuccessorChoiceMethod.FIRST_IMPROVEMENT;
	private static final int DEFAULT_MAX_NEIGHBOUR_NUMBER = 100;
	private static final int DEFAULT_MAX_STEP_NUMBER = 1000;
	private static final String DEFAULT_AFTERSHAKING_ALGORITHM = "LocalSearch";
	private static final String DEFAULT_SOLUTION_GENERATOR = "Random";
	private static final int DEFAULT_KMAX = 50;
	private static final String DEFAULT_EXPORT_FOLDER = "";
	private static final String DEFAULT_FOLDER = "";
	
	
	Properties properties;
	
	@Override
	public Object parse(String inputFile) throws Exception, Error {
		Run run = new Run();
		
		/*
		 * Estraggo tutte le proprietà del file
		 */
		properties = new Properties();
      properties.load(new FileInputStream(inputFile));
      
      /*
       * Leggo le proprietà e creo gli oggetti opportuni 
       */
            
      // Lista dei problemi      
      String path = readProperty("Folder");
      String fileProperty=readProperty("Files"); 
      
      Problem [] problems = makeProblems(path,fileProperty);
      
      run.setProblems(problems);
      
      //Algoritmi di risoluzione. Eventuali algoritmi interni e rispettivi paramentri vengono creati dal metodo makeAlgorithm
      String algorithmProperty=readProperty("Algorithm");
      Algorithm algorithm = makeAlgorithm(algorithmProperty);
      run.setAlgorithm(algorithm);
      
      //Algoritmo di generazione soluzioni iniziali
      String startSolutionMethodProperty= readProperty("StartSolutionMethod");
      //non è implementato un metodo per importare tante soluzioni ed associarle al problema corretto
      if (startSolutionMethodProperty.equals("File") && problems.length>1)
      	throw new Error("Errore: è stata richiesta l'importazione da file della soluzione iniziale, ma non è attualmente possibile importare soluzioni per problemi multipli. Termino.");
      
      SolutionGenerator solutionGenerator= makeSolutionGenerator(startSolutionMethodProperty);
      if(solutionGenerator==null && !algorithmProperty.equals("CPLEX"))
      	throw new Error("Errore: è stato richiesto di non creare un generatore di soluzioni iniziali, ma l'algoritmo scelto lo richiede. Per favore, controlla il file di input. Termino.");
      run.setSolutionGenerator(solutionGenerator);
      
      //Opzioni varie
      String exportSolutionsProperty = readProperty("ExportSolutions");
      if (exportSolutionsProperty.equals("true")){
      	run.setExportSolutionsOption(true);
      	String exportSolutionsPathProperty = readProperty("ExportSolutions.Folder");
      	run.setExportPath(exportSolutionsPathProperty);
      	String exportSolutionSuffix=readProperty("ExportSolution.FileSuffix");
      	if (exportSolutionSuffix!=null)
      		run.setExportSuffix(exportSolutionSuffix);
      	else
      		run.setExportSuffix("");
      	if (exportSolutionsPathProperty==null)
      		System.err.println(generateWarningMessage("ExportSolutions.Folder", DEFAULT_EXPORT_FOLDER));
      }
      
		return run;
	}


	private String readProperty(String property) {
		String value = properties.getProperty(property);
		if (value!=null)
			return value.trim();
		else
			return null;
	}

	
	private Algorithm makeAlgorithm(String algorithmProperty) {
		if(algorithmProperty.equals("LocalSearch"))
			return makeLocalSearch();
		else if(algorithmProperty.equals("VNS"))
			return makeVNS();
		else if(algorithmProperty.equals("CPLEX")) 
			return makeCPLEX();
		else {
			System.err.println(generateWarningMessage("Algorithm", DEFAULT_ALGORITHM));
			return makeAlgorithm(DEFAULT_ALGORITHM);
		}
	}

	private Algorithm makeCPLEX() {
		return new Cplex(null);
	}

	private Algorithm makeVNS() {
		String afterShakingProperty=readProperty("VNS.AfterShaking");
		Algorithm afterShaking;
		if(afterShakingProperty.equals("LocalSearch"))
			afterShaking=makeLocalSearch();
		else {
			System.err.println(generateWarningMessage("VNS.AfterShaking", DEFAULT_AFTERSHAKING_ALGORITHM));
			afterShaking = makeAlgorithm(DEFAULT_AFTERSHAKING_ALGORITHM);
		}
		
		String neighbourGeneratorProperty=readProperty("VNS.NeighbourGenerator");
		NeighbourGenerator nGenerator=makeNeighbourGenerator(neighbourGeneratorProperty);
		
		String kMaxProperty=readProperty("VNS.kMax");
		int kMax;
		if(kMaxProperty==null) {
			System.err.println(generateWarningMessage("VNS.kMax", Integer.toString(DEFAULT_KMAX)));
			kMax=DEFAULT_KMAX;
		} else
			kMax=Integer.parseInt(kMaxProperty);
		
		VNS vns=new VNS(kMax, afterShaking, nGenerator, null);
		
		String maximumTimeProperty=readProperty("VNS.MaximumTime");
		if(maximumTimeProperty!=null) {
			vns.setFinalTime(System.currentTimeMillis()+Integer.parseInt(maximumTimeProperty)*1000);
		}
		return vns;
	}

	
	
	private NeighbourGenerator makeNeighbourGenerator(String neighbourGeneratorProperty) {
		//BasicNeighbourGenerator è l'unico implementato
			return new BasicNeighbourGenerator(null);
	}


	private Algorithm makeLocalSearch() {
		String localSearchTypeProperty=readProperty("LocalSearch.Type");
		SuccessorChoiceMethod method;
		if(localSearchTypeProperty.equals("best"))
			method=SuccessorChoiceMethod.BEST_IMPROVEMENT;
		else if(localSearchTypeProperty.equals("first"))
			method=SuccessorChoiceMethod.FIRST_IMPROVEMENT;
		else {
			System.err.println(generateWarningMessage("LocalSearch.Type", DEFAULT_TYPE.toString()));
			method=DEFAULT_TYPE;
		}
		
		String maxNeighbourNumberProperty=readProperty("LocalSearch.MaxNeighbourNumber");
		int maxNeighbourNumber;
		if (maxNeighbourNumberProperty==null) {
			System.err.println(generateWarningMessage("LocalSearch.MaxNeighbourNumber", Integer.toString(DEFAULT_MAX_NEIGHBOUR_NUMBER)));
			maxNeighbourNumber= DEFAULT_MAX_NEIGHBOUR_NUMBER;
		} else
			maxNeighbourNumber=Integer.parseInt(maxNeighbourNumberProperty);
		
		
		String maxStepNumberProperty=readProperty("LocalSearch.MaxStepNumber");
		int maxStepNumber;
		if (maxNeighbourNumberProperty==null) {
			System.err.println(generateWarningMessage("LocalSearch.MaxStepNumber", Integer.toString(DEFAULT_MAX_STEP_NUMBER)));
			maxStepNumber= DEFAULT_MAX_STEP_NUMBER;
		} else
			maxStepNumber=Integer.parseInt(maxStepNumberProperty);
		
		String neighbourGeneratorProperty=readProperty("LocalSearch.NeighbourGenerator");
		NeighbourGenerator nGenerator=makeNeighbourGenerator(neighbourGeneratorProperty);
		
		return new LocalSearch(maxNeighbourNumber, maxStepNumber, method, nGenerator, null);
	}

	
	
	private SolutionGenerator makeSolutionGenerator(String startSolutionMethodProperty) {
		if(startSolutionMethodProperty.equals("Trivial"))
			return new TrivialSolutionGenerator();
		else if (startSolutionMethodProperty.equals("Lines"))
			return new LinesSolutionGenerator();
		else if (startSolutionMethodProperty.equals("Random"))
			return new RandomSolutionGenerator();
		else if (startSolutionMethodProperty.equals("File"))
			return makeSolutionGeneratorFromFile();
		else if (startSolutionMethodProperty.equals("Null"))
			return null;
		else {
			System.err.println(generateWarningMessage("StartSolutionMethod", DEFAULT_SOLUTION_GENERATOR));
			return makeSolutionGenerator(DEFAULT_SOLUTION_GENERATOR);
		}
	}

	
	
	private SolutionGenerator makeSolutionGeneratorFromFile() {
		FileSolutionGenerator generator=new FileSolutionGenerator();
		String importFile=readProperty("StartSolutionMethod.ImportSolutionFile");
		if (importFile==null) {
			throw new Error("Errore: è stata richiesta l'importazione da file della soluzione iniziale, ma non è stato specificato un file. Termino.");
		}
		generator.setImportFile(importFile);
		return generator;
	}


	private Problem[] makeProblems(String folderPath, String fileListString) {
		if (folderPath==null) {
			System.err.println(generateWarningMessage("Folder", DEFAULT_FOLDER));
		}
		File folder = new File(folderPath);
		String[] fileList = fileNameList(fileListString, folder);
		return parseProblemFiles(fileList,folderPath);
	}


	private String[] fileNameList(String fileListString, File folder) throws Error {
		String fileList[];
		if (fileListString==null) {
			throw new Error("Errore: Non sono stati specificati file. Termino.");
		} else if (fileListString.equals("*")) {
			fileList = folder.list();
		} else {
			fileList=fileListString.split(",[ \t\n\f\r]*");
		}
		return fileList;
	}

	
	
	private Problem[] parseProblemFiles(String[] fileList, String folderProperty) {
		int numProblems = fileList.length;
		ProblemParser parser= new ProblemParser(folderProperty);
		Problem [] problems=new Problem [numProblems];
		for(int i = 0; i < numProblems; i++) {
			problems[i] = parser.parse(fileList[i]);
		}
		return problems;
	}


	
	private String generateWarningMessage(String parameter, String defaultValue) {
		return "Warning: Il parametro specificato per " + parameter + " è assente o non valido. Uso il default " + defaultValue;
	}
}

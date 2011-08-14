package solvingalgorithm;

import data.Problem;
import data.Solution;
import data.Supplier;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;

public class Cplex extends Algorithm {

	public Cplex(Problem problem) {
		this.problem = problem;
	}

	/*
	 * startSolution viene ignorato perché Cplex non ne ha bisogno
	 */
	/**
	 * Risolve il problema usando il risolutore esatto CPLEX. Per eseguire questo
	 * metodo è necessario avere installato le librerie CPLEX e passare alla macchina
	 * virtuale Java i parametri necessari secondo la documentazione delle librerie
	 * CPLEX.<br>
	 * Nella sua implementazione attuale questo algoritmo non usa il parametro startSolution
	 * durante l'esecuzione. Tale parametro è stato comunque lasciato per eventuali sviluppi
	 * futuri. 
	 * 
	 */
	@Override
	public Solution execute(Solution startSolution){
		
		Solution solution;
		try {
         //Create the modeler/solver object
         IloCplex cplex = new IloCplex();
         /*
          * definisco le variabili del problema
          */
         IloIntVar zik[][] = new IloIntVar[problem.getDimension()][problem.getNumProducts()];
         IloIntVar zikr[][][] = new IloIntVar[problem.getDimension()][][];
         IloIntVar yir[][] = new IloIntVar[problem.getDimension()][];
         
         setModelVariables(cplex, zik, zikr, yir);
         
         setDemandConstraints(cplex, zik);
         
         setSegmentSumConstraint(cplex, zik, zikr);
         
         setSegmentsBoundsConstraints(cplex, zikr, yir);
         
         setSingleActiveSegmentConstraint(cplex, yir);
         
         //solve the model and display the solution if one was found
         if (cplex.solve()){
        	 
            cplex.output().println("Solution status = " + cplex.getStatus());
            cplex.output().println("Solution value  = " + cplex.getObjValue());
            
            
            solution = createSolutionObject(cplex, zik);
         }else{
         	solution = null;
         }
         cplex.end();
         return solution;
      }
      catch (IloException e) {
         System.err.println("Concert exception '" + e + "' caught");
         return null;
      }
	}

	/**
	 * Questo metodo converte il formato di soluzione di CPLEX in un oggetto Solution.
	 * @param cplex
	 * @param zik
	 * @return un oggetto di tipo Solution corrispondente alla matrice {@code zik}.
	 * @throws UnknownObjectException
	 * @throws IloException
	 */
	private Solution createSolutionObject(IloCplex cplex, IloIntVar[][] zik)
			throws UnknownObjectException, IloException {
		Solution solution;
		int solutionMatrix[][] = new int [problem.getDimension()+1][problem.getNumProducts()+1];
		//la riga e la colonna 0-esime non sono utilizzate
		for(int i=0; i<=problem.getDimension(); i++){
			for(int k=0; k<=problem.getNumProducts(); k++){
				if(i==0 || k==0){
					solutionMatrix[i][k] = 0;
				}
			}
		}
		for(int i=0; i<problem.getDimension(); i++){
			for(int k=0; k<problem.getNumProducts(); k++){
				solutionMatrix[i+1][k+1] = (int)Math.floor(cplex.getValue(zik[i][k]) + 0.5d);
			}
		}
		solution = new Solution(solutionMatrix, problem);
		return solution;
	}

	/**
	 * Impone il vincolo che per ogni fornitore ci sia una sola fascia di sconto attiva.
	 * @param cplex
	 * @param yir
	 * @throws IloException
	 */
	private void setSingleActiveSegmentConstraint(IloCplex cplex,
			IloIntVar[][] yir) throws IloException {
		/*
          * vincolo (6)
          */
         for(int i=0; i<problem.getDimension(); i++){
         	int numSegments = problem.getSupplier(i+1).getNumSegments();
         	IloLinearNumExpr summation = cplex.linearNumExpr();
         	for(int r=0; r<=numSegments; r++){
         		summation.addTerm(1, yir[i][r]);
         	}
         	cplex.addLe(summation, 1);
         }
	}

	/**
	 * Definisce gli upper bound e i lower bound delle fasce di sconto.
	 * @param cplex
	 * @param zikr
	 * @param yir
	 * @throws IloException
	 */
	private void setSegmentsBoundsConstraints(IloCplex cplex,
			IloIntVar[][][] zikr, IloIntVar[][] yir) throws IloException {
		/*
          * vincolo (5)
          */
         int lir[][] = new int[problem.getDimension()][];
         int uir[][] = new int[problem.getDimension()][];
         for(int i=0; i<problem.getDimension(); i++){ 
         	Supplier supplier = problem.getSupplier(i+1);
				int numSegments = supplier.getNumSegments();
				lir[i] = supplier.getLowerBounds();
				uir[i] = new int[numSegments+1];
				for(int r=0; r<numSegments; r++){
         		uir[i][r] = lir[i][r+1]-1;
         	}
				uir[i][numSegments] = Integer.MAX_VALUE; 
         	
				for(int r = 0; r <=numSegments ; r++){
					IloLinearNumExpr summation = cplex.linearNumExpr();
					for(int k=0; k < problem.getNumProducts(); k++) {
						summation.addTerm(1, zikr[i][k][r]);
					}
					cplex.addGe(summation, cplex.prod(lir[i][r], yir[i][r]));
					cplex.addLe(summation, cplex.prod(uir[i][r], yir[i][r]));
				}
         }
	}

	/**
	 * Definisce che la somma degli acquisti in ciascuna fascia di un fornitore sia pari al
	 * totale di tutti gli acquisti presso il fornitore stesso.
	 * @param cplex
	 * @param zik
	 * @param zikr
	 * @throws IloException
	 */
	private void setSegmentSumConstraint(IloCplex cplex, IloIntVar[][] zik,
			IloIntVar[][][] zikr) throws IloException {
		/*
          * vincolo (4)
          */
         for(int i=0; i<problem.getDimension(); i++){
         	int numSegments = problem.getSupplier(i+1).getNumSegments();
         	for(int k=0; k<problem.getNumProducts(); k++){
         		IloLinearNumExpr summation = cplex.linearNumExpr();
         		for(int r=0; r<=numSegments; r++){
         			summation.addTerm(1, zikr[i][k][r]);
         		}
         		cplex.addEq(summation, zik[i][k]);
         	}
         }
	}

	/**
	 * Definisce i vincoli della domanda.
	 * @param cplex
	 * @param zik
	 * @throws IloException
	 */
	private void setDemandConstraints(IloCplex cplex, IloIntVar[][] zik)
			throws IloException {
		/*
          * vincolo (2) nelle specifiche
          */
         for(int k=0; k<problem.getNumProducts(); k++){
         	 IloLinearNumExpr summation = cplex.linearNumExpr();
         	 for(int i=0; i<problem.getDimension(); i++){
         		 summation.addTerm(1, zik[i][k]);
         	 }
         	 int demand = problem.getProductDemand(k+1);
         	 cplex.addEq(demand, summation);
         }
	}


	/**
	 * 
	 * Crea le variabili e la funzione obiettivo e le aggiunge all'oggetto cplex.
	 * 
	 * @param cplex
	 * @param zik
	 * @param zikr
	 * @param yir
	 * @throws IloException
	 */
	private void setModelVariables(IloCplex cplex, IloIntVar[][] zik,
			IloIntVar[][][] zikr, IloIntVar[][] yir) throws IloException {
		/*
          * Gli indici i, k, r usati in cplex e problem sono diversi:
          *
          * 		cplex							Problem
          * i 		0 -> dimension-1			1 -> dimension
          * k		0 -> numProducts-1		1 -> numProducts
          * r		0 -> numSegments			0 -> numSegments
          */
		/*
		 * Setta anche il vincolo (3) delle specifiche perché è implicito nei
		 * lower e upper bounds delle variabili.
		 */
         //creo le zik
         for(int i=0; i<problem.getDimension(); i++){
         	for(int k = 0; k<problem.getNumProducts(); k++){
         		int lowerBound = 0;
         		int upperBound = problem.getAvailability(i+1, k+1);
         		zik[i][k] = cplex.intVar(lowerBound, upperBound);
         	}
         }
         
         IloLinearNumExpr objFunction = cplex.linearNumExpr(); 
         for(int i=0; i<problem.getDimension(); i++){
         	int numSegments = problem.getNumSegments(i+1);
         	zikr[i]	= new IloIntVar[problem.getNumProducts()][numSegments+1];
         	yir[i]	= new IloIntVar[numSegments+1];
         	
         	//creazione zikr e funzione obiettivo (1)
         	for(int k = 0; k<problem.getNumProducts(); k++){
         		for(int r=0; r<=numSegments; r++){
         			zikr[i][k][r] = cplex.intVar(0, Integer.MAX_VALUE);
         			
         			double price = problem.getSupplier(i+1).getPrice(k+1, r);
         			objFunction.addTerm(price, zikr[i][k][r]);
         		}
         	}
         	
         	//creazione yir
         	for(int r=0; r<=numSegments; r++){
         		yir[i][r] = cplex.intVar(0, 1);
         	}
         }
         cplex.addMinimize(objFunction);
	}
}

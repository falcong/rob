package solvingalgorithms;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import rob.Problem;
import rob.Solution;
import rob.Supplier;

public class Cplex extends Algorithm {

	public Cplex(Problem problem) {
		this.problem = problem;
	}
	
	public Cplex(){
	}

	/*
	 * startSolution viene ignorato perché Cplex non ne ha bisogno
	 */
	@Override
	public Solution execute(Solution startSolution){
		
		Solution solution;
		try {
         //Create the modeler/solver object
         IloCplex cplex = new IloCplex();
         
         int dimension 		= problem.getDimension();
         int numProducts	= problem.getNumProducts();
         /*
          * definisco le variabili del problema
          */
         IloIntVar zik[][] = new IloIntVar[dimension][numProducts];
         IloIntVar zikr[][][] = new IloIntVar[dimension][][];
         IloIntVar yir[][] = new IloIntVar[dimension][];
         
         /*
          * Gli indici i, k, r usati in cplex e problem sono diversi:
          *
          * 		cplex							Problem
          * i 		0 -> dimension-1			1 -> dimension
          * k		0 -> numProducts-1		1 -> numProducts
          * r		0 -> numSegments			0 -> numSegments
          */
         
         //creo le zik
         for(int i=0; i<dimension; i++){
         	for(int k = 0; k<numProducts; k++){
         		int lowerBound = 0;
         		int upperBound = problem.getAvailability(i+1, k+1);
         		zik[i][k] = cplex.intVar(lowerBound, upperBound);
         	}
         }
         
         IloLinearNumExpr objFunction = cplex.linearNumExpr(); 
         for(int i=0; i<dimension; i++){
         	int numSegments = problem.getNumSegments(i+1);
         	zikr[i]	= new IloIntVar[numProducts][numSegments+1];
         	yir[i]	= new IloIntVar[numSegments+1];
         	
         	//creazione zikr e funzione obiettivo (1)
         	for(int k = 0; k<numProducts; k++){
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
         
         /*
          * vincolo (2)
          */
         for(int k=0; k<numProducts; k++){
         	 IloLinearNumExpr summation = cplex.linearNumExpr();
         	 for(int i=0; i<dimension; i++){
         		 summation.addTerm(1, zik[i][k]);
         	 }
         	 int demand = problem.getProductDemand(k+1);
         	 cplex.addEq(demand, summation);
         }
         
         /*
          * vincolo (3): già fatto con gli upper bound di zik 
          */
         
         /*
          * vincolo (4)
          */
         for(int i=0; i<dimension; i++){
         	int numSegments = problem.getSupplier(i+1).getNumSegments();
         	for(int k=0; k<numProducts; k++){
         		IloLinearNumExpr summation = cplex.linearNumExpr();
         		for(int r=0; r<=numSegments; r++){
         			summation.addTerm(1, zikr[i][k][r]);
         		}
         		cplex.addEq(summation, zik[i][k]);
         	}
         }
         
         /*
          * vincolo (5)
          */
         int lir[][] = new int[dimension][];
         int uir[][] = new int[dimension][];
         for(int i=0; i<dimension; i++){ 
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
					for(int k=0; k < numProducts; k++) {
						summation.addTerm(1, zikr[i][k][r]);
					}
					cplex.addGe(summation, cplex.prod(lir[i][r], yir[i][r]));
					cplex.addLe(summation, cplex.prod(uir[i][r], yir[i][r]));
				}
         }
         
         /*
          * vincolo (6)
          */
         for(int i=0; i<dimension; i++){
         	int numSegments = problem.getSupplier(i+1).getNumSegments();
         	IloLinearNumExpr summation = cplex.linearNumExpr();
         	for(int r=0; r<=numSegments; r++){
         		summation.addTerm(1, yir[i][r]);
         	}
         	cplex.addLe(summation, 1);
         }
         
         //write model to file
         //cplex.exportModel("problem.lp");
       
         //solve the model and display the solution if one was found
         if (cplex.solve()){
            //double[] solutionX     = cplex.getValues(var[0]);
          
            cplex.output().println("Solution status = " + cplex.getStatus());
            cplex.output().println("Solution value  = " + cplex.getObjValue());
            
            int solutionMatrix[][] = new int [dimension+1][numProducts+1];
            //la riga e la colonna 0-esime non sono utilizzate
            for(int i=0; i<=dimension; i++){
            	for(int k=0; k<=numProducts; k++){
            		if(i==0 || k==0){
            			solutionMatrix[i][k] = 0;
            		}
            	}
            }
            for(int i=0; i<dimension; i++){
            	for(int k=0; k<numProducts; k++){
            		solutionMatrix[i+1][k+1] = (int)Math.floor(cplex.getValue(zik[i][k]) + 0.5d);
            	}
            }
            solution = new Solution(solutionMatrix, problem);
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

	@Override
	public void setProblem(Problem problem) {
		this.problem=problem;
		
	}

	//non usato
	@Override
	public void setFinalTime(long finalTime) {
		// TODO Auto-generated method stub
		
	}
}

package io;

public abstract class Printer {
	public abstract void print(Object object);
	
	/*
	 * stampa la matrice che riceve in ingresso bidimensionale
	 * x e y indicano come nominare le righe e le colonne
	 * i e j indicano da quale riga e colonna, rispettivamente, partire
	 * numTab1	numero tab prima riga intestazione [2]
	 * numTab2 	numero tab fra elementi riga intestazione [3] 
	 * numTab3	numero tab inizio ogni riga matrice [2]
	 * numTab4	numero tab fra gli elementi di ogni riga della matrice [2]
	 */
	protected static void printMatrix2D(double matrix[][], String x, String y,
			int i, int j, int numTab1, int numTab2, int numTab3, int numTab4){
		int numRows		= matrix.length; 
		int numColumns	= matrix[0].length;
		
		//intestazione tabella [y-j y-j+1 y-j+2 ...]
		for(int a=j; a<=numColumns-1; a++){
			//la prima volta numTab1 tab poi numTab2
			if(a==j){
				for(int c=1; c<=numTab1; c++){
					System.out.print("\t");
				}
			}else{
				for(int c=1; c<=numTab2; c++){
					System.out.print("\t");
				}
			}
			System.out.print(y+a);
		}
		System.out.println("\n");
		
		//stampo le righe
		for(int a=i; a<=numRows-1; a++){
			//intestazione 
			System.out.print(x+a);
			for(int b=j; b<=numColumns-1; b++){
				//la prima volta numTab3 tab poi numTab4
				if(b==j){
					for(int c=1; c<=numTab3; c++){
						System.out.print("\t");
					}
				}else{
					for(int c=1; c<=numTab4; c++){
						System.out.print("\t");
					}
				}
				System.out.print("("+a+","+b+")="+matrix[a][b]);
			}
			System.out.println("");
		}
		System.out.println("");
	}
}

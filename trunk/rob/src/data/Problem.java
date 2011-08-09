package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import comparator.BoughtQuantityComparator;
import comparator.CurrentPriceComparator;


import data.Supplier;


public class Problem {
	private String name;
	private String type;
	//-1 vuol dire classe non specificata
	private int problemClass;
	//numero dei fornitori
	private int dimension;
	/*
	 * numero massimo degli intervalli di sconto
	 * [viene conteggiata anche la fascia 0-esima in cui non è applicato alcuno sconto]
	 * se vale -1 vuol dire che non è stato specificato
	 */
	private int maxNRange;
	private int numProducts;
	/*
	 *  vettore contenente la domanda per i vari prodotti; demand[k]=domanda del 
	 *  prodotto k-esimo; demand[0] non è usato quindi demand.length=numProducts+1
	 */
	private int demand [];
	/*
	 * numero totale di prodotti che demand mi chiede di acquistare
	 */
	private int totalDemand;
	/*
	 * contiene i fornitori; suppliers[0] non è usato
	 */
	private Supplier suppliers[];
	
	
	public Problem(String name, String type, int problemClass, int maxNRange,
		int demand [], Supplier suppliers[]){
		this.name 			= name;
		this.type 			= type;
		this.problemClass	= problemClass;
		dimension			= suppliers.length-1;
		this.maxNRange		= maxNRange;
		numProducts			= demand.length-1;
		this.demand 		= demand;//
		totalDemand = 0;
		for(int p=1; p<=numProducts; p++){
			totalDemand += demand[p];
		}
		this.suppliers			= suppliers;

	}

	public String getName(){
		return name;
	}

	/*
	 * Restituisce un fornitore scelto a caso non contenuto in suppBlacklist
	 */
	public Supplier getRandomSupplier(HashSet<Integer> suppBlacklist) {
		int id = getRandomSupplierId(suppBlacklist);
		if(id==0)
			return null;
		return getSuppliers()[id];
	}

	public int getRandomSupplierId(HashSet<Integer> suppBlacklist) {
		int id;
		if (suppBlacklist.size()>=getDimension())
			return 0;
		boolean ok=false;
		do{
			id=1+(int)(Math.random()*getDimension());
			if (suppBlacklist.contains(id))
				continue;
			else
				ok=true;
		} while(!ok);
		return id;
	}
	
	/*
	 * Metodo overloaded che non richiede in ingresso una blacklist e genera automaticamente una blacklist vuota
	 */
	//TODO vedere le dipendenze con altri 2
	public Supplier getRandomSupplier() {
		HashSet<Integer> blacklist = new HashSet<Integer>();
		return getRandomSupplier(blacklist);
	}
	
	/*
	 * Metodo che estrae un Supplier diverso da quello dato in ingresso
	 */
/*	public Supplier getRandomSupplierDifferentFrom(Supplier exclude) {
		HashSet<Integer> blacklist = new HashSet<Integer>();
		blacklist.add(exclude.getId());
		return getRandomSupplier(blacklist);
	}*/


/*	public void setDimension(int dimension) {
		this.dimension = dimension;
	}*/

	public int getDimension() {
		return dimension;
	}

/*	public void setNumProducts(int numProducts) {
		this.numProducts = numProducts;
	}*/

	public int getNumProducts() {
		return numProducts;
	}

/*	public void setDemand(int demand[]) {
		this.demand = demand;
	}*/

	public int[] getDemand() {
		return demand;
	}
	
	public int getProductDemand(int product){
		return demand[product];
	}

/*	public void setSuppliers(Supplier suppliers[]) {
		this.suppliers = suppliers;
	}*/

	public Supplier[] getSuppliers() {
		return suppliers;
	}

	public Supplier getSupplier(int id) {
		return suppliers[id];
	}

	/*
	 * restituisce la disponibilità di product presso il fornitore supplier
	 */
	public int getAvailability(int supplier, int product) { 
		int availability = suppliers[supplier].getAvailability(product);
		if(availability==-1){
			return 0;
		}else{
			return availability;
		}
	}
	
	/*
	 * restituisce il numero di fasce di sconto del fornitore supplierId
	 * [la fascia 0-esima non viene contata]
	 */
	public int getNumSegments(int supplierId) {
		return suppliers[supplierId].getNumSegments();
	}

	public int getProblemClass() {
		return problemClass;
	}
	
	/*
	 * restituisce la massima quantità di prodotti acquistabili presso un fornitore
	 */
	public int getMaxQuantityBuyable(int supplier){
		int sum = 0;
		for(int p=1; p<=numProducts; p++){
			int availability = suppliers[supplier].getAvailability(p);
			if(availability==-1){
				availability=0;
			}
			
			sum += Math.min(demand[p], availability);
		}
		
		return sum;
	}
	
	/*
	 * Usato solo dal testing.
	 */
	public int getTotalDeman(){
		return totalDemand;
	}
	
	//restituisce la massima fascia di sconto attivabile di supplier
	public int maxSegmentActivable(int supplier){
		return suppliers[supplier].activatedSegment(getMaxQuantityBuyable(supplier));
	}
	
	/*
	 * Precondizione: cell contiene almeno 1 prodotto acquistato.
	 * Restituisce true se almeno 1 prodotto acquistato in cell è spostabile presso un altro fornitore.
	 * 
	 * C 				= insieme di tutte le celle della stessa colonna di cell escluso cell
	 * C_vietate		= intersezione(C; otherCellsToEmpty)
	 * C_riempibili		= C\C_vietate
	 * Q_tot_vietato	= quantità totale di prodotti comprati presso le C_vietate
	 * D_effettiva		= (disponibilità residua totale delle C_riempibili)-Q_tot_vietato
	 * cell per essere svuotata può utilizzare solamente D_effettiva
	 */
	public boolean cellIsEmptiable(int cell, Solution solution, ArrayList<Integer> otherCellsToEmpty){
		int product=getProductFromCell(cell);
		//int supplierId= getSupplierFromCell(cell);
		//somma disponibilità
		int sumResidualAvailability=0;
		for (int i=1;i<=dimension;i++){
			int cell2 = getCell(i,product);
			if (cell2==cell)
				continue;
			if (otherCellsToEmpty.contains(cell2))
				sumResidualAvailability-=solution.getQuantity(i, product);
			else
				sumResidualAvailability+=getSupplier(i).getResidual(product, solution);
		}
		if (sumResidualAvailability>0)
			return true;
		else 
			return false;
	}
	
	public int getSupplierFromCell (int cell) {
		return (cell-getProductFromCell(cell))/numProducts+1;
	}
	
	public int getProductFromCell (int cell) {
		return (cell-1)%numProducts+1;
	}
	
	public int getCell(int supplier,int product){
		return (supplier-1)*numProducts+product;
	}
	
	public Supplier[] sortByCurrentPrice(int product, Solution solution) {
		Supplier[] suppliersCopy=suppliers.clone();
		CurrentPriceComparator comparator= new CurrentPriceComparator(solution,product);
		Arrays.sort(suppliersCopy, 1, dimension, comparator);
		return suppliersCopy;
	}
	
	public Supplier[] sortByBoughtQuantity(Solution solution) {
		Supplier[] suppliersCopy=suppliers.clone();
		BoughtQuantityComparator comparator= new BoughtQuantityComparator(solution);
		//ordino l'array in base alle quantità totali acquistate in senso crescente
		Arrays.sort(suppliersCopy, 1, dimension+1, comparator);
		//inverto l'ordine dell'array per avere l'ordine decrescente
		Supplier result[] = new Supplier[dimension+1];
		result[0] = null;
		for(int i=1; i<=dimension; i++){
			result[i] = suppliersCopy[dimension-i+1];
		}
		return result;
	}
}

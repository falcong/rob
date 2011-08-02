package io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


import rob.Problem;
import rob.Supplier;
import util.Utility;

public class ProblemParser extends Parser{
	int numProducts;
	String file;
	String problemsPath;
	
	//TODO eventualmente spostare dove sono usate
	final int NOT_FOUND = -1;
	final char COMMENT_DELIMITER = '#';
	final String DEMAND_SECTION_DELIMITER = "DEMAND_SECTION";
	final String OFFER_SECTION_DELIMITER = "OFFER_SECTION";
	
	public ProblemParser(String path) {
		problemsPath = path;
	}

	/*
	 * Crea Problem descritto nel file problemFile [formato di Manerba].
	 */
	public Problem parse(String problemFile) throws Exception{
		file = problemsPath + File.separator + problemFile;
		
		//leggo da file i vari valori
		//NAME
		final String ATTRIBUTE = "NAME";
		String defaultStringValue = "senza nome";
		String name = readAttribute(ATTRIBUTE, defaultStringValue);
		
		//TYPE
		final String TYPE = "TYPE";
		defaultStringValue = "non specificato";
		String type = readAttribute(TYPE, defaultStringValue);
			
		//CLASS
		final String CLASS = "CLASS";
		int defaultIntValue = -1;
		int problemClass = Integer.parseInt(readAttribute(CLASS, Integer.toString(defaultIntValue)));
		
		//DIMENSION
		final String DIMENSION = "DIMENSION";
		int dimension = readMandatoryAttribute(DIMENSION);
		
		//MAX_N_RANGE
		final String MAX_N_RANGE = "MAX_N_RANGE";
		int maxNRange = readAttribute(MAX_N_RANGE, -1);
		
		//leggo DEMAND_SECTION
		int demand[] = readDemandSection();
		
		//creo array con suppliers
		Supplier suppliers[] = makeSuppliers(dimension);
			
		return new Problem(name, type, problemClass, maxNRange, demand, suppliers);
	}

	
	/*
	 * legge il valore di attribute come String;
	 * attribute può essere "NAME", "TYPE", "CLASS", "DIMENSION", "MAX_N_RANGE";
	 * (restituisce null se l'attributo non viene trovato)
	 */
	private String readAttributeFromFile(String attribute) throws Exception{
		String value = null;
		BufferedReader bufferedReader = Utility.openInFile(file);
		
		int tagLength = attribute.length();
		String line;
		boolean found = false;
		while((line = bufferedReader.readLine()) != null	&&	!found){
			if(line.length()>=tagLength){
				String temp=line.substring(0,tagLength);
				if(temp.equals(attribute)){
					found = true;
					String lineElements[] = line.split("\\s[\\s]*");
					value=lineElements[2];
				}
			}
		}
		bufferedReader.close();
	
		return value;
	}
	
	/*
	 * Legge dal file contenente la descrizione del problema la domanda dei vari prodotti
	 * e la restituisce; lancia una eccezione se DEMAND_SECTION non viene trovata.
	 */
	private int[] readDemandSection() throws Exception{
		int demand[] = null;
		BufferedReader inputFile = Utility.openInFile(file);
		String line;
		String lineElements[];
		
		while((line = inputFile.readLine()) != null){
			final String SEPARATOR = "\\s[\\s]*";
			lineElements = line.split(SEPARATOR);
			
			if(line.isEmpty()){
				//ignoro eventuali linee vuote
				;
			}else if(lineElements[0].charAt(0) == COMMENT_DELIMITER){
				//ignoro eventuali commenti
				;
			}else if(lineElements[0].equals(DEMAND_SECTION_DELIMITER)){
				//leggo la prima riga che indica il numero di prodotti
				line = inputFile.readLine();
				numProducts = Integer.parseInt(line);
				
				//leggo le altre righe (con la domanda di ogni prodotto), non viene utilizzato demand[0]  
				demand = new int[numProducts+1];
				//demand[k]=NOT_FOUND significa che per il prodotto k la domanda non è stata specificata
				Arrays.fill(demand, NOT_FOUND);
				//vale true quando raggiungo la fine DEMAND_SECTION
				boolean end = false;
				while(!end){
					line = inputFile.readLine();
					lineElements = line.split("\\s[\\s]*");
					
					if(lineElements[0].equals(OFFER_SECTION_DELIMITER)){
						end = true;
					}else{
						//id del prodotto
						int productId = Integer.parseInt(lineElements[0]);
						if(productId<1 || productId>numProducts){
							Utility.exception("In DEMAND_SECTION del file "+file+" il prodotto "+productId+" non è accettabile");
						}
						//domanda
						int productDemand = Integer.parseInt(lineElements[1]);
						demand[productId] = productDemand;
					}
				}
				/*
				 * Controllo che siano state specificate tutte le domande; per i prodotti la cui domanda non è stata 
				 * specificata assegno domanda pari a 0
				 */
				for(int i=1; i<=demand.length-1; i++){
					if(demand[i]==NOT_FOUND){
						final int DEFAULT_VALUE = 0;
						Utility.warning("In DEMAND_SECTION del file "+file+" la domanda del prodotto "+i+" non è stata specificata"+
								"Ad essa verrà assegnato il valore di default "+DEFAULT_VALUE+".\n");
						demand[i]=DEFAULT_VALUE;
					}
				}
			}
		}
		inputFile.close();
		if(demand==null){
			//lancio 1 eccezione perchè non ho trovato DEMAND_SECTION
			Utility.exception("Non è stata trovata la sezione DEMAND_SECTION.");
		}
		return demand;
	}
	
	
	/*
	 * legge il file contenente la descrizione del problema e crea l'array con i supplier
	 */
	//TODO spezzare
	private Supplier[] makeSuppliers(int numSuppliers) throws Exception{
		Supplier suppliers[] = null;
		
		BufferedReader bufferedReader = Utility.openInFile(file);
		
		String line;
		String lineElements[];
		
		//mi porto alla riga OFFER_SECTION
		String tag = "OFFER_SECTION";
		//vale true quando ho raggiunto OFFER_SECTION
		boolean end = false;
		while(!end	&&	(line = bufferedReader.readLine()) != null){
			if(line.length() >= tag.length()){
				line = line.substring(0,tag.length());
				if(line.equals(tag)){
					end = true;
				}
			}
		}
		//non ho trovato OFFER_SECTION dunque interrompo il programma
		if(!end){
			/*
			 * risolve fallimento di ProblemParser.parse2
			 */
			if(suppliers==null){
				throw new Error("Nel file di input del problema mancano i fornitori. Il programma verrà terminato");
			}
		}
		
		//line=OFFER_SECTION
		//leggo tuttte le righe di OFFER_SECTION
		tag = "DISCOUNT_SECTION";
		int tagLength = tag.length();
		//String temp;
		//suppliers[0] non è usato
		suppliers = new Supplier[numSuppliers+1];
		for(int i=0; i<=suppliers.length-1; i++){
			suppliers[i]=null;
		}
		int supplierId;
		Supplier supplier;
		//end = true quando raggiungo la fine di OFFER_SECTION
		//[raggiungo la riga DISCOUNT_SECTION]
		end = false;
		while(!end){
			line = bufferedReader.readLine();
			
			if(line.length()>=tagLength	&&	line.substring(0, tagLength).equals(tag)){
				end = true;	
			}else{
				lineElements = line.split("\\s[\\s]*");
				
				supplierId = Integer.parseInt(lineElements[0]);
				if(supplierId<1 || supplierId>numSuppliers){
					System.out.println("Errore:");
					System.out.println("Nella sezione OFFER_SECTION del file "+file+" il supplier "+supplierId+" non è consentito");
					System.out.println("Il programma verrà terminato");
					System.exit(1);
				}
				supplier = new Supplier(supplierId);
				
				int numOfferedProducts = Integer.parseInt(lineElements[1]);
				supplier.setNumOfferedProducts(numOfferedProducts);
				
				/*
				 * contiene i prodotti offerti da supplierId con il relativo costo e disponibilità
				 * offert[0]=id-prodotto1;	offert[1]=costo-p1;	offert[2]=disponibilità-p1;
				 * offert[3]=id-p2;			offert[4]=costo-p2;	offert[5]=disponibilità-p2;
				 * ...
				 */
				int offert[] = new int[lineElements.length-2];
				for(int i=0; i<=offert.length-1; i++){
					offert[i] = Integer.parseInt(lineElements[i+2]);
				}
				
				setOffer(supplier, offert, numProducts);
				suppliers[supplierId] = supplier;
			}
		}
		//ho finito di analizzare OFFER_SECTION
		//controllo che siano stati forniti tutti i fornitori
		for(int i=1; i<=suppliers.length-1; i++){
			if(suppliers[i]==null){
				System.out.println("Errore:");
				System.out.println("In OFFER_SECTION non è stato specificato il fornitore "+i);
				System.out.println("Il programma verrà terminato");
				System.exit(1);
			}
		}
		
		//line=DISCOUNT_SECTION
		//ora leggo tutte le righe di DISCOUNT_SECTION
		tag = "EOF";
		tagLength = tag.length();
		//per controllare che in DISCOUNT_SECTION siano definiti tutti i fornitori
		boolean definedSupplier[]	= new boolean[numSuppliers+1];
		for(int i=0; i<=definedSupplier.length-1; i++){
			definedSupplier[i] = false;
		}
		/*
		 * end = true quando raggiungo la fine di DISCOUNT_SECTION
		 * (=raggiungo la riga EOF)
		 */
		end = false;
		while(!end){
			line = bufferedReader.readLine();
			
			if(line.length()>=tagLength	&&	line.substring(0, tagLength).equals(tag)){
				end = true;
			}else{
				lineElements = line.split("\\s[\\s]*");
				
				supplierId = Integer.parseInt(lineElements[0]);
				if(supplierId<1 || supplierId>numSuppliers){
					System.out.println("Errore:");
					System.out.println("Nella sezione DISCOUNT_SECTION del file "+file+" il supplier "+supplierId+" non è consentito");
					System.out.println("Il programma verrà terminato");
					System.exit(1);
				}
				definedSupplier[supplierId] = true;
				
				//numero fasce di sconto senza contare quella 0 (prezzo base)
				int numBands = Integer.parseInt(lineElements[1]);
				//controllo che siano definiti lower bounds e sconti per ogni fascia
				if(lineElements.length	!=	numBands*2+2){
					System.out.println("Errore:");
					System.out.println("In DISCOUNT_SECTION non sono state specificate correttamente le fasce di sconto per il fornitore "+supplierId);
					System.out.println("Il programma verrà terminato");
					System.exit(1);
				}
				
				//discounts[r] = sconto percentuale della fascia r; discount[0] = 0
				int discounts[]		= new int[numBands+1];
				discounts[0]		= 0;
				
				/*
				 * lowerBounds[r] = lower bound della fascia r di sconto;
				 * lowerBounds[0] = 1
				 */
				int lowerBounds[]	= new int[numBands+1];
				lowerBounds[0]		= 1;
				
				//leggo da lineElements i lower bounds e le percentuali di sconto per le varie fasce
				for(int i=2; i<=lineElements.length-2; i+=2){
					lowerBounds[i/2]		= Integer.parseInt(lineElements[i]);
					discounts[i/2]			= Integer.parseInt(lineElements[i+1]);
				}
				
				suppliers[supplierId].setPrices(numBands, discounts);
				suppliers[supplierId].setLowerBounds(lowerBounds);
			}
		}
		//controllo che in DISCOUNT_SECTION siano stati definiti tutti i fornitori
		for(int i=1; i<=definedSupplier.length-1; i++){
			if(!definedSupplier[i]){
				System.out.println("Errore:");
				System.out.println("In DISCOUNT_SECTION non è stato definito il supplier "+i);
				System.out.println("Il programma verrà terminato");
				System.exit(1);
			}
		}
		
		bufferedReader.close();
		
		return suppliers; 
	}
	
	
	/*
	 * setta in supplier i prezzi base e le disponibilità dei prodotti (contenuti in offert);
	 * numProducts = numero di prodotti totale;
	 * offert[0]=id-prodotto1;	offert[1]=costo-p1;	offert[2]=disponibilità-p1;
	 * offert[3]=id-p2;			offert[4]=costo-p2;	offert[5]=disponibilità-p2;
	 * ...
	 */
	//t
	private void setOffer(Supplier supplier, int offert[], int numProducts){
		int basePrices[]		= new int[numProducts+1];
		int availability[]		= new int[numProducts+1];
		for(int i=0; i<=numProducts; i++){
			basePrices[i]	=-1;
			availability[i]	=-1;
		}
		
		//i = id-prodotto; i+1 = costo; i+2 = disponibilità;
		for(int i=0; i<=offert.length-3; i+=3){
			int productId			= offert[i];
			int basePrice			= offert[i+1];
			int productAvailability	= offert[i+2];
			
			if(productId<1 || productId>numProducts){
				System.out.println("Errore:");
				System.out.println("In OFFER_SECTION di "+file+" il prodotto "+productId+" del fornitore "+supplier.getId()+" non è ammesso");
				System.out.println("Il programma verrà terminato");
				System.exit(1);
			}
			basePrices[productId]	= basePrice;
			availability[productId]	= productAvailability;
		}
		
		//setto i prezzi base
		supplier.setBasePrices(basePrices);
		//setto la disponibilità
		supplier.setAvailability(availability);
		
		return;
	}
	
	//TODO pensare a nome migliore
	/*
	 * Legge il valore di attribute (di tipo String) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * restituisce defaultValue.
	 */
	private String readAttribute(String attribute, String defaultValue) throws Exception{
		String value;
		if((value = readAttributeFromFile(attribute))	==	null){
			value = defaultValue;
			
			Utility.warning("Nel file "+file+" non è stato trovato l'attributo "+attribute+".\n"+
					"Ad esso verrà assegnato il valore "+defaultValue+".\n");
		}	
			
		return value;
	}
	
	/*
	 * Legge il valore di attribute (di tipo int) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * restituisce defaultValue.
	 */
	private int readAttribute(String attribute, int defaultValue) throws Exception{
		return Integer.parseInt((readAttribute(attribute, Integer.toString(defaultValue))));
	}
	
	/*
	 * Legge il valore di attribute (di tipo int) e lo restituisce. Nel caso in cui non dovesse riuscire a leggerlo
	 * lancia un'eccezione.
	 */
	private int readMandatoryAttribute(String attribute) throws Exception{
		int value = readAttribute(attribute, NOT_FOUND);
		if(value==NOT_FOUND){
			Utility.exception("Nel file "+file+" non è stato trovato l'attributo "+attribute);
		}
		return value;
	}
}

package io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import rob.Problem;
import rob.Supplier;

public class ProblemParser extends Parser{
	int numProducts;
	String file;
	String problemsPath;
	
	public ProblemParser(String path) {
		problemsPath = path;
	}

	/*
	 * crea Problem descritto nel file problemFile [formato di Manerba]
	 */
	public Problem parse(String problemFile) throws Exception{
		file = problemsPath + File.separator + problemFile;
		
		//leggo da file i vari valori
		//NAME
		String name;
		if((name = readAttribute("NAME"))	==	null){
			System.err.println("Attenzione:");
			System.err.println("Nel file "+file+" non è stato trovato l'attributo NAME\n");
			name = "senza nome";
		}
		
		//TYPE
		String type;
		if((type = readAttribute("TYPE"))	==	null){
			System.err.println("Attenzione:");
			System.err.println("Nel file "+file+" non è stato trovato l'attributo TYPE\n");
			type = "non specificato";
		}
			
		//CLASS
		String temp;
		int problemClass;
		if((temp = readAttribute("CLASS"))	==	null){
			System.err.println("Attenzione:");
			System.err.println("Nel file "+file+" non è stato trovato l'attributo CLASS\n");
			problemClass = -1;
		}else{
			problemClass = Integer.parseInt(temp);
		}
		
		//DIMENSION
		int dimension=-1;
		if((temp = readAttribute("DIMENSION"))	==	null){
			System.err.println("Attenzione:");
			System.err.println("Nel file "+file+" non è stato trovato l'attributo DIMENSION (=numero dei fornitori)");
			System.err.println("Il programma verrà terminato");
			System.exit(1);
		}else{
			dimension = Integer.parseInt(temp);
		}
		
		//MAX_N_RANGE
		int maxNRange;
		if((temp = readAttribute("MAX_N_RANGE"))	==	null){
			System.err.println("Attenzione:");
			System.err.println("Nel file "+file+" non è stato trovato l'attributo MAX_N_RANGE\n");
			maxNRange = -1;
		}else{
			maxNRange = Integer.parseInt(temp);
		}
		
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
	private String readAttribute(String attribute){
		String value = null;
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			int tagLength = attribute.length();
			String line;
			boolean found = false;
			while((line = br.readLine()) != null	&&	!found){
				if(line.length()>=tagLength){
					String temp=line.substring(0,tagLength);
					if(temp.equals(attribute)){
						found = true;
						String lineElements[] = line.split("\\s[\\s]*");
						value=lineElements[2];
					}
				}
			}
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		return value;
	}
	
	/*
	 * legge dal file contenente la descrizione del problema la domanda dei vari prodotti
	 * e la restituisce; restituisce null se DEMAND_SECTION non viene trovata
	 */
	private int[] readDemandSection() throws Exception{
		int demand[] = null;
		try{
			FileInputStream fstream;
			
			fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			String lineElements[];
			
			while((line = br.readLine()) != null){
				lineElements = line.split("\\s[\\s]*");
				
				if(line.length()==0){
					//ignoro eventuali linee vuote
					;
				}else if(lineElements[0].charAt(0) == '#'){
					//ignoro eventuali commenti
					;
				}else if(lineElements[0].equals("DEMAND_SECTION")){
					//leggo la prima riga che indica il numero di prodotti
					line = br.readLine();
					numProducts = Integer.parseInt(line);
					
					//leggo le altre righe (con la domanda di ogni prodotto)
					//non viene utilizzato demand[0]
					demand = new int[numProducts+1];
					//demand[k]=-1 significa che per il prodotto k la domanda non è stata specificata
					for(int i = 0; i<=demand.length-1; i++){
						demand[i]=-1;
					}
					//vale true quando raggiungo la fine DEMAND_SECTION
					boolean end = false;
					while(!end){
						line = br.readLine();
						lineElements = line.split("\\s[\\s]*");
						
						if(lineElements[0].equals("OFFER_SECTION")){
							end = true;
						}else{
							//id del prodotto
							int productId = Integer.parseInt(lineElements[0]);
							if(productId<1 || productId>numProducts){
								error("In DEMAND_SECTION del file "+file+" il prodotto "+productId+" non è accettabile");
							}
							//domanda
							int productDemand = Integer.parseInt(lineElements[1]);
							demand[productId] = productDemand;
						}
					}
					//controllo che siano state specificate tutte le domande
					for(int i=1; i<=demand.length-1; i++){
						if(demand[i]==-1){
							//lanciaW("asdasdasdasdads", valoreDefault)
							System.out.println("Attenzione:");
							System.out.println("In DEMAND_SECTION del file "+file+" la domanda del prodotto "+i+" non è stata specificata");
							System.out.println("Ad essa verrà assegnato il valore di default 0\n");
							demand[i]=0;
						}
					}
				}
			}
			in.close();
		}catch(FileNotFoundException e){
			System.err.println("Errore: " + e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("Errore: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Errore: " + e.getMessage());
			e.printStackTrace();
		}
		return demand;
	}
	
	
	/*
	 * legge il file contenente la descrizione del problema e crea l'array con i supplier
	 */
	private Supplier[] makeSuppliers(int numSuppliers){
		Supplier suppliers[] = null;
		try{
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			String lineElements[];
			
			//mi porto alla riga OFFER_SECTION
			String tag = "OFFER_SECTION";
			//vale true quando ho raggiunto OFFER_SECTION
			boolean end = false;
			while(!end	&&	(line = br.readLine()) != null){
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
				line = br.readLine();
				
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
				line = br.readLine();
				
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
			
			in.close();
		}catch(Exception e){
			System.err.println("Errore: " + e.getMessage());
		}
		
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
	
	private void error(String message) throws Exception{
		System.err.println("Errore:");
		System.err.println(message);
		System.err.println("Il programma verrà terminato.");
		throw new Exception(message);
	}
	
	private void lanciaE(){
		;
	}
	
}

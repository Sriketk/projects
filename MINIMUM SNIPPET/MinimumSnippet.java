import java.util.List;
import java.util.ArrayList;

/* this class is used to find the shortest snippet of a sequence of characters
based upon given search terms
 */
public class MinimumSnippet {
	
	boolean foundAllTerms;// used to determine if all of the terms are found in the document
	int kingStart= 0;//starting character of the shortest snippet
	int kingEnd=999999;//ending character of the shortest snippet
	int kingLength;//length of the shortest snippet
	ArrayList<String> kingTerm = new ArrayList<String>(); // holds the shortest snippet
	Iterable<String>document; //document
	List<String>terms; //terms(search terms)

	/*The constructor is used to instantiate the document and the terms
	 * It is also responsible for finding the shortest snippet in a document
	 */
	public MinimumSnippet(Iterable<String> document, List<String> terms) {
		
		//variables
		int temporaryStart = -1; // variable for a temporary starting position
		int temporarySecond = -1;// variable for a temporary second position
		int lastStart = -1;// start position for a possible snippet
		int lastSecond = -1;// second term position for a possible snippet
		int lastEnd = -1;// last position for a possible snippet
		
		
		this.document = document;
		this.terms = terms;
		//copy of document in terms of an array list
		ArrayList<String> newDocument = new ArrayList<String>();
		// copy of terms in an arraylist
		ArrayList<String> temporaryTerms = new ArrayList<String>();
		
		newDocument = iteratorToArrayList(document);//converting document content into string arraylist
		 // determines if the document contains all the terms
		this.foundAllTerms = newDocument.containsAll(terms);
		
		
		if(terms.isEmpty()) { // exception is throw if there are no terms
			throw new IllegalArgumentException();
		}
		if(terms.size()==1) { // special case if there is only one term
			//once the term is found it assigns it as the shortest
			for(int i = 0;i<newDocument.size();i++) {
				if(terms.contains(newDocument.get(i))) {
					kingStart = i;
					kingEnd = i;
					break;
				}
				
			}		
		} else {// for any other case
			
			for(int i = 0;i<newDocument.size();i++) {
				// if the first element of document is a term
				if(terms.contains(newDocument.get(i))){
					// this checks for duplicate terms
					if(temporaryTerms.contains(newDocument.get(i))){
						// if the second term has not been repeated
						if(newDocument.get(i).equals(newDocument.get(temporaryStart))
						&& temporarySecond ==-1){
							//puts new starting position at the last 
							temporaryTerms.clear();
							temporaryTerms.add(newDocument.get(i));
							temporaryStart = i;
						} else if(newDocument.get(i).equals(newDocument.get(temporaryStart))) {
							temporaryTerms.clear();
							temporaryStart = temporarySecond;
							temporaryTerms.add(newDocument.get(temporaryStart));
							temporarySecond = -1;
							i = temporaryStart;
						} else {
							continue;
						}
					} else {
						//adds terms to the temporary list till all terms have been added
						temporaryTerms.add(newDocument.get(i));
						//if first terms hasnt been added
						if(temporaryStart ==-1) {
							temporaryStart = i;
						// if second term hasnt been added
						} else if(temporarySecond ==-1&& temporaryTerms.containsAll(terms)==false) {
							temporarySecond = i;
						// if all terms have been added to the temporary list, it is a possible snippet
						} else if (temporaryTerms.containsAll(terms)) {
							lastStart = temporaryStart;
							lastSecond = temporarySecond;
							lastEnd = i;
							//all temporary position have been reset
							temporaryStart = -1;
							temporarySecond= -1;
							
							
							//checks for the shortest snippett
							if(winConditions(lastEnd, lastStart)) {
								kingTerm.clear();
								kingStart = lastStart;
								kingEnd = lastEnd;
								for(int x = lastStart;x<=lastEnd;x++) {
									kingTerm.add(newDocument.get(x));
								}
								
							}
							// clears the temporary snippet and checks for a possible shorter one
							temporaryTerms.clear();
							// starts the loop at the last element
							if(lastSecond ==-1) {
								i = lastEnd -1;
							} else {
							i = lastSecond -1;
							}
						}else {
							// goes to the next iteration(next element in document)
							continue;
						}
					}
				}
			}
		}
	}
					
						
		
			
	
	// method turns an iterator into an arraylist
	private ArrayList<String> iteratorToArrayList(Iterable<String> iterator){
		ArrayList<String> arrayListDocument = new ArrayList<String>();
		iterator = this.document;
		for(String element: iterator){
			arrayListDocument.add(element);
		}
		return arrayListDocument;
		
	}
	// finds the shortest snippet
	private boolean winConditions(int rivalEnd, int rivalStart) {
		if((kingEnd - kingStart)+1 ==terms.size()) {
			return true;
		} else if((kingEnd - kingStart)+1 > (rivalEnd - rivalStart)+1) {
			return true;
		}
		return false;
	}

	public boolean foundAllTerms() { // returns if all the terms have been found
		
		return foundAllTerms;

	}

	public int getStartingPos() {// returns the first position
		return kingStart;

	}

	public int getEndingPos() { // returns the last position
		return kingEnd;

	}

	public int getLength() { // returns the length of the shortest snippet
		return (kingEnd - kingStart) +1;
	}
    //Returns the position of one of the search terms as it appears in the original document
	//finds the term that is needed using a for loop
	public int getPos(int index) {
		
		int intTerm = 0;
		for(int i = 0; i< kingTerm.size(); i++) {
			
			if(terms.get(index).equals(kingTerm.get(i))) {
				 intTerm = i;
			}
		}
		return kingStart +intTerm;


	}

}

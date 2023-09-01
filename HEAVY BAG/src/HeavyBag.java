import java.io.Serializable;
import java.util.HashMap;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 * <P>
 * The HeavyBag class implements a Set-like collection that allows duplicates (a
 * lot of them).
 * </P>
 * <P>
 * The HeavyBag class provides Bag semantics: it represents a collection with
 * duplicates. The "Heavy" part of the class name comes from the fact that the
 * class needs to efficiently handle the case where the bag contains 100,000,000
 * copies of a particular item (e.g., don't store 100,000,000 references to the
 * item).
 * </P>
 * <P>
 * In a Bag, removing an item removes a single instance of the item. For
 * example, a Bag b could contain additional instances of the String "a" even
 * after calling b.remove("a").
 * </P>
 * <P>
 * The iterator for a heavy bag must iterate over all instances, including
 * duplicates. In other words, if a bag contains 5 instances of the String "a",
 * an iterator will generate the String "a" 5 times.
 * </P>
 * <P>
 * In addition to the methods defined in the Collection interface, the HeavyBag
 * class supports several additional methods: uniqueElements, getCount, and
 * choose.
 * </P>
 * <P>
 * The class extends AbstractCollection in order to get implementations of
 * addAll, removeAll, retainAll and containsAll.  (We will not be over-riding those).
 * All other methods defined in
 * the Collection interface will be implemented here.
 * </P>
 */
public class HeavyBag<T> extends AbstractCollection<T> implements Serializable {

    /* Leave this here!  (We need it for our testing) */
	private static final long serialVersionUID = 1L;
	
	private HashMap<T,Integer> heavyBagMap;
	// hashmap that stores the object and the number of occurunces
	public int size;
	//stores the size of the total number of occurences of each object

	
	/* Create whatever instance variables you think are good choices */
	
	
	
	
	
	
	
	/**
     * Initialize a new, empty HeavyBag
     */
    public HeavyBag() {
    	 heavyBagMap = new HashMap<>();//instantiates the hashMap
    	 size = 0;//instantiates the size with 0
    }

    /**
     * Adds an instance of o to the Bag
     * 
     * @return always returns true, since added an element to a bag always
     *         changes it
     * 
     */
    @Override
    public boolean add(T o) {
    	if(heavyBagMap.containsKey(o)) {// checks to see if object is already there
			Integer numOccured = heavyBagMap.get(o);//gets the number of occurrences
			heavyBagMap.replace(o, numOccured+1);//adds one to the occurrences
			size++;//increase the size
		} else {// if the key is not already there
			heavyBagMap.put(o,1);//puts one occurence for the new key
			size++;
		}
    	return true;//return true since it is always going to add
    }

    /**
     * Adds multiple instances of o to the Bag.  If count is 
     * less than 0 or count is greater than 1 billion, throws
     * an IllegalArgumentException.
     * 
     * @param o the element to add
     * @param count the number of instances of o to add
     * @return true, since addMany always modifies
     * the HeavyBag.
     */
    
    public boolean addMany(T o, int count) {
    	if((count<0)||count>1000000000){// checks to see if count is valid
    		throw new IllegalArgumentException();
    	} 

		Integer numOccured = heavyBagMap.get(o);//gets the number of occurrences of the key
		size+=count;// increases the size with the count
    	if(heavyBagMap.containsKey(o)) {//checks to see if the key is already there
			heavyBagMap.remove(o);// for hashing purposes, removes a occurrence
			heavyBagMap.put(o, numOccured+count);//updates with the count
		} else {// if the key is not in the hashmap
			heavyBagMap.put(o, count);	
		}
    	return true;
    }
    
    /**
     * Generate a String representation of the HeavyBag. This will be useful for
     * your own debugging purposes, but will not be tested other than to ensure that
     * it does return a String and that two different HeavyBags return two
     * different Strings.
     */
    @Override
    public String toString() {
    	return heavyBagMap.keySet() +"";
    }

    /**
     * Tests if two HeavyBags are equal. Two HeavyBags are considered equal if they
     * contain the same number of copies of the same elements.
     * Comparing a HeavyBag to an instance of
     * any other class should return false;
     */
    @Override
    public boolean equals(Object o) {//FIX
    	boolean returnValue = true;
    	if(o==null) {// checks to make sure the parameter is not null
    		return false;
    	}
    	//checks to make sure the object is from the same class the bag
    	if(o.getClass()!=this.getClass()) {
    		return false;
    	}
    	// checks to make sure they have the same hashcode
    	if(o.hashCode()!=this.hashCode()) {
    		return false;
    	}
    	// iterates through the keys of the hashMap
    	for(T curr: heavyBagMap.keySet()){
    		// casts the o to a collection to T to be compatible
    		// if the bag of the parameter doest contain any of keys from the current object
    		if(((AbstractCollection<T>) o).contains(curr)== false) {
    			returnValue = false;
    			break;
    			
    		}
    	}
    	return returnValue;
    }

    /**
     * Return a hashCode that fulfills the requirements for hashCode (such as
     * any two equal HeavyBags must have the same hashCode) as well as desired
     * properties (two unequal HeavyBags will generally, but not always, have
     * unequal hashCodes).
     */
    @Override
    public int hashCode() {
    	int returnValue=0;//variable for the hashcode
    	for(T curr:heavyBagMap.keySet()) {//iterates through the keyset
    		returnValue += curr.hashCode()*heavyBagMap.get(curr);
    		// by operating within the keys from the current object, the hashcode stays unique
    	}
    	return returnValue;
    }

    /**
     * <P>
     * Returns an iterator over the elements in a heavy bag. Note that if a
     * Heavy bag contains 3 a's, then the iterator must iterate over 3 a's
     * individually.
     * </P>
     */
    @Override
    public Iterator<T> iterator() {
    	return new MyIterator();
    }
    
    class MyIterator implements Iterator<T>{
    	Iterator <T> keyIt=heavyBagMap.keySet().iterator();//makes the key set into an iterator
        T next=keyIt.next();// the first next element
        int curr = 0;//keeps track of the current key number along with occurunces
    	int currCount = 0;//keeps track of the current occurrence number
    	int count = heavyBagMap.get(next);//number of occurrences of each key
    
    	@Override
    	public boolean hasNext() {
    		//checks to make sure the number of the current element doesnt reach the size yet
    		return(curr<size);
    	}
    	
    	@Override
    	public T next() {
    		if(!hasNext()){
    			throw new NoSuchElementException();
    		}
    		// if occurrence tracker hasnt met the number of occurrences of that key yet
    		if(currCount<count) {
    			curr++;//increases to amount of visited occurrences
    			currCount++;//increases the count along the occurrences
    			return next;//returns the element
    		}else {
    			next = keyIt.next();//goes to next key
    			currCount = 1;//resets the currcount for the next key
    			//gets the number of occurrences of next key
    			count = heavyBagMap.get(next);
    			curr++;//increases the number of visited occurrences
    			return next;
    		}
    	}
    	
    	@Override
    	public void remove() {
    		  throw new UnsupportedOperationException();
    	}
    	
    }
    /**
     * return a Set of the elements in the Bag (since the returned value is a
     * set, it can contain no duplicates. It will contain one value for each 
     * UNIQUE value in the Bag).
     * 
     * @return A set of elements in the Bag
     */
    public Set<T> uniqueElements() {
    	//returns the key set and since set doesnt contain duplicate elements
    	return heavyBagMap.keySet();
    }

    /**
     * Return the number of instances of a particular object in the bag. Return
     * 0 if it doesn't exist at all.
     * 
     * @param o
     *            object of interest
     * @return number of times that object occurs in the Bag
     */
    public int getCount(Object o) {
    	if(this.contains(o)==false) {//if the object is not in the bag
    		return 0;
    	}
    	return heavyBagMap.get(o);//returns the number of occurrences
    }

    /**
     * Given a random number generator, randomly choose an element from the Bag
     * according to the distribution of objects in the Bag (e.g., if a Bag
     * contains 7 a's and 3 b's, then 70% of the time choose should return an a,
     * and 30% of the time it should return a b.
     * 
     * This operation can take time proportional to the number of unique objects
     * in the Bag, but no more.
     * 
     * This operation should not affect the Bag.
     * 
     * @param r
     *            Random number generator
     * @return randomly chosen element
     */
    public T choose(Random r) {
    	int randNumber = r.nextInt(this.size());//the number of the random element
    	int currPosition = 0;
    	T returnValue = null;
    	for(T curr:heavyBagMap.keySet()) {//iterates through the key set
    		//number of occurrences for the key
    		currPosition += heavyBagMap.get(curr);
    		if(randNumber<currPosition) {//if the random element is less 
    			returnValue = curr;//return value holds the key
    			break;
    		}
    	}
    	return returnValue;
    }

    /**
     * Returns true if the Bag contains one or more instances of o
     */
    @Override
    public boolean contains(Object o) {
    	return heavyBagMap.containsKey(o);//if the map contains the parameter
    }


    /**
     * Decrements the number of instances of o in the Bag.
     * 
     * @return return true if and only if at least one instance of o exists in
     *         the Bag and was removed.
     */
    @Override
    public boolean remove(Object o) {
    	//number of occurrence of the object
    	Integer occurrenceSize = heavyBagMap.get(o);
    	//if the parameter is in the map
    	if(heavyBagMap.containsKey(o)&&heavyBagMap.get(o)>0) {
    		//if o has more than 2 occurrences
    		if(occurrenceSize>=2) {
    			//removes an occurrence
    			heavyBagMap.put((T)o, --occurrenceSize);
    			//reduces the size
    			size--;
    		} else {//if only one occurrence of the object
    			//removes the key from the map
    			heavyBagMap.remove(o);
    			//reduces the size
    			size--;
    		}
    		return true;
    	}
    	return false;
    }

    /**
     * Total number of instances of any object in the Bag (counting duplicates)
     */
    @Override
    public int size() {
    	return size;//size is updated through the functions of the class
    }
}
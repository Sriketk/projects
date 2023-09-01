package graph;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */
	
	
	
	
	
	
	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;
	private HashMap <V,HashMap<V,Integer>> weightGraph;
	
	

	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		observerList = new ArrayList<GraphAlgorithmObserver<V>>();
		weightGraph = new HashMap<V,HashMap<V,Integer>>();
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if(containsVertex(vertex)) {
			throw new IllegalArgumentException();
		} else {
			weightGraph.put(vertex, new HashMap<V,Integer>());
		}
		
	}
	
	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		if(weightGraph.containsKey(vertex)) {
			return true;
		}
		return false;
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if(containsVertex(from)== false) {//if from doesnt exist
			throw new IllegalArgumentException();
		} else if(containsVertex(to)==false) {// if to doesnt exist
			throw new IllegalArgumentException();
		} else if(weight<0) {// if the weight is negative
			throw new IllegalArgumentException();
		} else {
			weightGraph.get(from).put(to, weight);//assigns the weight
		}
	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {//calculates the weight
		if(containsVertex(from)== false) {
			throw new IllegalArgumentException();
		} else if(containsVertex(to)==false) {
			throw new IllegalArgumentException();
		} else if (weightGraph.get(from).containsKey(to)== false){
			return null;
		} else {
			return weightGraph.get(from).get(to);
		}
	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		for(GraphAlgorithmObserver<V> observed: observerList) {
			observed.notifyBFSHasBegun();
		}
		Queue<V> discoveredQueue = new LinkedList<V>();//queue for the discovered vertexes
		Set<V> visitedSet = new HashSet<V>();//hashset for the visited vertexes
		discoveredQueue.add(start);//adds the root vertex to the queue
		while(discoveredQueue.isEmpty()== false) {// if queue has elements inside
			V temporaryElement =discoveredQueue.element() ;//holds the head
			discoveredQueue.remove();//removes from the queue
			// if the visited set does not contain the removed element
			if(visitedSet.contains(temporaryElement)== false){
				visitedSet.add(temporaryElement);//adds to the visited set
				for(GraphAlgorithmObserver<V> observed: observerList) {
					observed.notifyVisit(temporaryElement);
				}
			}
			if(temporaryElement.equals(end)) {// if the end is reached
				for(GraphAlgorithmObserver<V> observed: observerList) {
					observed.notifySearchIsOver();
				}
				break;
			}	
			//looks at the neighbors and adds them to the end of the queue
				for(V temp:weightGraph.get(temporaryElement).keySet()) {
					// if the elements arent already visited
					if(visitedSet.contains(temp)== false) {
						discoveredQueue.add(temp);
					}
				}
		}
		
	}
	
	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		for(GraphAlgorithmObserver<V> observed: observerList) {
			observed.notifyDFSHasBegun();
		}
		Set<V> visitedSet = new HashSet<V>();
		Stack<V> discoveredStack = new Stack<V>();// stack for the discovered
		discoveredStack.push(start);//pushes the root vertex into the empty stack
		//while the stack is not empty
		while(discoveredStack.isEmpty()== false) {
			//takes whats on top of the stack out
			V temporaryElement = discoveredStack.pop();
			// if the visited set doesnt already contain whats popped from the stack
			if(visitedSet.contains(temporaryElement)== false) {
				visitedSet.add(temporaryElement);//added
				for(GraphAlgorithmObserver<V> observed: observerList) {
					observed.notifyVisit(temporaryElement);
				}
			}
			if(temporaryElement.equals(end)) {// if the end is found the search stops
				for(GraphAlgorithmObserver<V> observed: observerList) {
					observed.notifySearchIsOver();
				}
				break;
			}
			// looks at the neighhbors and adds them to top of the stack
			for(V temp:weightGraph.get(temporaryElement).keySet()) {
				// if element is not already in the visited set
				if(visitedSet.contains(temp)== false) {
					discoveredStack.push(temp);
				}
			}
		}
		
	}
	
	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		for(GraphAlgorithmObserver<V> observed: observerList) {
			observed.notifyDijkstraHasBegun();;
		}
		Map<V,V>predecessorMap = new HashMap<V,V>();//this maps the vertex with the predecessor
		Map<V,Integer> weightMap = new HashMap<V,Integer>();// this maps the vertex with the cost
		HashSet<V> finishedSet = new HashSet<V>();// this is where all visited sets go
		Integer smallestValue = Integer.MAX_VALUE;// this holds the smallest cost value
		LinkedList<V> dijsktraList = new LinkedList<V>();
		// this holds the key of the smallest value
		// this loop assigns the maps with the vertex and default values
		for(V value: weightGraph.keySet()) {
			predecessorMap.put(value, null);
			weightMap.put(value, Integer.MAX_VALUE);
		}
		weightMap.put(start, 0);// the starting root vertex holds a weight of 0
		//the starting root vertex is the same as the predecessor
		predecessorMap.put(start,start);
		
		// as long as all the vertexes havent been visited
		while(finishedSet.size()!=weightGraph.size()) {
			weightMap.put(null, Integer.MAX_VALUE);
			V smallestKey = null;
			//loop tries to find the smallest value
			for(V kingSearcher:weightMap.keySet()) {
				if((weightMap.get(kingSearcher)<weightMap.get(smallestKey))) {
					if(finishedSet.contains(kingSearcher)== false) {
						smallestKey = kingSearcher;
						smallestValue = weightMap.get(smallestKey);
					}
				}
			}
			finishedSet.add(smallestKey);//visits the smallest key because its done
			//looks at the neighbors of the smallest key
			for(GraphAlgorithmObserver<V> observed: observerList) {
				observed.notifyDijkstraVertexFinished(smallestKey, weightMap.get(smallestKey));;
			}
			for(V neighbor:weightGraph.get(smallestKey).keySet()) {
				if(finishedSet.contains(neighbor)== false){
					//checks to see if the new path yields a lower cost
					if(smallestValue + getWeight(smallestKey,neighbor)<weightMap.get(neighbor)) {
					//alters the maps with the lower costs and the new precedessor
						weightMap.put(neighbor, smallestValue + getWeight(smallestKey,neighbor));
						predecessorMap.put(neighbor, smallestKey);
					}
					
				}
				
			}
			weightMap.remove(null,Integer.MAX_VALUE);
		}
		for(V value = end; value!=start;value = predecessorMap.get(value)) {
			dijsktraList.add(value);
		}
		for(GraphAlgorithmObserver<V> observed: observerList) {
			observed.notifyDijkstraIsOver(dijsktraList);
		}
		
			
	}
	
}

package smallworld;

import edu.princeton.cs.In;
import edu.princeton.cs.StdOut;

/******************************************************************************
 *  Compilation:  javac Graph.java
 *  Execution:    java Graph
 *  Dependencies: ST.java SET.java In.java StdOut.java
 *  
 *  Undirected graph data type implemented using a symbol table
 *  whose keys are vertices (String) and whose values are sets
 *  of neighbors (SET of Strings).
 *
 *  Remarks
 *  -------
 *   - Parallel edges are not allowed
 *   - Self-loop are allowed
 *   - Adjacency lists store many different copies of the same
 *     String. You can use less memory by interning the strings.
 *
 ******************************************************************************/

/**
 *  The <tt>Graph</tt> class represents an undirected graph of vertices
 *  with string names.
 *  It supports the following operations: add an edge, add a vertex,
 *  get all of the vertices, iterate over all of the neighbors adjacent
 *  to a vertex, is there a vertex, is there an edge between two vertices.
 *  Self-loops are permitted; parallel edges are discarded.
 *  <p>
 *  For additional documentation, see <a href="http://introcs.cs.princeton.edu/45graph">Section 4.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public class GraphQueue {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private ST<String, Queue<String>> st;

    // number of edges
    private int E;

   /**
     * Create an empty graph with no vertices or edges.
     */
    public GraphQueue() {
        st = new ST<String, Queue<String>>();
    } //Graph()
   
    /**
     * Create a copy constructor for Graph that takes as argument a graph G,
     * then creates and initializes a new, independent copyof the graph. 
     * Any future changes to G should not affect the newly created graph.     * 
     */
    public GraphQueue(GraphQueue G) {
        Graph g = new Graph();
    } //Graph()

   /**
     * Create an graph from given input stream using given delimiter.
     */
    public GraphQueue(In in, String delimiter) {
        st = new ST<String, Queue<String>>();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] names = line.split(delimiter);
            for (int i = 1; i < names.length; i++) {
                addEdge(names[0], names[i]);
            } //for
        } //while
    } //Graph

   /**
     * Number of vertices.
     */
    public int V() {
        return st.size();
    } //V()

   /**
     * Number of edges.
     */
    public int E() {
        return E;
    } //E()

    // throw an exception if v is not a vertex
    private void validateVertex(String v) {
        if (!hasVertex(v)) throw new IllegalArgumentException(v + " is not a vertex");
    } //validateVertex( String )

   /**
     * Degree of this vertex.
     */
    public int degree(String v) {
        validateVertex(v);
        return st.get(v).size();
    } //degree ( String )

   /**
     * Add edge v-w to this graph (if it is not already an edge)
     */
    public void addEdge(String v, String w) {
        if (!hasVertex(v)) addVertex(v);
        if (!hasVertex(w)) addVertex(w);
        if (!hasEdge(v, w)) E++;
        st.get(v).enqueue(w);
        st.get(w).enqueue(v);
    } //addEdge( String, String)

   /**
     * Add vertex v to this graph (if it is not already a vertex)
     */
    public void addVertex(String v) {
        if (!hasVertex(v)) st.put(v, new Queue<String>());
    } //AddVertex ( String )


   /**
     * Return the set of vertices as an Iterable.
     */
    public Iterable<String> vertices() {
        return st.keys();
    } //vertices()

   /**
     * Return the set of neighbors of vertex v as an Iterable.
     */
    public Iterable<String> adjacentTo(String v) {
        validateVertex(v);
        return st.get(v);
    } //adjacentTo (String)

   /**
     * Is v a vertex in this graph?
     */
    public boolean hasVertex(String v) {
        return st.contains(v);
    } //hasVertex( String )

   /**
     * Is v-w an edge in this graph?
     */
    public boolean hasEdge(String v, String w) {
        validateVertex(v);
        validateVertex(w);
        return st.get(v).contains(w);
    } // hasEdge( String, String)

   /**
     * Return a string representation of the graph.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            } //for
            s.append("\n");
        } //for
        return s.toString();
    } //toString()      

    public static void main(String[] args) {
        Graph G = new Graph();
        G.addEdge("A", "B");
        G.addEdge("A", "C");
        G.addEdge("C", "D");
        G.addEdge("D", "E");
        G.addEdge("D", "G");
        G.addEdge("E", "G");
        G.addVertex("H");

        // print out graph
        StdOut.println(G);

        // print out graph again by iterating over vertices and edges
        for (String v : G.vertices()) {
            StdOut.print(v + ": ");
            for (String w : G.adjacentTo(v)) {
                StdOut.print(w + " ");
            } //for
            StdOut.println();
        } //for

    } //main( String, args)

} //Graph


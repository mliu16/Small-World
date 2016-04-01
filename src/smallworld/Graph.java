package smallworld;

import edu.princeton.cs.In;
import edu.princeton.cs.StdOut;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ****************************************************************************
 * Compilation: javac Graph.java Execution: java Graph Dependencies: ST.java
 * SET.java In.java StdOut.java
 *
 * Undirected graph data type implemented using a symbol table whose keys are
 * vertices (String) and whose values are sets of neighbors (SET of Strings).
 *
 * Remarks ------- - Parallel edges are not allowed - Self-loop are allowed -
 * Adjacency lists store many different copies of the same String. You can use
 * less memory by interning the strings.
 *
 *****************************************************************************
 */
/**
 * The <tt>Graph</tt> class represents an undirected graph of vertices with
 * string names. It supports the following operations: add an edge, add a
 * vertex, get all of the vertices, iterate over all of the neighbors adjacent
 * to a vertex, is there a vertex, is there an edge between two vertices.
 * Self-loops are permitted; parallel edges are discarded.
 * <p>
 * For additional documentation, see
 * <a href="http://introcs.cs.princeton.edu/45graph">Section 4.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public class Graph {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private ST<String, SET<String>> st;

    // number of edges
    private int E;
    // number of vertices
    private int V;
    private ArrayList<String>[] adj;

    /**
     * Create an empty graph with no vertices or edges.
     */
    public Graph() {
        st = new ST<String, SET<String>>();
    } //Graph()

    /**
     * Create a copy constructor for Graph that takes as argument a graph G,
     * then creates and initializes a new, independent copy of the graph. Any
     * future changes to G should not affect the newly created graph. *
     */
    public Graph(Graph G) {
        try {
            Graph newGraph = (Graph) G.clone();
            this.V = newGraph.V();
            this.E = newGraph.E();
            this.st = newGraph.st;
        } catch (CloneNotSupportedException ex) {
            // do nothing
        } //Graph
    } //Graph()    

    /**
     * Create an graph from given input stream using given delimiter.
     */
    public Graph(In in, String delimiter) {
        st = new ST<String, SET<String>>();
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
        if (!hasVertex(v)) {
            throw new IllegalArgumentException(v + " is not a vertex");
        }
    } //validateVertex( String )

    /**
     * Degree of this vertex: how many adges attaching to it.
     */
    public int degree(String v) {
        validateVertex(v);
        return st.get(v).size();
    } //degree ( String )

    //average degree: total number of adges divided by number of vertices
    public double averageDegree() {
        double total = 0;
        for (String vertex : this.vertices()) {
            total += this.degree(vertex);
        } //for
        double average = total / this.V();
        return average;
    } //averageDegree()

    //average distance:  
    public double averageLength() {
        double grandTotal = 0;
        int counter = 0;
        for (String vertex : this.vertices()) {
            PathFinder pf = new PathFinder(this, vertex);
            int total = 0;
            for (String othervertex : this.vertices()) {
                if (vertex.equals(othervertex)) {
                    break;
                }
                counter++;
                total += pf.distanceTo(othervertex);
            } //for
            total /= (this.V() - 1);
            grandTotal += total;
        } //for
        double average = grandTotal / counter;
        return average;
    } //averageDegree()

    /**
     * Add edge v-w to this graph (if it is not already an edge)
     */
    public void addEdge(String v, String w) {
        if (!hasVertex(v)) {
            addVertex(v);
        }
        if (!hasVertex(w)) {
            addVertex(w);
        }
        if (!hasEdge(v, w)) {
            E++;
        }
        st.get(v).add(w);
        st.get(w).add(v);
    } //addEdge( String, String)

    /**
     * Add vertex v to this graph (if it is not already a vertex)
     */
    public void addVertex(String v) {
        if (!hasVertex(v)) {
            st.put(v, new SET<String>());
        }
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
     * remove specific edges between v and w *
     */
    public void remove(String v, String w) {
        if (hasEdge(v, w)) {
            st.get(v).delete(w);
            st.get(w).delete(v);
        } //if
    } //remove(String)      

    /**
     * Return a string representation of the graph.
     */
    @Override
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

    private String makeName(int a) {
        return "v" + a;
    }//makeName

    private String makeName(int row, int column) {
        return "r" + row + "c" + column;
    } //makeName ()

    //Make a complete graph
    public void completeGraph(int v) {
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                this.addEdge(makeName(i), makeName(j));
            } //for
        } //for 
    } //Graph()

    //Make a ring graph
    public void ringGraph(int r) {
        for (int i = 0; i < r - 1; i++) {
            this.addEdge(makeName(i), makeName(i + 1));
        } //for 
        this.addEdge(makeName(r - 1), makeName(0));
    } //Graph()

    //Make a grid graph
    public void gridGraph(int n) {
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                this.addEdge(makeName(i, j), makeName(i, j + 1));
                this.addEdge(makeName(i, j), makeName(i + 1, j));

            } //for
        } //for

        //complete right-most edges
        for (int i = 0; i < n - 1; i++) {
            this.addEdge(makeName(i, n - 1), makeName(i + 1, n - 1));
        } //for

        //compete bottom-most edges
        for (int i = 0; i < n - 1; i++) {
            this.addEdge(makeName(n - 1, i), makeName(n - 1, i + 1));
        }
    } //gridGraph( int )

    //Create dot file for grid graph
    public void writeDotFile() {
        for (String u : this.vertices()) {
            for (String v : this.vertices()) {
                if ((u.compareTo(v) < 0) && (this.hasEdge(u, v))) {
                    System.out.println(u + "->" + v + ":");
                } //if
            } //for
        } //for
    } //writeDotFile()

    //make a second level ring graph
    public void secondLevelRingGraph(int h) {
        for (int i = 0; i < h - 2; i++) {
            this.addEdge(makeName(i), makeName(i + 1));
            this.addEdge(makeName(i), makeName(i + 2));
        } //for
        this.addEdge(makeName(h - 2), makeName(h - 1));
        this.addEdge(makeName(h - 1), makeName(0));
        this.addEdge(makeName(h - 2), makeName(0));
        this.addEdge(makeName(h - 1), makeName(1));
    } //Graph()   

    //make a star graph
    public void starGraph(int s) {
        int globleCluster = (int) ((int) 1 + (Math.random() * (s - 1)));
        if (globleCluster == 0) {
            for (int i = 0; i < s - 1; i++) {
                this.addEdge(makeName(0), makeName(i + 1));
            } //for
        } //if
        else{
            for (int i = 0; i < s - globleCluster - 1; i++) {
                this.addEdge(makeName(globleCluster), makeName(globleCluster + i + 1));
            }//for
            for (int i = 0; i < globleCluster; i++ ) {
                this.addEdge(makeName(globleCluster), makeName(i ));
            }//for
        } //else        
        System.out.println("Globle cluster: " + globleCluster);
    } //starGraph( int )

    public static void main(String[] args) {

        Graph G = new Graph();
        G.starGraph(4);
//        System.out.println(G.averageLength());
        System.out.println(G);
//        G.writeDotFile();

//        Graph G = new Graph();
//        G.addEdge("A", "B");
//        G.addEdge("A", "C");
//        G.addEdge("C", "D");}
//        G.addEdge("D", "E");
//        G.addEdge("D", "G");
//        G.addEdge("E", "G");
//        G.addVertex("H");
//
//        // print out graph
//        StdOut.println(G);
//
//        // print out graph again by iterating over vertices and edges
//        for (String v : G.vertices()) {
//            StdOut.print(v + ": ");
//            for (String w : G.adjacentTo(v)) {
//                StdOut.print(w + " ");
//            } //for
//            StdOut.println();
//        } //for
//
    } //main( String, args)
//  
} //Graph

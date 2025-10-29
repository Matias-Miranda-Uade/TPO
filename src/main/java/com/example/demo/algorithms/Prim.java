package com.example.demo.algorithms;

import java.util.*;

public class Prim {

    // Representa una arista no dirigida entre u y v con peso w
    public static class Edge implements Comparable<Edge> {
        public final int u;
        public final int v;
        public final int w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.w, o.w);
        }

        @Override
        public String toString() {
            return String.format("%d -- %d (w=%d)", u, v, w);
        }
    }

    // Construye la lista de adyacencia (arrays de listas) a partir de una colección de aristas
    public static List<Edge>[] buildAdjList(int n, Collection<Edge> edges) {
        @SuppressWarnings("unchecked")
        List<Edge>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (Edge e : edges) {
            // añadimos aristas en ambas direcciones para grafo no dirigido
            adj[e.u].add(new Edge(e.u, e.v, e.w));
            adj[e.v].add(new Edge(e.v, e.u, e.w));
        }
        return adj;
    }

    // Ejecuta Prim y devuelve la lista de aristas del MST o null si el grafo no es conexo
    // n: numero de nodos (de 0 a n-1)
    // adj: lista de adyacencia
    // start: nodo inicial (por defecto 0 si no se quiere especificar)
    public static List<Edge> primMST(int n, List<Edge>[] adj, int start) {
        if (n <= 0) return Collections.emptyList();
        boolean[] inMST = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        List<Edge> mst = new ArrayList<>();

        // marcar el nodo inicial y añadir sus aristas al heap
        inMST[start] = true;
        for (Edge e : adj[start]) pq.offer(e);

        while (!pq.isEmpty() && mst.size() < n - 1) {
            Edge e = pq.poll();
            // seleccionar la arista que conecta el árbol con un vértice fuera de él
            if (inMST[e.u] && !inMST[e.v]) {
                mst.add(e);
                int next = e.v;
                inMST[next] = true;
                for (Edge ne : adj[next]) {
                    if (!inMST[ne.v]) pq.offer(ne);
                }
            }
            // si la arista conecta dos vértices dentro del árbol o dos fuera, ignorarla
        }

        // si no tenemos n-1 aristas, el grafo no era conexo
        if (mst.size() != n - 1) return null;
        return mst;
    }

    // Sobrecarga conveniente que construye la lista de adyacencia desde una lista de aristas
    public static List<Edge> primMST(int n, Collection<Edge> edges) {
        List<Edge>[] adj = buildAdjList(n, edges);
        return primMST(n, adj, 0);
    }

    // Calcula el peso total de un conjunto de aristas
    public static int totalWeight(Collection<Edge> edges) {
        int sum = 0;
        for (Edge e : edges) sum += e.w;
        return sum;
    }
}

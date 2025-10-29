// java
package com.example.demo.algorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class RamificacionYPoda {

    public static class Result {
        private final int totalValue;
        private final boolean[] selected;

        public Result(int totalValue, boolean[] selected) {
            this.totalValue = totalValue;
            this.selected = selected;
        }

        public int getTotalValue() {
            return totalValue;
        }

        public boolean[] getSelected() {
            return selected;
        }
    }

    private static class Item {
        final int index;
        final int value;
        final int weight;
        final double ratio;

        Item(int index, int value, int weight) {
            this.index = index;
            this.value = value;
            this.weight = weight;
            this.ratio = (weight == 0) ? Double.POSITIVE_INFINITY : (double) value / weight;
        }
    }

    private static class Node {
        int level; // último índice considerado en el arreglo ordenado
        int value;
        int weight;
        double bound;
        boolean[] taken; // selección parcial en orden ordenado

        Node(int n) {
            this.level = -1;
            this.value = 0;
            this.weight = 0;
            this.bound = 0;
            this.taken = new boolean[n];
        }

        Node(Node other) {
            this.level = other.level;
            this.value = other.value;
            this.weight = other.weight;
            this.bound = other.bound;
            this.taken = Arrays.copyOf(other.taken, other.taken.length);
        }
    }


     // Resuelve 0/1 knapsack usando ramificación y poda (best-first).

    public static Result knapsackBranchAndBound(int[] values, int[] weights, int capacity) {
        if (values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Arrays de valores y pesos deben existir y tener la misma longitud.");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacidad debe ser >= 0.");
        }

        int n = values.length;
        if (n == 0 || capacity == 0) {
            return new Result(0, new boolean[n]);
        }

        // Preparar ítems ordenados por ratio desc
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) items[i] = new Item(i, values[i], weights[i]);
        Arrays.sort(items, Comparator.comparingDouble((Item it) -> it.ratio).reversed());

        // Cola prioridad por bound descendente
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(b.bound, a.bound));

        Node root = new Node(n);
        root.bound = computeBound(root, items, capacity);
        pq.add(root);

        int maxValue = 0;
        boolean[] bestTaken = new boolean[n];

        while (!pq.isEmpty()) {
            Node u = pq.poll();
            if (u.bound <= maxValue) continue;

            int nextLevel = u.level + 1;
            if (nextLevel >= n) continue;

            // Hijo: tomar el siguiente ítem
            if (u.weight + items[nextLevel].weight <= capacity) {
                Node with = new Node(u);
                with.level = nextLevel;
                with.weight += items[nextLevel].weight;
                with.value += items[nextLevel].value;
                with.taken[nextLevel] = true;

                if (with.value > maxValue) {
                    maxValue = with.value;
                    bestTaken = Arrays.copyOf(with.taken, n);
                }
                with.bound = computeBound(with, items, capacity);
                if (with.bound > maxValue) pq.add(with);
            }

            // Hijo: no tomar el siguiente ítem
            Node without = new Node(u);
            without.level = nextLevel;
            without.taken[nextLevel] = false;
            without.bound = computeBound(without, items, capacity);
            if (without.bound > maxValue) pq.add(without);
        }

        // Mapear selección desde ordenado a orden original
        boolean[] selected = new boolean[n];
        for (int i = 0; i < n; i++) {
            selected[items[i].index] = bestTaken[i];
        }

        return new Result(maxValue, selected);
    }

    // Calcula cota superior usando la relajación fraccionaria (como fractional knapsack)
    private static double computeBound(Node node, Item[] items, int capacity) {
        if (node.weight >= capacity) return 0;
        double bound = node.value;
        int j = node.level + 1;
        int totalWeight = node.weight;

        while (j < items.length && totalWeight + items[j].weight <= capacity) {
            totalWeight += items[j].weight;
            bound += items[j].value;
            j++;
        }

        if (j < items.length) {
            int remain = capacity - totalWeight;
            if (items[j].weight > 0) {
                bound += remain * (items[j].ratio);
            } else if (items[j].weight == 0 && items[j].value > 0) {
                // si peso 0 y valor positivo => puede sumar todo
                // pero para evitar problemas tratamos como sumar su valor completo.
                bound += items[j].value;
            }
        }
        return bound;
    }
}

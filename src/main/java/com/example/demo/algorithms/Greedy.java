// java
package com.example.demo.algorithms;

import java.util.Arrays;
import java.util.Comparator;

public class Greedy {

    public static class FractionResult {
        private final double totalValue;
        private final double[] fractions; // fracción tomada por ítem (misma orden que entrada)

        public FractionResult(double totalValue, double[] fractions) {
            this.totalValue = totalValue;
            this.fractions = fractions;
        }

        public double getTotalValue() {
            return totalValue;
        }

        public double[] getFractions() {
            return fractions;
        }
    }

    private static class Item {
        final int index;
        final double value;
        final double weight;
        final double ratio;

        Item(int index, double value, double weight) {
            this.index = index;
            this.value = value;
            this.weight = weight;
            this.ratio = weight == 0 ? Double.POSITIVE_INFINITY : value / weight;
        }
    }


    public static FractionResult fractionalKnapsack(double[] values, double[] weights, double capacity) {
        if (values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Arrays de valores y pesos deben existir y tener la misma longitud.");
        }
        int n = values.length;
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(i, values[i], weights[i]);
        }

        Arrays.sort(items, Comparator.comparingDouble((Item it) -> it.ratio).reversed());

        double[] fractions = new double[n];
        double totalValue = 0.0;
        double remaining = capacity;

        for (Item it : items) {
            if (remaining <= 0) break;
            if (it.weight <= 0) continue; // evitar división por cero y items inválidos
            if (it.weight <= remaining) {
                fractions[it.index] = 1.0;
                totalValue += it.value;
                remaining -= it.weight;
            } else {
                double frac = remaining / it.weight;
                fractions[it.index] = frac;
                totalValue += it.value * frac;
                remaining = 0;
            }
        }

        return new FractionResult(totalValue, fractions);
    }


    public static double greedy01(double[] values, double[] weights, double capacity) {
        if (values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Arrays de valores y pesos deben existir y tener la misma longitud.");
        }
        int n = values.length;
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(i, values[i], weights[i]);
        }
        Arrays.sort(items, Comparator.comparingDouble((Item it) -> it.ratio).reversed());

        double totalValue = 0.0;
        double remaining = capacity;
        for (Item it : items) {
            if (it.weight <= remaining) {
                totalValue += it.value;
                remaining -= it.weight;
            }
        }
        return totalValue;
    }
}



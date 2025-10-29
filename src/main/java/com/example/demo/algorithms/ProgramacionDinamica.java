
package com.example.demo.algorithms;

public class ProgramacionDinamica {

    public static class Result {
        private final int totalValue;
        private final boolean[] selected; // mismo orden que la entrada

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
     //Resuelve 0/1 Knapsack usando programación dinámica.
    public static Result knapsack01(int[] values, int[] weights, int capacity) {
        if (values == null || weights == null || values.length != weights.length) {
            throw new IllegalArgumentException("Arrays de valores y pesos deben existir y tener la misma longitud.");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("La capacidad debe ser >= 0.");
        }

        int n = values.length;
        if (n == 0 || capacity == 0) {
            return new Result(0, new boolean[0]);
        }

        // dp[i][w] = mejor valor usando primeros i ítems (i desde 0..n) con capacidad w
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            int val = values[i - 1];
            int wt = weights[i - 1];
            for (int w = 0; w <= capacity; w++) {
                // no tomar
                int noTake = dp[i - 1][w];
                int take = Integer.MIN_VALUE;
                if (wt <= w) {
                    take = dp[i - 1][w - wt] + val;
                }
                dp[i][w] = Math.max(noTake, take);
            }
        }

        // Reconstruir la selección
        boolean[] selected = new boolean[n];
        int w = capacity;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                // se tomó el ítem i-1
                selected[i - 1] = true;
                w -= weights[i - 1];
            } else {
                selected[i - 1] = false;
            }
        }

        return new Result(dp[n][capacity], selected);
    }
}

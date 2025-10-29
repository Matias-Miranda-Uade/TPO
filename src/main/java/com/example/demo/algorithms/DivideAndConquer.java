
package com.example.demo.algorithms;

public class DivideAndConquer {

    // Merge Sort
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        int[] tmp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, tmp);
    }

    private static void mergeSort(int[] arr, int left, int right, int[] tmp) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid, tmp);
        mergeSort(arr, mid + 1, right, tmp);
        merge(arr, left, mid, right, tmp);
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] tmp) {
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) tmp[k++] = arr[i++];
            else tmp[k++] = arr[j++];
        }
        while (i <= mid) tmp[k++] = arr[i++];
        while (j <= right) tmp[k++] = arr[j++];
        for (k = left; k <= right; k++) arr[k] = tmp[k];
    }

    // Búsqueda binaria (retorna índice o -1)
    public static int binarySearch(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        return binarySearch(arr, 0, arr.length - 1, target);
    }

    private static int binarySearch(int[] arr, int low, int high, int target) {
        if (low > high) return -1;
        int mid = low + (high - low) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) return binarySearch(arr, mid + 1, high, target);
        else return binarySearch(arr, low, mid - 1, target);
    }

    // Máximo subarreglo por divide & vencerás
    public static int maxSubarrayDivideAndConquer(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        return maxSubHelper(arr, 0, arr.length - 1).maxSub;
    }

    private static SubResult maxSubHelper(int[] arr, int l, int r) {
        if (l == r) {
            int v = arr[l];
            return new SubResult(v, v, v, v);
        }
        int m = l + (r - l) / 2;
        SubResult left = maxSubHelper(arr, l, m);
        SubResult right = maxSubHelper(arr, m + 1, r);

        int total = left.total + right.total;
        int maxPrefix = Math.max(left.maxPrefix, left.total + right.maxPrefix);
        int maxSuffix = Math.max(right.maxSuffix, right.total + left.maxSuffix);
        int maxSub = Math.max(Math.max(left.maxSub, right.maxSub), left.maxSuffix + right.maxPrefix);

        return new SubResult(total, maxPrefix, maxSuffix, maxSub);
    }


    private static class SubResult {
        final int total;
        final int maxPrefix;
        final int maxSuffix;
        final int maxSub;

        SubResult(int total, int maxPrefix, int maxSuffix, int maxSub) {
            this.total = total;
            this.maxPrefix = maxPrefix;
            this.maxSuffix = maxSuffix;
            this.maxSub = maxSub;
        }
    }
}

// Name: Licia Bordignon
// AndrewId: lbordign
// Project 4, Part 3 (same as part 2)

public class OptimalTSP {
    private double[][] adj;
    private int[] bestPath;
    private double bestLength;

    public OptimalTSP(double[][] adj) {
        this.adj = adj; // adjacency matrix holding the computed distance between each pair
        this.bestLength = Double.MAX_VALUE; // total distance, initialized to infinity
        this.bestPath = null; //best tour found so far
    }

    public void findOptimalTour() {
        int n = adj.length;

        // perm will contain the nodes we need to permute.
        // The array has length n-1 because it will contain all nodes except 0. Node 0 is fixed as the starting point of the TSP tour
        int[] perm = new int[n - 1];

        // as said above, we start from one because we will always start from node 0
        for (int i = 1; i < n; i++) {
            perm[i - 1] = i; // we fill the array with values 1, 2, ..., n-1 (but we store them starting from index 0 -> i-1)
        }

        permute(perm, 0); // we start this by permuting perm and holding fixed the first element of the array (index 0)
    }

    private void permute(int[] perm, int start) {
        // if start = perm.length it means we have fixed all positions in the permutation, so we now have one complete ordering of the nodes
        if (start == perm.length) {
            double currentLength = computeTourLength(perm); // and therefore we compute the total length of this tour
            // if this tour is shorter than the best one found so far, update the best solution
            if (currentLength < bestLength) {
                bestLength = currentLength; //update best length

                // update best path
                bestPath = new int[perm.length + 2];
                bestPath[0] = 0; //start is 0
                for (int i = 0; i < perm.length; i++) {
                    bestPath[i + 1] = perm[i]; // this is correct because for example: bestPath[0+1] = perm[0] -> perm does not contain 0 as start
                }
                bestPath[bestPath.length - 1] = 0; // end is also 0
            }
            return;
        }

        // Recursively, we generate all permutations by fixing one element at position "start"
        // help to build this part was used from the following website: https://www.geeksforgeeks.org/java/java-program-to-print-all-permutations-of-a-given-string/
        for (int i = start; i < perm.length; i++) {
            // By looping from "start" to the end, we try every possible choice for the element that will go in position "start".
            // In each iteration, we select a different element to fix at this position.

            swap(perm, start, i); // place perm[i] into position "start"

            // Now that position "start" is fixed, recursively generate
            // all permutations of the remaining positions (start + 1 onward)
            permute(perm, start + 1);

            // here we undo the swap to restore the original array before trying the next possibility in the loop
            swap(perm, start, i);

            //Essentially, at each step, we choose which element goes in the current position, recursively permute the rest, and then undo the choice.
        }
    }

    private double computeTourLength(int[] perm) {
        double total = 0.0;
        int previous = 0; // start at 0

        for (int i = 0; i < perm.length; i++) {
            total += adj[previous][perm[i]]; // adding all distances that are stored in the adjacency matrix
            previous = perm[i];
        }

        total += adj[previous][0]; // return to 0
        return total;
    }

    // swaps elements at index i and j in the perm array a
    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public int[] getBestPath() {
        return bestPath;
    }

    public double getBestLength() {
        return bestLength;
    }
}
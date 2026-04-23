//Name: Licia Bordignon
//AndrewId: lbordign
//Project 4, Part 1

import java.util.Scanner;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        String fileName = "CrimeLatLonXY1990.csv";

        // here I prompt the user to enter dates (assuming correct format M/D/YY)
        Scanner input = new Scanner(System.in);
        System.out.println("Enter start date:");
        String startDate = input.nextLine();
        System.out.println("Enter end date:");
        String endDate = input.nextLine();

        CrimeRow[] crimes = CrimeGraph.loadCrimesBetweenDates(fileName, startDate, endDate);
        double[][] adj = CrimeGraph.buildDistanceMatrix(crimes);


        System.out.println("Crime records between " + startDate + " and " + endDate + ":");
        for (int i = 0; i < crimes.length; i++) {
            System.out.println(i + ": " + crimes[i]);
        }

        // Build the MST starting from node 0
        ApproximateTSP prim = new ApproximateTSP(adj);
        prim.buildMST(0);

        prim.buildChildrenLists(); //building the children list
        LinkedList<Integer> approxTour = prim.getPreorderTSPTour(); //get the preorder tour
        System.out.println("Hamiltonian Cycle (not necessarily optimum):");
        for (int node : approxTour) {
            System.out.print(node + " ");
        }
        System.out.println(approxTour.getFirst()); // we manually close the tour by getting back to the first

        double length = prim.computeCycleLength(approxTour); //calculating the length (within the function we manually close the circle as well to calculate the complete distance)
        System.out.println("Length Of cycle: " + length + " miles");


        input.close();
    }

}
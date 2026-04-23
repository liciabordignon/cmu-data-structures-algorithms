//Name: Licia Bordignon
//AndrewId: lbordign

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class TwoDTreeDriver {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        TwoDTree root = new TwoDTree("CrimeLatLonXY.csv");
        System.out.println("Crime file loaded into 2d tree with "  + root.getSize() + " records.");

        while (true) {

            System.out.println("\nWhat would you like to do?");
            System.out.println("1: inorder");
            System.out.println("2: preorder");
            System.out.println("3: levelorder");
            System.out.println("4: postorder");
            System.out.println("5: reverseLevelOrder");
            System.out.println("6: search for points within rectangle");
            System.out.println("7: search for nearest neighbor");
            System.out.println("8: quit");
            System.out.print("> ");
            String choice = in.nextLine().trim();
            if (choice.equals("1")) {root.inOrderPrint();}
            else if (choice.equals("2")) {root.preOrderPrint();}
            else if (choice.equals("3")) {root.levelOrderPrint();}
            else if (choice.equals("4")) {root.postOrderPrint();}
            else if (choice.equals("5")) {root.reverseLevelOrderPrint();}
            else if (choice.equals("6")) {
                System.out.println("Enter a rectangle bottom left (X1,Y1) and top right (X2, Y2) as four doubles each separated by a space.");
                System.out.print("> ");
                String line = in.nextLine().trim();
                String[] parts = line.split("\\s+");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                System.out.println("\nSearching for points within (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")");

                ListOfCrimes crimes = root.findPointsInRange(x1,y1,x2,y2);
                System.out.println("\nExamined " + crimes.getExamined() + " nodes during search.");
                System.out.println("Found " + crimes.getSize() + " crimes.\n");
                System.out.println(crimes.toString());

                try {
                    Files.write(Paths.get("PGHCrimes.kml"), crimes.toKML().getBytes());
                    System.out.println("The crime data has been written to PGHCrimes.KML. It is viewable in Google Earth Pro.");
                } catch (IOException e) {
                    throw new RuntimeException("Error writing PGHCrimes.kml", e);
                }

            }else if (choice.equals("7")) {
                System.out.println("Enter a point to find nearest crime. Separate with a space.");
                System.out.print("> ");

                String line = in.nextLine().trim();
                while (line.isEmpty()) line = in.nextLine().trim();

                String[] parts = line.split("\\s+");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);

                Neighbor n = root.nearestNeighbor(x, y);

                System.out.println("\nLooked at " + n.getLookedAt() + " nodes in tree. Found the nearest crime at:");
                System.out.println(n.getCrime());

            } else if (choice.equals("8")) {
                System.out.println("Thank you for exploring Pittsburgh crimes in the 1990's.");
                break;
            }
        }

        in.close();
    }
}




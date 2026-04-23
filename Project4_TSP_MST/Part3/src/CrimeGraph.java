//Name: Licia Bordignon
//AndrewId: lbordign
//Project 4, Part 3 (same as part1)

import java.io.*;

public class CrimeGraph {

    /// ///////////////////////////
    /// Loading the relevant crimes
    /// ///////////////////////////
    public static CrimeRow[] loadCrimesBetweenDates(String crimeDataLocation, String startDate, String endDate) {
        // firstly I count the rows that are included in the date range
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(crimeDataLocation))) {
            String line;
            br.readLine(); // this is used to skip the header row

            while ((line = br.readLine()) != null) {
                CrimeRow crime = new CrimeRow(line.split(","));
                if (isBetween(crime.getDate(), startDate, endDate)) {
                    count++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading crime file: " + crimeDataLocation, e);
        }

        // I create an array with the exact number of selected records
        CrimeRow[] result = new CrimeRow[count]; // 1D array of crime records

        // then I populate the array with the filtered crime records
        try (BufferedReader br = new BufferedReader(new FileReader(crimeDataLocation))) {
            String line;
            br.readLine(); // this is used to skip the header row
            int i = 0;

            while ((line = br.readLine()) != null) {
                CrimeRow crime = new CrimeRow(line.split(","));
                if (isBetween(crime.getDate(), startDate, endDate)) {
                    result[i] = crime;
                    i++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading crime file: " + crimeDataLocation, e);
        }
        return result;
    }


    // Pre-condition: date, start, and end are non-null strings in the format M/D/YY
    // Post-condition: returns true if date is within the inclusive range [start, end], false otherwise
    public static boolean isBetween(String date, String start, String end) {
        int d = dateToInt(date);
        int s = dateToInt(start);
        int e = dateToInt(end);
        return d >= s && d <= e; //comparison is possible because I converted the date to integer
    }

    // Pre-condition: date is a non-null string in the format M/D/YY
    // Post-condition: returns an integer representation of the date that I then use for comparison purposes
    public static int dateToInt(String date) {
        String[] parts = date.split("/");
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return year * 10000 + month * 100 + day;
    }



    /// ///////////////////////////
    /// Building matrix (weighted -> weights = distance)
    /// ///////////////////////////
    // Pre-condition: crimes is a non-null array of CrimeRow objects
    // Post-condition: returns an n x n matrix where entry [i][j] stores the distance in miles between crimes[i] and crimes[j]
    public static double[][] buildDistanceMatrix(CrimeRow[] crimes) {
        int n = crimes.length;
        double[][] adj = new double[n][n]; // as stated in the Project 4 FAQ the graph is implemented as a two-dimensional array of doubles – holding the computed distance between each pair

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adj[i][j] = distance(crimes[i], crimes[j]);
            }
        }
        return adj;
    }

    // Pre-condition: i and j are non-null CrimeRow objects
    // Post-condition: returns the Euclidean distance in miles between the two crime locations
    public static double distance(CrimeRow i, CrimeRow j) {
        double dx = i.getX() - j.getX();
        double dy = i.getY() - j.getY();
        return Math.sqrt(dx * dx + dy * dy) * 0.00018939;
    }

}



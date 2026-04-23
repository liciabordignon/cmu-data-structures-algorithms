//Name: Licia Bordignon
//AndrewId: lbordign
//Project 4, Part 3

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class KML {

    public static void writeKML(CrimeRow[] crimes,
                                LinkedList<Integer> approxTour,
                                int[] optimalTour,
                                String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            bw.write("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
            bw.write("<Document>\n");
            bw.write("<name>Pittsburgh TSP</name><description>TSP on Crime</description>\n");

            // Style for prim TSP path
            bw.write("<Style id=\"style6\">\n");
            bw.write("<LineStyle>\n");
            bw.write("<color>ff00ff00</color>\n");
            bw.write("<width>7</width>\n");
            bw.write("</LineStyle>\n");
            bw.write("</Style>\n");

            // Style for optimal TSP path
            bw.write("<Style id=\"style5\">\n");
            bw.write("<LineStyle>\n");
            bw.write("<color>ff0000ff</color>\n");
            bw.write("<width>7</width>\n");
            bw.write("</LineStyle>\n");
            bw.write("</Style>\n");

            // Approximate path
            bw.write("<Placemark>\n");
            bw.write("<name>TSP Path</name>\n");
            bw.write("<description>TSP Path</description>\n");
            bw.write("<styleUrl>#style6</styleUrl>\n");
            bw.write("<LineString>\n");
            bw.write("<tessellate>1</tessellate>\n");
            bw.write("<coordinates>\n");
            for (int node : approxTour) {
                CrimeRow crime = crimes[node];
                bw.write(crime.getLongitude() + "," + crime.getLatitude() + ",0.000000\n");
            }
            // we manually need to close the tour and get back to the first
            CrimeRow firstApprox = crimes[approxTour.getFirst()];
            bw.write(firstApprox.getLongitude() + "," + firstApprox.getLatitude() + ",0.000000\n");

            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");

            // Optimal path with offset so one line will not completely cover the other
            bw.write("<Placemark>\n");
            bw.write("<name>Optimal Path</name>\n");
            bw.write("<description>Optimal Path</description>\n");
            bw.write("<styleUrl>#style5</styleUrl>\n");
            bw.write("<LineString>\n");
            bw.write("<tessellate>1</tessellate>\n");
            bw.write("<coordinates>\n");

            for (int node : optimalTour) {
                CrimeRow crime = crimes[node];
                double longitude = crime.getLongitude() + 0.001;
                double latitude = crime.getLatitude() - 0.001;
                bw.write(longitude + "," + latitude + ",0.000000\n");
            }

            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");

            bw.write("</Document>\n");
            bw.write("</kml>\n");

        } catch (IOException e) {
            throw new RuntimeException("Error writing KML file: " + fileName, e);
        }
    }
}
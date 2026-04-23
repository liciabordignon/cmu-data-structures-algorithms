// Name: Licia Bordignon
// AndrewId: lbordign

import java.io.*;
import java.util.*;

public class InputDemo {
    // Red-Black Tree is used as a dictionary (course name -> id)
    private static final RedBlackTree dict = new RedBlackTree();
    // Array to convert id back to course name (id -> course)
    private static final String[] idToCourse = new String[20];
    private static int nextId = 0; // Next available id for a new course (start form 0)

    private static final int[][] adj = new int[20][20]; //I create a matrix of all zeros 20x20 (max number of courses offered each term = 20)
    // When adj[i][j] = 1 it means course i conflicts with course j

    // Processes one line from the input file (one student)
    public static void processLine(String line) {
        StringTokenizer st;
        st = new StringTokenizer(line, " \t");

        String student = st.nextToken(); // first token = student name
        int nCourses = Integer.parseInt(st.nextToken()); //second = number of courses the student takes (max= 5)

        // Array in which we will store the ids of this student's courses
        int[] ids = new int[nCourses];
        // we loop through the number of courses, for every course we get its id (or create one if new)
        for (int i = 0; i < nCourses; i++) {
            String course = st.nextToken();
            ids[i] = getOrCreateCourseId(course);
        }

        // building the adjacency matrix: every pair of courses taken by this student creates a conflict (edge)
        for (int i = 0; i < nCourses; i++) {
            for (int j = i + 1; j < nCourses; j++) {
                int a = ids[i], b = ids[j];
                //both ways (undirected)
                adj[a][b] = 1;
                adj[b][a] = 1;
            }
        }
    }

    // returns existing id or creates new id and inserts into RB tree
    private static int getOrCreateCourseId(String course) {
        Integer id = dict.getValue(course); //we look if  the course id is already in the tree
        if (id != null) return id; //if it is (id != null) we return its id


        int newId = nextId; //nextId was initalized at 0. Therefore, the first course we encounter will be assigned the id = 0.
        dict.insert(course, newId);
        idToCourse[newId] = course;
        nextId++; // We increase nextId to 1. The next new course will be 1 and so on.
        return newId;
    }

    // method to print the courses and their ids
    private static void printCourseIds() {
        System.out.println("\nCourse IDs:");
        for (int i = 0; i < nextId; i++) {
            System.out.println(i + " -> " + idToCourse[i]);
        }
    }

    // method to print the adjacency matrix
    private static void printAdjacencyMatrix() {
        System.out.println("\nAdjacency Matrix:");
        System.out.print("    ");
        // Print column headers
        for (int j = 0; j < nextId; j++) System.out.print(j+"  ");
        System.out.println();

        // Print each row of the matrix
        for (int i = 0; i < nextId; i++) {
            System.out.print(i+" "); //row header
            for (int j = 0; j < nextId; j++) {
                System.out.print("  "+ adj[i][j]); // either one or zero
            }
            System.out.println();
        }
    }

    // Applies greedy graph coloring to schedule exams
    // Each "color" represents one exam period. Courses in the same period do not conflict (= each student enrolled in those courses does not have other exams in that period)
    private static void doGreedyColoring() {

        int C = nextId; // number of vertices (courses)
        boolean[] colored = new boolean[C]; // colored[v] = true means course v has been scheduled
        int remaining = C; // how many courses are still not scheduled
        int period = 0;

        // we repeat until every course has been assigned to some exam period
        while (remaining > 0) {

            period++;
            List<Integer> newclr = new ArrayList<>();  // newclr stores a list of courses that can share the same exam period (same color)

            // we try to add to newclr as many courses as we can
            // to do so, we look in a greedy way at every course, add those with no conflict with other courses already added to newclr
            for (int v = 0; v < C; v++) {

                if (colored[v]) continue; // skip already colored courses (already scheduled)

                boolean found = false; // becomes true if we find a conflict within this period

                // we check v against every course already in this period (newclr)
                for (int w : newclr) {
                    if (adj[v][w] == 1) {   // if there is an edge between v and w
                        found = true; // we found a conflict: v cannot be added to this period
                        break;
                    }
                }

                // if found == false -> no conflicts-> we can schedule v in this period
                if (!found) {
                    colored[v] = true;   // we mark v as colored (scheduled)
                    newclr.add(v);       // and add v to newclr
                    remaining--;
                }
            }

            // Print this color (exam period)
            System.out.print("\nFinal Exam Period " + period + " => ");
            for (int v : newclr) {
                System.out.print(idToCourse[v] + " ");
            }
        }
        // the while loop then stars again to create the next exam period (newclr), scheduling only the remaining courses (colored[v]= false)
    }


    public static void main(String args[]) {
        try {
            BufferedReader in =
                    new BufferedReader(
                            new FileReader(args[0]));
            String line;
            line = in.readLine();
            while (line != null) {
                processLine(line);
                line = in.readLine();
            }
        }
        catch (IOException e) {
            System.out.println("IO Exception");
        }


        printCourseIds();
        printAdjacencyMatrix();
        doGreedyColoring();
    }
}

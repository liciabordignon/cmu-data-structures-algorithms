//Name: Licia Bordignon
//AndrewId: lbordign

public class ListOfCrimes {

    private static class Node {
        CrimeRow crime;
        Node next;

        // pre-condition: crime is a valid CrimeRow object
        // post-condition: a Node object is created and it stores the given crime record. The next reference is initialized to null.
        Node(CrimeRow crime) {
            this.crime = crime;
            this.next = null;
        }
    }

    private Node head;
    private int size;
    private int examined;

    // pre-condition: none
    // post-condition: an empty list is created
    public ListOfCrimes() {
        head = null;
        size = 0;
        examined = 0;
    }

    // pre-condition: crime is a valid CrimeRow object
    // post-condition: crime is added to the end of the list
    // This implementation is based on the method presented in the solutions to Quiz1
    public void append(CrimeRow crime) {
        Node n = new Node(crime);

        if (head == null) {
            head = n;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = n;
        }
        size++;
    }

    // pre-condition: the list has been constructed
    // post-condition: returns the number of crimes stored in the list
    public int getSize() {return size;}
    // pre-condition: the list has been constructed
    // post-condition: increases the number of crimes examined
    public void incrementExamined() { examined++; }
    // pre-condition: the list has been constructed
    // post-condition: returns the number of crimes examined
    public int getExamined() { return examined; }


    // pre-condition: the list has been constructed
    // post-condition: returns a String containing all crimes in the list
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;

        while (current != null) {
            sb.append(current.crime).append("\n");
            current = current.next;
        }
        return sb.toString();
    }


    // pre-condition: the list has been constructed
    // post-condition: returns a String containing a KML representation of all crimes in the list
    public String toKML() {

        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        sb.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
        sb.append("<Document>\n");

        sb.append(" <Style id=\"style1\">\n");
        sb.append("  <IconStyle>\n");
        sb.append("   <Icon>\n");
        sb.append("    <href>http://maps.gstatic.com/intl/en_ALL/mapfiles/ms/micons/blue-dot.png</href>\n");
        sb.append("   </Icon>\n");
        sb.append("  </IconStyle>\n");
        sb.append(" </Style>\n");

        Node current = head;
        while (current != null) {
            CrimeRow c = current.crime;

            sb.append(" <Placemark>\n");
            sb.append("  <name>").append(c.getOffense()).append("</name>\n");
            sb.append("  <description>").append(c.getStreet()).append("</description>\n");
            sb.append("  <styleUrl>#style1</styleUrl>\n");
            sb.append("  <Point>\n");
            sb.append("   <coordinates>")
                    .append(c.getLongitude()).append(",")
                    .append(c.getLatitude())
                    .append(",0.000000</coordinates>\n");
            sb.append("  </Point>\n");
            sb.append(" </Placemark>\n");

            current = current.next;
        }

        sb.append("</Document>\n");
        sb.append("</kml>");

        return sb.toString();
    }
}
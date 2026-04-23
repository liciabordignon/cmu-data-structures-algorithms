//Name: Licia Bordignon
//AndrewId: lbordign
//Project 4, Part 1

public class CrimeRow {
    private int index;
    private double x;
    private double y;
    private int time;
    private String street;
    private String offense;
    private String date;
    private int tract;
    private double latitude;
    private double longitude;


    // Pre-condition: row is not null and contains 9 elements, specifically:
    // - row[0], row[1], row[7], and row[8] contain double values
    // - row[2] and row[6] contain valid integer values
    // Post-condition: a CrimeRow object is created with the specified fields
    public CrimeRow(String[] row) {
        this.x = Double.parseDouble(row[0]);
        this.y = Double.parseDouble(row[1]);
        this.time = Integer.parseInt(row[2]);
        this.street = row[3];
        this.offense = row[4];
        this.date = row[5];
        this.tract = Integer.parseInt(row[6]);
        this.latitude = Double.parseDouble(row[7]);
        this.longitude = Double.parseDouble(row[8]);
        this.index = -1; //assigning indexes to crimes
    }

    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the X coordinate of this crime record
    public double getX() { return x; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the Y coordinate of this crime record
    public double getY() { return y; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the latitude of this crime record
    public double getLatitude() { return latitude; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the longitude of this crime record
    public double getLongitude() { return longitude; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the offense of this crime record
    public String getOffense() { return offense; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the street of this crime record
    public String getStreet() { return street; }
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the date of this crime record
    public String getDate() {return date;}

    // Pre-condition: index is a vertex number
    // Post-condition: assigns an index to this crime record
    public void setIndex(int index) {this.index = index;}
    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns the index of this crime record
    public int getIndex() {return index;}



    // Pre-condition: the CrimeRow object has been correctly constructed
    // Post-condition: returns a String containing all fields of the crime record
    @Override
    public String toString() {
        return x + "," + y + "," + time + "," + street + "," +
                offense + "," + date + "," + tract + "," +
                latitude + "," + longitude;
    }
}

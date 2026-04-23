//Name: Licia Bordignon
//AndrewId: lbordign

public class Neighbor {

    private CrimeRow crime;
    private double distance;
    private int lookedAt;

    // pre-condition: none
    // post-condition: a Neighbor object is created with distance set to positive infinity (max)
    public Neighbor() {
        crime = null;
        distance = Double.POSITIVE_INFINITY;
        lookedAt = 0;
    }

    // pre-condition: the Neighbor object has been constructed
    // post-condition: increases the number of nodes looked at
    public void incrementLookedAt() { lookedAt++; }

    // pre-condition: the Neighbor object has been constructed
    // post-condition: returns the number of nodes looked at
    public int getLookedAt() { return lookedAt; }

    // pre-condition: the Neighbor object has been constructed
    // post-condition: returns the stored crime record
    public CrimeRow getCrime() {return crime;}

    // pre-condition: the Neighbor object has been constructed
    // post-condition: returns the stored distance
    public double getDistance() {return distance;}

    // pre-condition: crime is a valid CrimeRow object
    // post-condition: updates the stored crime record
    public void setCrime(CrimeRow crime) {this.crime = crime;}

    // pre-condition: distance >= 0
    // post-condition: updates the stored distance
    public void setDistance(double distance) {this.distance = distance;}
}

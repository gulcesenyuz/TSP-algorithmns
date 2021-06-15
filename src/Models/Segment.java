package Models;

public class Segment {
    private Town town1;
    private Town town2;
    private double distance;

    public Segment(Town town1, Town town2, double length) {
        this.town1 = town1;
        this.town2 = town2;
        this.distance = length;
    }

    public Town takeTown1() {
        return town1;
    }

    public Town takeTown2() {
        return town2;
    }

    public double takeDistance() {
        return distance;
    }

}

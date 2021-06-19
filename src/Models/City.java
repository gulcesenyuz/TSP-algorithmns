package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class City implements Serializable {

    public double x;
    public double y;

    private int con1 = -1;
    private int con2 = -1;

    private final String isim;
    private final int num;

    public City(int num, String isim, String x, String y) {
        this.isim = isim;
        this.x = Double.parseDouble(x);
        this.y = Double.parseDouble(y);
        this.num = num;
    }

    public double dictanceTo(City town) {
        return Math.sqrt((town.y - this.y) * (town.y - this.y) + (town.x - this.x) * (town.x - this.x));
    }

    public City closest(ArrayList<City> list) {
        // create new object to store smallest city
        City smallest;
        do {
            // get a random city from the list to compare
            Random random = new Random();
            int index = random.nextInt(list.size());
            smallest = list.get(index);
        } while (smallest == this);

        for (City object : list) {
            if (!(this == object)) {
                // System.out.println(dictanceTo(object));
                // compare cities and store smallest one in smallest object
                if (this.dictanceTo(object) < this.dictanceTo(smallest)) {
                    smallest = object;
                }
            }
        }
        return smallest;
    }

    public String toString() {
        return isim;
    }

    public String takeName() {
        return isim;
    }

    public int getID() {
        return num;
    }

    public double takeX() {
        return x;
    }

    public double takeY() {
        return y;
    }

}

package Models;

import java.util.ArrayList;
import java.util.Random;

public class City {

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

    public int getCon1() {
        return con1;
    }

    public int getCon2() {
        return con2;
    }

    public void plug(City town) {
        if (town.isConnectionAvaible() || town.con1 == this.getID() || town.con2 == this.getID()) {
            if (!isConnected(con1)) {
                con1 = town.getID();

            } else if (!isConnected(con2) || town.con1 == this.getID() || town.con2 == this.getID()) {
                con2 = town.getID();
            }
        }

    }

    public void unplug(City town) {
        var a = town.getID();
        if (a == con1) {
            con1 = -1;
        } else if (a == con2) {
            con2 = -1;
        }
        // System.out.println(this.id+" to "+a+" disconnected.");
    }

    private boolean isConnected(int con) {
        if (con == -1)
            return false;
        else
            return true;
    }

    public boolean isConnectionAvaible() {
        if (isConnected(con1) && isConnected(con2))
            return false;
        else
            return true;
    }

    public String toString() {
        return isim + "->";
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

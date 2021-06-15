import java.io.*;
import java.time.Duration;
import java.time.Instant;

import java.util.*;

import Models.Segment;
import Models.Town;

public class Main {

    static String file = "C:/Users/gizem/Desktop/ca4663.tsp";

    public static void main(String[] args) throws IOException {

        ArrayList<Town> cities = dataInput(file, false);

        Instant start = Instant.now();
        System.out.println("Greedy Algorithm");
        greedy(cities);
        Instant end = Instant.now();
        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
        System.out.println();
        start = Instant.now();
        System.out.println("Nearest Neighbor  :\n" + totalDistanceCalculator(NearestNeigborTownFinder(cities, 6)));
        end = Instant.now();
        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
        System.out.println();
        System.out.println("Divide and Conquer ");
        start = Instant.now();
        System.out.println(totalDistanceCalculator(divideAndConquer(cities)));
        end = Instant.now();
        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
    }

    public static ArrayList<Town> divideAndConquer(ArrayList imp_cities) {
        int totalX = 0;
        int totalY = 0;
        ArrayList<Town> towns = imp_cities;
        ArrayList<Town> towns2 = new ArrayList<Town>();
        ArrayList<Town> towns1 = new ArrayList<Town>();
        for (Town object : towns) {
            totalX += (int) object.takeX();

            totalY += (int) object.takeY();

        }
        int centerX = totalX / towns.size();
        // int centerY = totalY / towns.size();

        for (Town object : towns) {
            if (object.takeX() >= centerX) {
                towns2.add(object);

            } else
                towns1.add(object);

        }

        Random random = new Random();

        for (int k = 0; k < towns1.size(); k++) {
            ArrayList<Town> test = (ArrayList<Town>) towns1.clone();
            int length1 = totalDistanceCalculator(towns1);
            int random1 = random.nextInt(test.size());
            int random2 = random.nextInt(test.size());

            Town first = test.get(random1);
            Town second = test.get(random2);

            test.set(random2, first);
            test.set(random1, second);
            int length2 = totalDistanceCalculator(test);

            if (length2 < length1) {
                towns1 = (ArrayList<Town>) test.clone();
                k = 0;

            } else {
                test = null;
            }

        }
        for (int k = 0; k < towns2.size(); k++) {
            ArrayList<Town> test = (ArrayList<Town>) towns2.clone();
            int length1 = totalDistanceCalculator(towns2);
            int random1 = random.nextInt(test.size());
            int random2 = random.nextInt(test.size());
            Town first = test.get(random1);
            Town second = test.get(random2);
            test.set(random2, first);
            test.set(random1, second);
            int length2 = totalDistanceCalculator(test);

            if (length2 < length1) {
                towns2 = (ArrayList<Town>) test.clone();
                k = 0;

            } else {
                test = null;
            }

        }
        double a = towns1.get(0).dictanceTo(towns2.get(0));
        double b = towns1.get(0).dictanceTo(towns2.get(towns2.size() - 1));
        double c = towns1.get(towns1.size() - 1).dictanceTo(towns2.get(0));
        double d = towns1.get(towns1.size() - 1).dictanceTo(towns2.get(towns2.size() - 1));

        if (a < b && a < c && a < d) {
            for (Town object : towns2) {
                towns1.add(0, object);
            }
        }
        if (b < a && b < c && b < d) {
            for (int i = towns2.size(); i > 0; i--) {
                towns1.add(0, towns2.get(i - 1));
            }
        }
        if (c < a && c < b && c < d) {
            for (Town object : towns2) {
                towns1.add(towns1.size(), object);
            }
        }
        if (d < a && d < b && d < c) {
            for (int i = towns2.size(); i > 0; i--) {
                towns1.add(towns1.size(), towns2.get(i - 1));
            }
        }

        return towns1;

    }

    public static ArrayList<Town> greedy(ArrayList<Town> imp_cities) {
        ArrayList<Town> citiess = (ArrayList<Town>) imp_cities.clone();
        ArrayList<Segment> segmentArrayList = shortestWayComperator(citiess);
        int totalDist = 0;

        for (int i = 0; i < segmentArrayList.size(); i++) {
            Town town1 = segmentArrayList.get(i).takeTown1();
            Town town2 = segmentArrayList.get(i).takeTown2();
            if (town1.isConnectionAvaible() && town2.isConnectionAvaible()) {
                totalDist += segmentArrayList.get(i).takeDistance();
                town1.plug(town2);
                town2.plug(town1);
                int next = citiess.indexOf(town1);
                int num = -1;
                int tour = 0;
                while (true) {

                    var c = citiess.get(next).getID();
                    var con1 = citiess.get(next).getCon1();
                    var con2 = citiess.get(next).getCon2();
                    if (con1 == -1 || con2 == -1)
                        break;
                    if (!(num == -1) && con1 == num)
                        next = con2;
                    else
                        next = con1;
                    num = c;

                    if (citiess.indexOf(town1) == next && !(tour == 0)) {
                        // System.out.println("Yapılmaması gerken bağlantı bulundu");
                        town1.unplug(town2);
                        town2.unplug(town1);
                        totalDist -= segmentArrayList.get(i).takeDistance();
                        break;
                    }

                    tour++;

                }

            }

        }
        ArrayList<Town> lasts = new ArrayList<>();
        for (Town object1 : citiess) {
            if (object1.getCon2() == -1) {
                lasts.add(object1);
            }
        }

        lasts.get(0).plug(lasts.get(1));
        lasts.get(1).plug(lasts.get(0));

        totalDist += lasts.get(0).dictanceTo(lasts.get(1));
        System.out.println(totalDist);
        return citiess;
    }

    public static ArrayList<Segment> shortestWayComperator(ArrayList<Town> imp_cities) {
        ArrayList<Town> cities = (ArrayList<Town>) imp_cities.clone();
        ArrayList<Segment> segmentArrayList = new ArrayList<Segment>();
        for (Town object : cities) {
            for (Town object2 : cities) {
                if (object == object2) {
                    continue;
                }
                segmentArrayList.add(new Segment(object, object2, object.dictanceTo(object2)));

            }
        }

        Collections.sort(segmentArrayList, new Comparator<Segment>() {
            @Override
            public int compare(Segment p1, Segment p2) {
                if (p1.takeDistance() < p2.takeDistance())
                    return -1;
                if (p1.takeDistance() > p2.takeDistance())
                    return 1;
                return 0;

            }
        });

        ArrayList<Segment> cleanedSegmentArrayList = new ArrayList<Segment>();
        for (int k = 0; k < segmentArrayList.size(); k++) {
            if (k % 2 == 0) {
                cleanedSegmentArrayList.add(segmentArrayList.get(k));
            }

        }

        return cleanedSegmentArrayList;
    }

    public static ArrayList<Town> NearestNeigborTownFinder(ArrayList<Town> unvisitedCitiesa, int start) {
        ArrayList<Town> unvisitedCities = (ArrayList<Town>) unvisitedCitiesa.clone();
        ArrayList<Town> visitedCities = new ArrayList<Town>();
        int unvisetedSize = unvisitedCities.size();
        Town a = unvisitedCities.get(start);
        Town b;
        for (int z = 0; z < unvisetedSize; z++) {
            if (unvisitedCities.size() > 1) {
                b = a.closest(unvisitedCities);
                visitedCities.add(a);
                unvisitedCities.remove(a);
                // System.out.println(a.getName());
                a = b;
            } else {

                unvisitedCities.remove(a);
                visitedCities.add(a);

            }

        }
        return visitedCities;
    }

    public static int totalDistanceCalculator(ArrayList<Town> visitedCities) {
        int visitedSize = visitedCities.size();
        int totalDistance = 0;
        for (int z = 0; z < visitedSize; z++) {
            if (!(z == (visitedSize - 1))) {
                totalDistance += visitedCities.get(z).dictanceTo(visitedCities.get(z + 1));

            } else {

                totalDistance += visitedCities.get(z).dictanceTo(visitedCities.get(0));

            }
        }

        return totalDistance;
    }

    private static ArrayList<Town> dataInput(String file, boolean print) {
        ArrayList<Town> citiesArr = new ArrayList<Town>();
        File f = new File(file);

        try {
            Scanner input = new Scanner(f);
            String x_cord;
            String y_cord;
            String name;
            int id;

            for (int i = 0; i < 6; i++) {
                input.nextLine();
            }
            while (input.hasNext()) {
                name = input.next();
                id = Integer.parseInt(name) - 1;
                x_cord = input.next();
                y_cord = input.next();
                citiesArr.add(new Town(id, name, x_cord, y_cord));
            }
        } catch (IOException e) {
            // Handle error...
        }
        Collections.sort(citiesArr, new Comparator<Town>() {
            @Override
            public int compare(Town o1, Town o2) {
                return Integer.parseInt(o1.takeName()) - Integer.parseInt(o2.takeName());

            }
        });
        if (print)
            System.out.println(citiesArr);

        return citiesArr;
    }
}

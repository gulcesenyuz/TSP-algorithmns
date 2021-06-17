import java.io.*;
import java.time.Duration;
import java.time.Instant;

import java.util.*;

import Models.City;

public class Main {

    static String file = "C:/Users/gizem/Desktop/ca4663.tsp";

    public static void main(String[] args) throws IOException {

        ArrayList<City> cities = dataInput(file, false);
        Instant start = Instant.now();
        System.out.println("Greedy Algorithm \n" + "Shortest Distance:  ");
        greedy(cities);
        Instant end = Instant.now();
        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
        System.out.println();
        start = Instant.now();
        System.out.println("Nearest Neighbor\n" + "Shortest Distance:  "
                + totalDistanceCalculator(NearestNeigborTownFinder(cities)));
        end = Instant.now();

        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
        System.out.println();
        System.out.println("Divide and Conquer ");
        start = Instant.now();
        System.out.println("Shortest Distance:  " + totalDistanceCalculator(divideAndConquer(cities)));
        end = Instant.now();
        System.out.print("Total time elapsed :");
        System.out.println(Duration.between(start, end));
    }

    public static ArrayList<City> divideAndConquer(ArrayList imp_cities) {
        int totalX = 0;
        int totalY = 0;
        ArrayList<City> towns = imp_cities;
        ArrayList<City> towns2 = new ArrayList<City>();
        ArrayList<City> towns1 = new ArrayList<City>();
        for (City object : towns) {
            // calculate total x and y coordinates distance.
            totalX += (int) object.takeX();
            totalY += (int) object.takeY();

        }
        // calculate middle point of total X coordinate distance
        int centerX = totalX / towns.size();
        // int centerY = totalY / towns.size();

        for (City object : towns) {
            // for each data of city list towms, compare its X coordinate with middle point
            if (object.takeX() >= centerX) {
                // if X coordinate is greather than middle add cit to town2
                towns2.add(object);

            } else
                // if X coordinate is smaller than middle add cit to town1

                towns1.add(object);
        }

        Random random = new Random();

        for (int k = 0; k < towns1.size(); k++) {
            ArrayList<City> test = (ArrayList<City>) towns1.clone();
            int length1 = totalDistanceCalculator(towns1);
            int random1 = random.nextInt(test.size());
            int random2 = random.nextInt(test.size());
            // get random 2 city
            City first = test.get(random1);
            City second = test.get(random2);
            // replace 2 city to compare total ditance
            test.set(random2, first);
            test.set(random1, second);
            int length2 = totalDistanceCalculator(test);
            // if the new list after swapping is smaller then store the list in town1
            if (length2 < length1) {
                towns1 = (ArrayList<City>) test.clone();
                k = 0;

            } else {
                test = null;
            }

        }
        for (int k = 0; k < towns2.size(); k++) {
            ArrayList<City> test = (ArrayList<City>) towns2.clone();
            int length1 = totalDistanceCalculator(towns2);
            int random1 = random.nextInt(test.size());
            int random2 = random.nextInt(test.size());
            City first = test.get(random1);
            City second = test.get(random2);
            test.set(random2, first);
            test.set(random1, second);
            int length2 = totalDistanceCalculator(test);

            if (length2 < length1) {
                towns2 = (ArrayList<City>) test.clone();
                k = 0;

            } else {
                test = null;
            }

        }
        // calculate 2 list's elements distance
        double a = towns1.get(0).dictanceTo(towns2.get(0));
        double b = towns1.get(0).dictanceTo(towns2.get(towns2.size() - 1));
        double c = towns1.get(towns1.size() - 1).dictanceTo(towns2.get(0));
        double d = towns1.get(towns1.size() - 1).dictanceTo(towns2.get(towns2.size() - 1));
        // add cities in order after comparing the values
        if (a < b && a < c && a < d) {
            for (City object : towns2) {
                towns1.add(0, object);
            }
        }
        if (b < a && b < c && b < d) {
            for (int i = towns2.size(); i > 0; i--) {
                towns1.add(0, towns2.get(i - 1));
            }
        }
        if (c < a && c < b && c < d) {
            for (City object : towns2) {
                towns1.add(towns1.size(), object);
            }
        }
        if (d < a && d < b && d < c) {
            for (int i = towns2.size(); i > 0; i--) {
                towns1.add(towns1.size(), towns2.get(i - 1));
            }
        }

        System.out.println("Divide And Conquer Tour Path :  " + towns1.toString());

        return towns1;

    }

    public static void greedy(ArrayList<City> imp_cities) {
        ArrayList<City> citiess = (ArrayList<City>) imp_cities.clone();
        int totalDist = 0;
        ArrayList<City> solutionCities = new ArrayList<City>();
        int random = (int) (Math.random() * citiess.size());
        // choose a random start node
        City currentTown = imp_cities.get(random);
        solutionCities.add(currentTown);
        citiess.remove(random);

        while (citiess.size() > 0) {
            currentTown = solutionCities.get(solutionCities.size() - 1);
            City nearestNeighbor = getNearestNeighbor(currentTown, citiess);
            double minDistance = distance(currentTown, nearestNeighbor);
            solutionCities.add(nearestNeighbor);
            citiess.remove(nearestNeighbor);
            totalDist += minDistance;
        }
        totalDist += distance(solutionCities.get(solutionCities.size() - 1), solutionCities.get(0));

        System.out.println(solutionCities.toString());
        System.out.println("Distance: " + totalDist);
    }

    public static ArrayList<City> NearestNeigborTownFinder(ArrayList<City> CityList) {
        // clone the input file into an arraylist that we created as unvisitedCities.
        ArrayList<City> city_unvisited = (ArrayList<City>) CityList.clone();
        // arraylist to store the cities visited
        ArrayList<City> city_visited = new ArrayList<City>();
        int unvisetedSize = city_unvisited.size();
        // get a random city id to define start point
        Random random = new Random();
        int start = random.nextInt(city_unvisited.size());
        City current = city_unvisited.get(start);
        City next;
        for (int z = 0; z < unvisetedSize; z++) {
            if (city_unvisited.size() > 1) {
                // calculate closest point of current city
                next = current.closest(city_unvisited);
                city_visited.add(current);
                city_unvisited.remove(current);
                // change next city to current
                current = next;
            } else {
                city_unvisited.remove(current);
                city_visited.add(current);

            }

        }
        System.out.println("Nearest Neigbor Tour Path:  " + city_visited.toString());

        return city_visited;
    }

    public static int totalDistanceCalculator(ArrayList<City> visitedCities) {
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

    private static ArrayList<City> dataInput(String file, boolean print) {

        ArrayList<City> cities = new ArrayList<City>();
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
                cities.add(new City(id, name, x_cord, y_cord));
            }
        } catch (IOException e) {
            // Handle error...
        }
        Collections.sort(cities, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return Integer.parseInt(o1.takeName()) - Integer.parseInt(o2.takeName());

            }
        });
        if (print)
            System.out.println(cities);

        return cities;
    }

    public static City getNearestNeighbor(City currentNode, ArrayList<City> nodes) {
        City nearestNeighbor = null;
        double minDistance = Double.MAX_VALUE;
        for (int k = 0; k < nodes.size(); k++) {
            City neighbor = nodes.get(k);
            double dist = distance(currentNode, neighbor);
            // NOTE: using dist<= minDistance in the below conditional can yield different
            // solution

            if (dist < minDistance) {
                minDistance = dist;
                nearestNeighbor = neighbor;
            }
        }
        return nearestNeighbor;
    }

    public static double distance(City a, City b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }
}

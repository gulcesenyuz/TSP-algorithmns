package functions;

public  static int totalDistanceCalculator(ArrayList<Town> visitedCities) {
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
import java.util.ArrayList;
import java.util.Random;

public class DivideAndConquer {

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

}

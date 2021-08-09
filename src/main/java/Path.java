import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Data
@NoArgsConstructor
public class Path {
    private List<City> cityList;
    private static Double[][] distMap;
    private double value;
    private static Random rand = new Random();

    public void culDistMap() {
        distMap = new Double[cityList.size()][cityList.size()];
        for (int i = 0; i < cityList.size(); i++) {
            for (int j = 0; j < cityList.size(); j++) {
                double x1 = cityList.get(i).x;
                double y1 = cityList.get(i).y;
                double x2 = cityList.get(j).x;
                double y2 = cityList.get(j).y;
                distMap[i][j] = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
            }
        }
    }

    public Double getDist(City c1, City c2) {
        return distMap[c1.id - 1][c2.id - 1];
    }

    public void culValue() {
        double val = 0;
        for (int i = 0; i < cityList.size() - 1; i++) {
            val += getDist(cityList.get(i), cityList.get(i + 1));
        }
        val += getDist(cityList.get(0), cityList.get(cityList.size() - 1));
        this.value = val;
    }

    public void readFileAndLoad(String fileName, int skip) {
        String[] nameSplit = fileName.split("\\.");
        String type = nameSplit[nameSplit.length - 1];

        setCityList(new ArrayList<City>());
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(
                    URLDecoder.decode(this.getClass().getResource(fileName).getPath(), "utf-8")
            )));
            String line;
            while ((line = in.readLine()) != null && !"EOF".equals(line)) {
                if (skip != 0) {
                    skip--;
                    continue;
                }
                String[] split = null;
                if ("csv".equals(type)) {
                    split = line.split(",");
                } else if ("tsp".equals(type)) {
                    split = line.split(" ");
                }
                City city = new City(Integer.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]));
                cityList.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.culDistMap();
        this.culValue();
    }

    public void shuffle() {
        Collections.shuffle(cityList);
        this.culValue();
    }

    public Path getNewPath_1() {
        Path newPath = new Path();
        newPath.setCityList(new ArrayList<>());
        for (City city : cityList) {
            newPath.getCityList().add(city);
        }
        int a = rand.nextInt(newPath.cityList.size());
        int b = rand.nextInt(newPath.cityList.size());
        Collections.swap(newPath.getCityList(), a, b);
        newPath.culValue();
        return newPath;
    }

    public Path getNewPath_2() {
        int[] c = new int[3];
        c[0] = rand.nextInt(this.cityList.size());
        c[1] = rand.nextInt(this.cityList.size());
        c[2] = rand.nextInt(this.cityList.size());
        Arrays.sort(c);
        List<City> arr = new ArrayList<City>();
        for (int i = 0; i < c[0]; i++) {
            arr.add(this.getCityList().get(i));
        }
        for (int i = c[1]; i < c[2]; i++) {
            arr.add(this.getCityList().get(i));
        }
        for (int i = c[0]; i < c[1]; i++) {
            arr.add(this.getCityList().get(i));
        }
        for (int i = c[2]; i < this.cityList.size(); i++) {
            arr.add(this.getCityList().get(i));
        }
        Path newPath = new Path();
        newPath.setCityList(arr);
        newPath.culValue();
        return newPath;
    }

    public Path getNewPath_3() {
        Path newPath = new Path();
        newPath.setCityList(new ArrayList<>());
        for (City city : cityList) {
            newPath.getCityList().add(city);
        }
        Integer a = rand.nextInt(this.cityList.size());
        Integer b = rand.nextInt(this.cityList.size());
        Integer i = Math.min(a, b);
        Integer j = Math.max(a, b);
        while (i < j) {
            Collections.swap(newPath.getCityList(), i, j);
            i++;
            j--;
        }
        newPath.culValue();
        return newPath;
    }

    public Path getNewPath() {
        double r = 0.33;
        double p = Math.random();
        if (p < r) {
            return getNewPath_1();
        } else if (p < 2 * r) {
            return getNewPath_2();
        } else {
            return getNewPath_3();
        }
    }

    public void print() {
        for (City city : this.getCityList()) {
            System.out.println(city.id+" "+city.x+" "+city.y);
        }
    }
}

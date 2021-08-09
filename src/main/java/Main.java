public class Main {

    static Double T0 = 1000.0;
    static Double T = T0;
    static Double alpha = 0.95;
    static Integer gen = 5000;
    static Integer Lk = 1000;
    static String fileName = "wi29.tsp";
    static Integer skipLine = 7;

    public static void main(String[] args) {
        Path path = new Path();
        path.readFileAndLoad(fileName, skipLine);
        for (Integer iter = 0; iter < gen; iter++) {
            for (Integer i = 0; i < Lk; i++) {
                Path newPath = path.getNewPath();
                if (newPath.getValue() < path.getValue()) {
                    path = newPath;
                } else {
                    Double p = Math.exp(-(newPath.getValue() - path.getValue()) / T);
                    if (Math.random() < p) {
                        path = newPath;
                    }
                }
            }
            if(iter % 20 == 0) {
                System.out.println("——————————————————————————————");
                System.out.println("当前迭代次数：" + iter + "/" + gen);
                System.out.println("当前解：" + path.getValue());
            }
            T = alpha * T;
        }

        System.out.println("——————————————————————————————");
        System.out.println("最终数据如下：");
        path.print();
        System.out.println("总点数：" + path.getCityList().size());
        System.out.print("最优解为：");
        for (City city : path.getCityList()) {
        }
        for (int i = 0; i < path.getCityList().size(); i++) {
            System.out.print(path.getCityList().get(i).id);
            if (i != path.getCityList().size() - 1) {
                System.out.print(" -> ");
            } else {
                System.out.println();
            }
        }
        System.out.print("最优值为：" + path.getValue());
    }
}

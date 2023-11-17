package src;

import java.io.FileWriter;
import java.io.IOException;

public class ParticleGenerator {

    public static void main(String[] args) throws IOException {

        if(args.length != 3){
            throw new IllegalArgumentException("Invalid parameters");
        }

        int N;
        double v, L, radius;
        N = Integer.parseInt(args[0]);
        L = Double.parseDouble(args[1]);
        v = Double.parseDouble(args[2]);
        radius = 0;


        FileWriter staticWriter = new FileWriter("./input/static.txt");
        FileWriter dynamicWriter = new FileWriter("./input/dynamic.txt");

        staticWriter.write("   " + N + "\n   " + L);
        dynamicWriter.write("   " + 0);

        double x, y, theta;
        for (int i = 0; i < N; i++) {
            x = L * Math.random();
            y = L * Math.random();
            theta = 2 * Math.PI * Math.random();
            staticWriter.write("\n   " + radius);
            dynamicWriter.write(String.format("\n   %.7e   %.7e   %f   %.7e", x,y,v,theta));
        }
        staticWriter.close();
        dynamicWriter.close();
    }
}
package src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputWriter {

    private static FileWriter positionsFileWriter;
    private static FileWriter vaFileWriter;

    public static void createFiles(String positionsFile, String vaFile) throws IOException {
        positionsFileWriter = new FileWriter(positionsFile);
        vaFileWriter = new FileWriter(vaFile);
        vaFileWriter.write("Step\tVa\n");
    }

    public static void newStep(List<Particle> particles, int step, int N) throws IOException {
        positionsFileWriter.write(String.format("%d\n", step));

        double sinSum= 0,cosSum=0;
        for(Particle particle: particles){
            positionsFileWriter.write(String.format("%d\t%.7e\t%.7e\t%f\t%.7e\n", particle.getId(),particle.getX(), particle.getY(), particle.getV(), particle.getTheta()));
            sinSum+=(Math.sin(particle.getTheta()));
            cosSum+=(Math.cos(particle.getTheta()));
        }

        double va = Math.sqrt(Math.pow(sinSum,2) + Math.pow(cosSum,2)) / N;
        vaFileWriter.write(String.format("%d\t%g\t\n",step,va));
    }

    public static void closeFiles() throws IOException {
        positionsFileWriter.close();
        vaFileWriter.close();
    }
}

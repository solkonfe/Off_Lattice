package src;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

public class OffLatticeMethod {

    // scanner static data
    public static void getStaticData(Scanner reader, List<Particle> particles){
        double radius;
        int counter = 1;
        double rMax = 0;

        while (reader.hasNextLine()) {
            String data = reader.nextLine().trim().replaceAll("\\s+", " ");
            StringTokenizer tokenizer = new StringTokenizer(data);

            radius = Double.parseDouble(tokenizer.nextToken());
            particles.add(new Particle(counter));
            if(radius > rMax){
                rMax = radius;
            }
            counter++;
        }
    }

    // scanner dynamic data
    public static void getDynamicData(Scanner reader, List<Particle> particles){
        List<Double> props = new ArrayList<>();
        int counter = 1;
        reader.nextLine(); //skip the t0

        while (reader.hasNextLine()) {
            String data = reader.nextLine().trim().replaceAll("\\s+", " ");
            StringTokenizer tokenizer = new StringTokenizer(data);
            while(tokenizer.hasMoreElements()){
                props.add(Double.parseDouble(tokenizer.nextToken()));
            }
            if(props.size() == 4){
                particles.get(counter - 1).setX(props.get(0));
                particles.get(counter - 1).setY(props.get(1));
                particles.get(counter - 1).setV(props.get(2));
                particles.get(counter - 1).setTheta(props.get(3));
                props.clear();
                counter++;
            }
            else{
                System.out.printf("Invalid dynamic properties for particle %d\n", counter);
                exit(-1);
            }
        }


    }

    public static void nextStep(List<Particle> particles, HashMap<Integer, List<Integer>> neighboursMap, double L, double n){
        for (Particle p: particles){
            // update position
            double newX = p.getX() + p.getV() * Math.cos(p.getTheta());
            double newY = p.getY() + p.getV() * Math.sin(p.getTheta());

            // check if particle should move to other side of board (periodic contour condition)
            if (newX > L)
                newX = newX - L;
            else if (newX < 0)
                newX = newX + L;
            if (newY > L)
                newY = newY - L;
            else if (newY < 0)
                newY = newY + L;

            p.setX(newX);
            p.setY(newY);

            // update theta angle
            double noise = ((Math.random() * 2*n) - n)/2;
            double sumSin = Math.sin(p.getTheta());
            double sumCos = Math.cos(p.getTheta());
            for (Integer id : neighboursMap.get(p.getId())){
                sumSin += Math.sin(particles.get(id - 1).getTheta());
                sumCos += Math.cos(particles.get(id - 1).getTheta());
            }
            double newTheta = Math.atan2(sumSin,sumCos);

            p.setTheta((newTheta + noise) % (2*Math.PI));
        }
    }

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        if(args.length != 5){
            throw new IllegalArgumentException("Invalid parameters");
        }

        File staticFile = new File("./input/static.txt");
        Scanner myStaticReader = new Scanner(staticFile);
        File dynamicFile = new File("./input/dynamic.txt");
        Scanner myDynamicReader = new Scanner(dynamicFile);

        int N = 0;
        double L = 0;
        double rc = Double.parseDouble(args[0]);
        double n = Double.parseDouble(args[1]);
        int steps = Integer.parseInt(args[2]);
        String positionsFile = "./out/" + args[3] + ".txt";
        String vaFile = "./out/" + args[4] + ".txt";

        //parse the quantity of particles(N) and the size of the square grid(L)
        if(myStaticReader.hasNextLine()){
            N = Integer.parseInt(myStaticReader.nextLine().trim().replaceAll("\\s+", ""));
        }
        if(myStaticReader.hasNextLine()){
            L = Double.parseDouble(myStaticReader.nextLine().trim().replaceAll("\\s+", ""));
        }

        // create particles
        List<Particle> particles = new ArrayList<>();
        getStaticData(myStaticReader, particles);
        getDynamicData(myDynamicReader, particles);

        // open output files
        OutputWriter.createFiles(positionsFile,vaFile);

        Grid grid = new Grid(L, rc);
        for (int i = 0; i <= steps; i++) {
            // write each particle's data in output file at step i
            OutputWriter.newStep(new ArrayList<>(particles), i, particles.size());

            // fill the grid with the new particles and find their neighbours
            grid.fill(particles);
            HashMap<Integer, List<Integer>> neighbours = grid.getNeighbours();

            // update new theta values
            nextStep(particles, neighbours, L, n);
        }

        // close output files
        OutputWriter.closeFiles();

        long endTime = System.currentTimeMillis();
        System.out.printf("Time: %d ms\n", endTime - startTime);

    }
}

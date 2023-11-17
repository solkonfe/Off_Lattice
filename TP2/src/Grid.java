package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grid{

    private final Cell[][] matrix;
    private final double L;
    private int M;
    private final double rc;

    public Grid(double L, double rc) {
        this.L = L;
        this.M = (int) Math.floor(L/rc);
        if(this.M == L/rc){
            this.M -=1;
        }
        if (this.M == 0) {
            this.M = 1;
        }
        this.rc = rc;
        this.matrix = new Cell[M][M];
    }

    // fill the grid with generated particles
    public void fill(List<Particle> particles) {
        int rowIndex;
        int colIndex;

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                matrix[i][j] = new Cell();
            }
        }

        for (Particle particle : particles) {
            rowIndex = ((int) (particle.getX() / ((float) L / M))) % M;
            colIndex = ((int) (particle.getY() / ((float) L / M))) % M;
            matrix[rowIndex][colIndex].addParticle(particle);
        }
    }

    // distance between particles
    public double getDistance(Particle p1, Particle p2){
        double deltaX = Math.abs(p1.getX() - p2.getX());
        double deltaY = Math.abs(p1.getY() - p2.getY());
        deltaX -= deltaX > ((L*1.0f) / 2 ) ? L : 0;
        deltaY -= deltaY > ((L*1.0f) / 2) ? L : 0;

        return Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));
    }

    // match neighbours in the grid
    public HashMap<Integer, List<Integer>> getNeighbours(){
        HashMap<Integer,List<Integer>> neighbours = new HashMap<>();

        for (int i = 0; i < M; i++){
            for (int j = 0; j < M; j++) {
                for (Particle particle : matrix[i][j].getParticles()) {

                    if(!neighbours.containsKey(particle.getId())) {
                        neighbours.put(particle.getId(), new ArrayList<>());
                    }

                    checkSelf(neighbours.get(particle.getId()), particle, matrix[i][j].getParticles());
                    possibleNeighbours(particle,neighbours, matrix[(i+ M -1)% M][(j+1)% M].getParticles());
                    possibleNeighbours(particle,neighbours, matrix[i][(j+1)% M].getParticles());
                    possibleNeighbours(particle,neighbours, matrix[(i+1)% M][(j+1)% M].getParticles());
                    possibleNeighbours(particle,neighbours, matrix[(i+1)% M][j].getParticles());
                }
            }
        }
        return neighbours;
    }

    // check neighbours in particle's own cell
    private void checkSelf(List<Integer> neighbours, Particle particle, List<Particle> neighbourCell){
        for(Particle neighbour : neighbourCell){
            if(neighbour.getId() != particle.getId()){
                if(getDistance(particle, neighbour) <= rc){
                    neighbours.add(neighbour.getId());
                }
            }
        }
    }

    // check neighbours from another cell
    private void possibleNeighbours(Particle particle, HashMap<Integer, List<Integer>> neighbours, List<Particle> neighbourCell){
        double distance;
        for (Particle neighbour : neighbourCell) {
            distance = getDistance(particle, neighbour);
            if(distance <= rc){
                neighbours.get(particle.getId()).add(neighbour.getId());
                if(!neighbours.containsKey(neighbour.getId())){
                    neighbours.put(neighbour.getId(), new ArrayList<>());
                }
                neighbours.get(neighbour.getId()).add(particle.getId());
            }
        }
    }
}

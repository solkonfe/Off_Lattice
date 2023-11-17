package src;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private List<Particle> particles;

    public Cell() {
        this.particles = new ArrayList<>();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void addParticle(Particle p){
        particles.add(p);
    }
}

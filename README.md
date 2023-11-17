# Off_Lattice
All files must be executed from the /Birds directory for proper operation.

ParticleGenerator parameters (in order):

N: number of particles
L: grid length
v: speed module of the particles

The execution of this method produces the static.txt and dynamic.txt files with the necessary values for creating the grid.

OffLatticeMethod parameters (in order):

rc: interaction radius of the particles
n: noise amplitude
steps: number of temporal simulation steps
output file path for positions
output file path for orders

The execution of this method produces the positions.txt file containing the properties of the particles at each temporal step and va.txt containing the order values at each temporal step.

Video of animation:
https://youtu.be/v5MkIHmjmAA?si=8e-Me6R1nWiLmMfU

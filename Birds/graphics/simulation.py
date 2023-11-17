import sys

import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline

def export_to_ovito(positions_file, static_file):
    data_frame = get_particle_data(positions_file)
    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    with open(static_file, "r") as static:
        next(static)
        L = static.readline()

    def simulation_cell(frame, data):
        cell = SimulationCell()
        cell.is2D = True
        cell[:, 0] = (L, 0, 0)
        cell[:, 1] = (0, L, 0)
        data.objects.append(cell)
        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    pipeline.modifiers.append(simulation_cell)

    export_file(pipeline, 'simulation.dump', 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Velocity.X", "Velocity.Y"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(frame_file):
    frames = []
    with open(frame_file, "r") as frame:
        next(frame)
        frame_lines = []
        for line in frame:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                frame_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "speed", "angle"])
                frames.append(df)
                frame_lines = []
        df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "speed", "angle"])
        frames.append(df)
    print(frames)

    return frames


def get_particles(data_frame):
    particles = Particles()
    particles.create_property('Particle Identifier', data=data_frame.id)
    particles.create_property('Position', data=np.array((data_frame.x, data_frame.y, np.zeros([len(data_frame.x),]))).T)
    particles.create_property('Angle', data=data_frame.angle)
    particles.create_property('Velocity', data=np.array((np.cos(data_frame.angle) * data_frame.speed, np.sin(data_frame.angle) * data_frame.speed, np.zeros([len(data_frame.x,)]))).T)

    return particles


if __name__ == '__main__':
    export_to_ovito(sys.argv[1], sys.argv[2])

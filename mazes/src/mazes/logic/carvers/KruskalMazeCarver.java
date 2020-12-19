package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Hint: you'll probably need to include something like the following:
        Collection<EdgeWithData<Room, Wall>> allEdges = new ArrayList<>();
        for (Wall eachWall : walls) {
            EdgeWithData<Room, Wall> temp =
                new EdgeWithData<>(eachWall.getRoom1(), eachWall.getRoom2(), rand.nextDouble(), eachWall);
            allEdges.add(temp);
        }
        Collection<EdgeWithData<Room, Wall>> mazeWalls =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(allEdges)).edges();
        Set<Wall> set = new HashSet<>();
        for (EdgeWithData<Room, Wall> singleWall : mazeWalls) {
            set.add(singleWall.data());
        }
        return set;
    }
}

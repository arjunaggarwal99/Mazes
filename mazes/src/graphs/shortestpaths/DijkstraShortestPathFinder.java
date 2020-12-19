package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<V, E>();
        if (Objects.equals(start, end)) {
            return edgeTo;
        }
        Map<V, Double> distTo = new HashMap<V, Double>();
        DoubleMapMinPQ<V> perimeter = new DoubleMapMinPQ<>();
        distTo.put(start, 0.0);
        perimeter.add(start, 0);
        V curr;
        Collection<E> neighbors;
        double oldDist = -1.0;
        double newDist = -1.0;
        while (!perimeter.isEmpty()) {
            curr = perimeter.removeMin();
            if (Objects.equals(curr, end)) {
                break;
            }
            neighbors = graph.outgoingEdgesFrom(curr);
            for (E neighbor : neighbors) {
                oldDist = Double.POSITIVE_INFINITY;
                if (distTo.containsKey(neighbor.to())) {
                    oldDist = distTo.get(neighbor.to()); // previous best path to v
                }
                newDist = distTo.get(curr) + neighbor.weight(); // what if we went through u?
                if (newDist < oldDist) {
                    distTo.put(neighbor.to(), newDist);
                    edgeTo.put(neighbor.to(), neighbor);
                    if (perimeter.contains(neighbor.to())) {
                        perimeter.changePriority(neighbor.to(), newDist);
                    } else {
                        perimeter.add(neighbor.to(), newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        List<E> edges = new ArrayList<E>();
        E edge = spt.get(end);
        if (edge == null) {
            return new ShortestPath.Failure<>();
        }
        // System.out.println(spt.get(start).from());
        while (edge.from() != start) {
            //  E currEdge = spt.get(edge.from());
            edges.add(edge);
            edge = spt.get(edge.from());
            if (edge == null) {
                Collections.reverse(edges);
                return new ShortestPath.Success<>(edges);
            }
        }
        if (edge.from() == null && start != null) {
            return new ShortestPath.Failure<>();
        }
        edges.add(edge);
        Collections.reverse(edges);
        if (edges == null && start != null) {
            return new ShortestPath.Failure<>();
        }
        return new ShortestPath.Success<>(edges);
    }
}

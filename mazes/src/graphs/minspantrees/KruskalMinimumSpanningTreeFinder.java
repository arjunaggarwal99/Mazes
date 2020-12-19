package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // Here's some code to get you started; feel free to change or rearrange it if you'd like.

        // sort edges in the graph in ascending weight order
        if (graph.allEdges().size() == 0 && graph.allVertices().size() == 0) {
            return new MinimumSpanningTree.Success<>();
        }
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        List<V> vertices = new ArrayList<>(graph.allVertices());
        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> finalEdges = new ArrayList<E>();
        for (int i = 0; i < vertices.size(); i++) {
            disjointSets.makeSet(vertices.get(i));
        }
        int count = 0;
        for (E edge : edges) {
            if (disjointSets.union(edge.to(), edge.from())) {
                count++;
                finalEdges.add(edge);
            }
        }
        if (count != vertices.size() - 1) {
            return new MinimumSpanningTree.Failure<>();
        }
        return new MinimumSpanningTree.Success<>(finalEdges);
    }
}

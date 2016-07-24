package aima.extra.instrument.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import aima.extra.instrument.api.*;
import aima.extra.search.api.*;

/**
 * A version of GraphSearch that registers and notifies listeners
 *
 * @author Ciaran O'Reilly
 * @author Benjamin Kusin
 */
public class GraphSearchInstrumented<A, S> implements SearchForActionsFunction<A, S>, SearchForActionsEventProvider<A,S> {
	
	private Vector<SearchForActionsListener<A,S>> listeners;

	// function GRAPH-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
		SearchForActionsEventProvider.notifyNodeAddedToFrontier(new SearchForActionsEvent<A,S>(frontier.peek(), SearchForActionsEvent.NODE_ADDED_TO_FRONTIER));
		// initialize the explored set to be empty
		Set<S> explored = newExploredSet();
		// loop do
		while (true) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) {
				SearchForActionsEventProvider.notifyFailed(new SearchForActionsEvent<A,S>(null, SearchForActionsEvent.FAILED));
				return failure();
			}
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			SearchForActionsEventProvider.notifyNodeRemovedFromFrontier(new SearchForActionsEvent<A,S>(node, SearchForActionsEvent.NODE_REMOVED_FROM_FRONTIER));
			// if the node contains a goal state then return the corresponding
			// solution
			if (problem.isGoalState(node.state())) {
				return solution(node);
			}
			// add the node to the explored set
			explored.add(node.state());
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (A action : problem.actions(node.state())) {
				Node<A, S> child = newChildNode(problem, node, action);
				// only if not in the frontier or explored set
				if (!(containsState(frontier, child) || explored.contains(child.state()))) {
					frontier.add(child);
					SearchForActionsEventProvider.notifyNodeAddedToFrontier(new SearchForActionsEvent<A,S>(child, SearchForActionsEvent.NODE_ADDED_TO_FRONTIER));
				}
			}
		}
	}

	//
	// Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();

	public GraphSearchInstrumented() {
	}
	
	public GraphSearchInstrumented(Collection<SearchForActionsListener<A,S>> listeners) {
		this.listeners=(Vector<SearchForActionsListener<A, S>>) listeners;
	}

	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return nodeFactory.newChildNode(problem, node, action);
	}

	public Queue<Node<A, S>> newFrontier(S initialState) {
		Queue<Node<A, S>> frontier = new LinkedList<>();
		frontier.add(nodeFactory.newRootNode(initialState));
		return frontier;
	}

	public Set<S> newExploredSet() {
		return new HashSet<>();
	}

	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}

	public boolean containsState(Queue<Node<A, S>> frontier, Node<A, S> child) {
		// NOTE: Not very efficient (i.e. linear in the size of the frontier)
		return frontier.stream().anyMatch(frontierNode -> frontierNode.state().equals(child.state()));
	}

	@Override
	public Collection<SearchForActionsListener<A, S>> searchForActionsListeners() {
		return listeners;
	}
}

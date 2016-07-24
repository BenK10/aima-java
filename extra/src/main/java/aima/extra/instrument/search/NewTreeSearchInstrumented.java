
package aima.extra.instrument.search;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import aima.extra.instrument.api.*;
import aima.extra.search.api.SearchForActionsEvent;
import aima.extra.search.api.SearchForActionsEventProvider;
import aima.extra.search.api.SearchForActionsListener;

/**
 * A version of TreeSearch that registers and notifies listeners
 *
 * @author Ciaran O'Reilly
 * @author Benjamin Kusin
 */

public class NewTreeSearchInstrumented<A, S> implements SearchForActionsFunction<A, S>, SearchForActionsEventProvider<A,S> {
	
	private Vector<SearchForActionsListener<A,S>> listeners;
	
	// function TREE-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of the problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
		SearchForActionsEventProvider.notifyNodeAddedToFrontier(new SearchForActionsEvent<A,S>(frontier.peek(), SearchForActionsEvent.NODE_ADDED_TO_FRONTIER));
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
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (A action : problem.actions(node.state())) {
				Node<A,S> childNode = newChildNode(problem, node, action);
				frontier.add(childNode);
				SearchForActionsEventProvider.notifyNodeAddedToFrontier(new SearchForActionsEvent<A,S>(childNode, SearchForActionsEvent.NODE_ADDED_TO_FRONTIER));
			}
		}
	}

	//
	// Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();
	
	public NewTreeSearchInstrumented() {
	}

	public NewTreeSearchInstrumented(Collection<SearchForActionsListener<A,S>> listeners) {
		this.listeners=(Vector<SearchForActionsListener<A, S>>) listeners;
	}

	public Queue<Node<A, S>> newFrontier(S initialState) {
		Queue<Node<A, S>> frontier = new LinkedList<>();
		frontier.add(nodeFactory.newRootNode(initialState));
		return frontier;
	}

	public List<A> failure() {
		return searchController.failure();
	}

	public List<A> solution(Node<A, S> node) {
		return searchController.solution(node);
	}

	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
		return nodeFactory.newChildNode(problem, node, action);
	}

	@Override
	public Collection<SearchForActionsListener<A, S>> searchForActionsListeners() {
		return listeners;
	}
}
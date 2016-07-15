
package aima.extra.instrument.search;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import aima.extra.instrument.api.*;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 *
 * Figure ?? An informal description of the general tree-search algorithm.
 *
 * @author Ciaran O'Reilly
 */
public class NewTreeSearchInstrumented<A, S> implements SearchForActionsFunction<A, S>, Provider {
	
	private Vector<Listener> listeners;
	
	// function TREE-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of the problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
		Provider.notify(SearchEvent.NODE_ADDED_TO_FRONTIER, frontier.peek());
		// loop do
		while (true) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) {
				Provider.notify(SearchEvent.FAILURE, null);
				return failure();
			}
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			Provider.notify(SearchEvent.NODE_REMOVED_FROM_FRONTIER, node);
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
				Provider.notify(SearchEvent.NODE_ADDED_TO_FRONTIER, childNode);
			}
		}
	}

	//
	// Supporting Code
	protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	protected SearchController<A, S> searchController = new BasicSearchController<A, S>();
	
	public NewTreeSearchInstrumented() {
	}

	public NewTreeSearchInstrumented(Collection<Listener> listeners) {
		for(Listener L : listeners)
			Provider.registerListener(this.listeners, L);
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
}
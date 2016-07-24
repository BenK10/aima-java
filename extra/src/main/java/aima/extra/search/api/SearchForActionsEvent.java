
package aima.extra.search.api;

import java.util.EventObject;

import aima.core.search.api.Node;

/**
 * class representing seach events
 * @author Benjamin Kusin
 */

public class SearchForActionsEvent<A,S> extends EventObject {
	
	public static final String FOUND_GOAL = "found_goal";
	public static final String FAILED = "failed";
	public static final String NODE_EXPANDED = "node_expanded";
	public static final String NODE_ADDED_TO_FRONTIER = "node_added_to_frontier";
	public static final String NODE_REMOVED_FROM_FRONTIER = "node_removed_from_frontier";
	
	private String eventType;
	
	public SearchForActionsEvent()
	{
		super(null);
		eventType="";
	}
	
	public SearchForActionsEvent(Node<A,S> sourceNode, String event)
	{
		super(sourceNode);
		eventType = event;
	}
	
	public Node<A,S> getSourceNode()
	{
		return (Node<A, S>) super.getSource();
	}
	
	public final String eventType()
	{
		return eventType;
	}
	
}

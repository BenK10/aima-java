
package aima.extra.search.api;

import java.util.EventObject;

import aima.core.search.api.Node;

/**
 * class representing seach events
 * @author Benjamin Kusin
 */

public class SearchForActionsEvent<A,S> extends EventObject {
	

    public static final String CMD_START = "start";
    public static final String CMD_INITIALIZE = "initialize";
    public static final String CMD_FRONTIER_EMPTY_CHECK = "frontier-empty-check";
    public static final String CMD_SOLUTION = "solution";
	public static final String CMD_CHECK_GOAL = "check-goal";
	public static final String CMD_FAILURE = "failure";
	public static final String CMD_EXPAND_NODE = "expand-node";
	public static final String CMD_FRONTIER_ADD = "frontier-add";
	public static final String CMD_FRONTIER_REMOVE = "frontier-remove";
	
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

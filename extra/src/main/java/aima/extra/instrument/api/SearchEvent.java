
package aima.extra.instrument.api;

public static class SearchEvent {
	
	public static final String FOUND_GOAL = "found_goal";
	public static final String FAILED = "failed";
	public static final String NODE_EXPANDED = "node_expanded";
	public static final String NODE_ADDED_TO_FRONTIER = "node_added_to_frontier";
	public static final String NODE_REMOVED_FROM_FRONTIER = "node_removed_from_frontier";
	
	private String eventType;
	
	public final String eventType()
	{
		return eventType;
	}
	
	public SearchEvent(String event)
	{
		eventType = event;
	}
}

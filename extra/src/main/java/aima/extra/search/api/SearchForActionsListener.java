package aima.extra.search.api;

import java.util.EventListener;


/**
 * Listens for algorithm events of interest
 * 
 * @author Benjamin Kusin
 **
 */

public interface SearchForActionsListener<A,S> extends EventListener {
	
	default void foundGoal(SearchForActionsEvent<A,S> event) {}
	default void failed(SearchForActionsEvent<A,S> event) {}
	default void nodeExpanded(SearchForActionsEvent<A,S> event) {}
	default void nodeAddedToFrontier(SearchForActionsEvent<A,S> event) {}
	default void nodeRemovedFromFrontier(SearchForActionsEvent<A,S> event) {}
}

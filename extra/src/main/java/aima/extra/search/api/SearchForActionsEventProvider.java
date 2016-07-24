package aima.extra.search.api;

import java.util.Collection;

import aima.extra.search.api.SearchForActionsEvent;


/**
 * interface for working with SearchForActionsListener<A,S> interfaces
 * 
 * @author Benjamin Kusin
 *
 */

public interface SearchForActionsEventProvider <A,S> {

	default void addSearchForActionsListener(SearchForActionsListener<A,S> listener)
	{
		searchForActionsListeners().add(listener);
	}
	
	default void removeListener(Collection<SearchForActionsListener<A,S>> listeners, SearchForActionsListener<A,S> listener)
	{
		searchForActionsListeners().remove(listener);
	}
	
	default void notifyFoundGoal(SearchForActionsEvent<A,S> event)
	{
		searchForActionsListeners().forEach(l -> l.foundGoal(event));
	}
	
	default void notifyFailed(SearchForActionsEvent<A,S> event)
	{
		searchForActionsListeners().forEach(l -> l.failed(event));
	}
	
	
	default void notifyNodeExpanded(SearchForActionsEvent<A,S> event)
	{
		searchForActionsListeners().forEach(l -> l.nodeExpanded(event));
	}
	
	default void notifyNodeAddedToFrontier(SearchForActionsEvent<A,S> event)
	{
		searchForActionsListeners().forEach(l -> l.nodeAddedToFrontier(event));
	}
	
	default void notifyNodeRemovedFromFrontier(SearchForActionsEvent<A,S> event)
	{
		searchForActionsListeners().forEach(l -> l.nodeRemovedFromFrontier(event));
	}
	
	Collection<SearchForActionsListener<A,S>> searchForActionsListeners();
}

package aima.extra.search.api;

import java.util.Collection;

import aima.core.search.api.Node;
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
	
	default void notifyFoundGoal(Node<A,S> node)
	{
		if(searchForActionsListeners().size() > 0) {
	        SearchForActionsEvent<A,S> event = new SearchForActionsEvent<>(node, SearchForActionsEvent.CMD_FRONTIER_ADD);
		searchForActionsListeners().forEach(l -> l.foundGoal(event));
		}
	}
	
	default void notifyFailed(Node<A,S> node)
	{
		if(searchForActionsListeners().size() > 0) {
	        SearchForActionsEvent<A,S> event = new SearchForActionsEvent<>(node, SearchForActionsEvent.CMD_FAILURE);
		searchForActionsListeners().forEach(l -> l.failed(event));
		}
	}
	
	
	default void notifyNodeExpanded(Node<A,S> node)
	{
		if(searchForActionsListeners().size() > 0) {
	        SearchForActionsEvent<A,S> event = new SearchForActionsEvent<>(node, SearchForActionsEvent.CMD_EXPAND_NODE);
		searchForActionsListeners().forEach(l -> l.failed(event));
		}
	}
	
	default void notifyNodeAddedToFrontier(Node<A,S> node)
	{
		if(searchForActionsListeners().size() > 0) {
	        SearchForActionsEvent<A,S> event = new SearchForActionsEvent<>(node, SearchForActionsEvent.CMD_FRONTIER_ADD);
		searchForActionsListeners().forEach(l -> l.failed(event));
		}
	}
	
	default void notifyNodeRemovedFromFrontier(Node<A,S> node)
	{
		if(searchForActionsListeners().size() > 0) {
	        SearchForActionsEvent<A,S> event = new SearchForActionsEvent<>(node, SearchForActionsEvent.CMD_FRONTIER_REMOVE);
		searchForActionsListeners().forEach(l -> l.failed(event));
		}
	}
	
	Collection<SearchForActionsListener<A,S>> searchForActionsListeners();
}

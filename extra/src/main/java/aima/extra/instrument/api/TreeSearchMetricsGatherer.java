package aima.extra.instrument.api;


import aima.core.search.api.Node;
import aima.core.search.basic.support.BasicNode;

import aima.extra.search.api.SearchForActionsEvent;
import aima.extra.search.api.SearchForActionsListener;

import java.util.*;

/**
 * Gathers metrics for tree search 
 * @author Benjamin Kusin
 *
 */

public class TreeSearchMetricsGatherer<A,S> implements SearchForActionsListener<A,S> {
	
	 private boolean            searchFailed = false;
	 private int                maxFrontierSize = 0;
	 private int                numberAddedToFrontier = 0;
	 private int                numberRemovedFromFrontier = 0;
	 private Map<S, Integer>    statesVisitedCounts = new HashMap<>();
	 private Map<S, Node<A, S>> statesInFrontierNotVisited = new HashMap<>();
	 private Node<A, S>         lastNodeVisited;
	 private List<Integer>      searchSpaceLevelCounts = new ArrayList<>();
	 private List<Integer>      searchSpaceLevelRemainingCounts = new ArrayList<>();
	 
	 public boolean			   searchFailed() {return searchFailed;}
	 public int                currentFrontierSize() {return numberAddedToFrontier - numberRemovedFromFrontier;}
	 public int                maxFrontierSize() {return maxFrontierSize;}
	 public int                numberAddedToFrontier() {return numberAddedToFrontier;}
	 public int                numberRemovedFromFrontier() {return numberRemovedFromFrontier;}
	 public Map<S, Integer>    statesVisitedCounts() {return statesVisitedCounts;}
	 public Map<S, Node<A, S>> statesInFrontierNotVisited() {return statesInFrontierNotVisited;}
	 public Node<A, S>         lastNodeVisited() {return lastNodeVisited;}
	 public List<Integer>      searchSpaceLevelCounts() {return searchSpaceLevelCounts;}
	 public List<Integer>      searchSpaceLevelRemainingCounts() {return searchSpaceLevelRemainingCounts;}
	 
	 private final String REMOVE_UPDATE = "remove";
	 private final String ADD_UPDATE = "add";
     
     private void update_searchSpaceLevelAndRemainingCounts(final String updateType, Node<A,S> node)
     {
    	 int level = BasicNode.depth(node);
    	 
    	 if(updateType==REMOVE_UPDATE)
    	 {
           searchSpaceLevelRemainingCounts = new ArrayList<>(searchSpaceLevelRemainingCounts);
           searchSpaceLevelRemainingCounts.set(level, searchSpaceLevelRemainingCounts.get(level) - 1);
    	 }
    	 
    	 if(updateType==ADD_UPDATE)
    	 {
    		 searchSpaceLevelCounts = new ArrayList<>(searchSpaceLevelCounts);
             searchSpaceLevelRemainingCounts = new ArrayList<>(searchSpaceLevelRemainingCounts);
             if (level >= searchSpaceLevelCounts.size()) {
                 searchSpaceLevelCounts.add(1);
                 searchSpaceLevelRemainingCounts.add(1);
             } else {
                 searchSpaceLevelCounts.set(level, searchSpaceLevelCounts.get(level) + 1);
                 searchSpaceLevelRemainingCounts.set(level, searchSpaceLevelRemainingCounts.get(level) + 1);
             }
    	 }
     }
     
     private void update_statesVisitedCounts(Node<A,S> removed)
     {
    	 statesVisitedCounts = new HashMap<>(statesVisitedCounts);
         Integer visitedCount = statesVisitedCounts.get(removed.state());
         if (visitedCount == null)  
             statesVisitedCounts.put(removed.state(), 1);
         else 
             statesVisitedCounts.put(removed.state(), visitedCount+1); 
     }
     
     private void update_statesInFrontierNotVisited(final String updateType, Node<A,S> node)
     {
    	 if(updateType==REMOVE_UPDATE)
    	 {
    		 if (statesInFrontierNotVisited.containsKey(node.state())) {
    			 statesInFrontierNotVisited = new HashMap<>(statesInFrontierNotVisited);
    			 statesInFrontierNotVisited.remove(node.state());
    		 }
    	 }
    	 
    	 if(updateType==ADD_UPDATE)
    	 {
    		 if (!statesVisitedCounts.containsKey(node.state())) {
                 if (!statesInFrontierNotVisited.containsKey(node.state())) {
                     statesInFrontierNotVisited = new HashMap<>(statesInFrontierNotVisited);
                     statesInFrontierNotVisited.put(node.state(), node);
                 }
             }
    	 }
     }
     
    @Override
	public void nodeAddedToFrontier(SearchForActionsEvent<A,S> event)  
	{
		  numberAddedToFrontier++;
		  update_searchSpaceLevelAndRemainingCounts(event.eventType(), event.getSourceNode());
          update_statesInFrontierNotVisited(event.eventType(), event.getSourceNode()); 
	}
		
    @Override
    public void nodeRemovedFromFrontier(SearchForActionsEvent<A,S> event)
    {
    	 lastNodeVisited = event.getSourceNode();
		 numberRemovedFromFrontier++;
		  
		 update_searchSpaceLevelAndRemainingCounts(event.eventType(), event.getSourceNode());
		 update_statesVisitedCounts(event.getSourceNode());
         update_statesInFrontierNotVisited(event.eventType(), event.getSourceNode()); 
    }

}

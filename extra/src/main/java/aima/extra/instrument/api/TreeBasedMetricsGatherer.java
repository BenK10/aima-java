package aima.extra.instrument.api;


import aima.core.search.api.Node;
import aima.core.search.basic.TreeSearch;
import aima.core.search.basic.support.BasicNode;
import aima.core.search.basic.support.BasicNodeFactory;

import aima.extra.instrument.api.Listener;
import aima.extra.instrument.api.SearchEvent;

import java.util.*;

public class TreeBasedMetricsGatherer implements Listener {
	
	 private int maxFrontierSize = 0;
	 private int numberAddedToFrontier = 0;
	 private int numberRemovedFromFrontier = 0;
	 private Map<S, Integer> statesVisitedCounts = new HashMap<>();
	 private Map<S, Node<A, S>> statesInFrontierNotVisited = new HashMap<>();
	 private Node<A, S> lastNodeVisited;
	 private List<Integer> searchSpaceLevelCounts = new ArrayList<>();
	 private List<Integer> searchSpaceLevelRemainingCounts = new ArrayList<>();
	
	 public int                currentFrontierSize();
	 public int                maxFrontierSize();
	 public int                numberAddedToFrontier();
	 public int                numberRemovedFromFrontier();
	 public Node<A, S>         node();
	 public Map<S, Integer>    statesVisitedCounts();
	 public Map<S, Node<A, S>> statesInFrontierNotVisited();
	 public Node<A, S>         lastNodeVisited();
	 public List<Integer>      searchSpaceLevelCounts();
	 public List<Integer>      searchSpaceLevelRemainingCounts();
	 
	 private final String REMOVE_UPDATE = "remove";
	 private final String ADD_UPDATE = "add";
     
     private void update_searchSpaceLevelAndRemainingCounts(final String updateType, Node<A,S> node)
     {
    	 int level = BasicNode.depth(removed);
    	 
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
     
     private void update_statesVisitedCounts( Node<A,S> removed)
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
	public void processEvent(SearchEvent event, Node<A,S> node)
	{
		switch (event)
		{
		  case FOUND_GOAL:
			  break;
			  
		  case FAILED:
			  break;
			  
		  case NODE_EXPANDED:
			  break;
			  
		  case NODE_ADDED_TO_FRONTIER:
			  numberAddedToFrontier++;
			  
			  update_searchSpaceLevelAndRemainingCounts();
	          update_statesInFrontierNotVisited(); 
			  break;
			  
		  case NODE_REMOVED_FROM_FRONTIER:
			  lastNodeVisited = node;
			  numberRemovedFromFrontier++;
			  
			  update_searchSpaceLevelAndRemainingCounts();
			  update_statesVisitedCounts();
	          update_statesInFrontierNotVisited(); 
			  break;
			  
		default:
			break; 
		}
	}

}

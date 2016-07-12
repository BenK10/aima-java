package aima.core.search.api;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.basic.TreeSearch;
import aima.core.search.basic.support.BasicNode;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.api.Listener;

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
	
	 int                currentFrontierSize();
     int                maxFrontierSize();
     int                numberAddedToFrontier();
     int                numberRemovedFromFrontier();
     Node<A, S>         node();
     Map<S, Integer>    statesVisitedCounts();
     Map<S, Node<A, S>> statesInFrontierNotVisited();
     Node<A, S>         lastNodeVisited();
     List<Integer>      searchSpaceLevelCounts();
     List<Integer>      searchSpaceLevelRemainingCounts();
	
	@Override
	public void processEvent(String event)
	{
		
	}

}

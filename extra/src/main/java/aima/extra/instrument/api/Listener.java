package aima.extra.instrument.api;

import aima.core.search.api.Node;


/**
 * Listens for algorithm events of interest
 * 
 * @author Benjamin Kusin
 *
 */

public interface Listener {
	
	public void processEvent(String event, Node node);

}

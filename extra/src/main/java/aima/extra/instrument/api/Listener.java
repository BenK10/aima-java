package aima.extra.instrument.api;

/**
 * Listens for algorithm events of interest
 * 
 * @author Benjamin Kusin
 *
 */

public interface Listener {
	
	public void processEvent(String event);

}

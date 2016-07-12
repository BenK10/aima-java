package aima.extra.instrument.api;

import aima.extra.instrument.api.Listener;


/**
 * interface for working with Listener interfaces
 * 
 * @author Benjamin Kusin
 *
 */

public interface Provider {

	default public void registerListener(Collection<Listener> listeners, Listener listener)
	{
		listeners.add(listener);
	}
	
	default public void removeListenerCollection<Listener> listeners, Listener listener)
	{
		listeners.remove(listener);
	}
	
	default public void notify(Listener listener, String event)
	{
		listener.processEvent(event);
	}
}

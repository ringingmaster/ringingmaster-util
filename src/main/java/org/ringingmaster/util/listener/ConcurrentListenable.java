package org.ringingmaster.util.listener;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkState;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public class ConcurrentListenable<T> implements Listenable<T> {

	private final Set<T> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

	@Override
	public void addListener(T listener) {
		final boolean added = listeners.add(listener);
		checkState(added);
	}

	public Set<T> getListeners() {
		return listeners;
	}

}

package com.concurrentperformance.util.listener;

/**
 * TODO comments ???
 *
 * @author Lake
 */
public interface Listenable<T> {
	void addListener(T listener);
}

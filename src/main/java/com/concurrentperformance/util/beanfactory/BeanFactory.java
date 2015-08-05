package com.concurrentperformance.util.beanfactory;

/**
 * Generic factory - to be implemented by whatever DI framework is being used.
 *
 * @author Lake
 */
public interface BeanFactory {

	/**
	 * Build a configured bean from whatever DI implementation is being used.
	 */
	<T> T build(Class<T> type);
}

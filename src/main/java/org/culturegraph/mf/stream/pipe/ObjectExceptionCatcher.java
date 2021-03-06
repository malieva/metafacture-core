/*
 *  Copyright 2013 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.stream.pipe;

import org.culturegraph.mf.framework.DefaultObjectPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Wraps the call to the process method of the downstream module
 * in a try catch block with Exception as the catch-block's 
 * argument type. This module is supposed to stop any exception 
 * from downstream modules to travel further upstream. If an 
 * exception is caught, a log message with log level "error" is 
 * written. 
 * 
 * @param <T> object type
 * 
 * @author Christoph Böhme
 */
@Description("passes objects through and catches exceptions.")
@In(Object.class)
@Out(Object.class)
public final class ObjectExceptionCatcher<T> extends
		DefaultObjectPipe<T, ObjectReceiver<T>> {

	private static final Logger LOG = LoggerFactory.getLogger(ObjectExceptionCatcher.class);
	private static final String MSG_PATTERN = "{}{}";
	
	private final String logPrefix;
	
	public ObjectExceptionCatcher() {
		this("");
	}
	
	public ObjectExceptionCatcher(final String logPrefix) {
		super();
		this.logPrefix = logPrefix;
	}
	
	@Override
	public void process(final T obj) {
		try {
			getReceiver().process(obj);
		} catch(final Exception e) {  // NO CHECKSTYLE: IllegalCatch
			// This module is supposed to intercept _all_ exceptions
			// thrown by downstream modules. Hence, we have to catch
			// Exception.
			LOG.error(MSG_PATTERN, logPrefix, obj);
			LOG.error(MSG_PATTERN, logPrefix, e);
		}
	}

}

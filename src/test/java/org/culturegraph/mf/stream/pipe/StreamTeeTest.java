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

import static org.junit.Assert.fail;

import org.culturegraph.mf.exceptions.FormatException;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.stream.sink.EventList;
import org.culturegraph.mf.stream.sink.StreamValidator;
import org.junit.Test;


/**
 * Tests if {@link StreamTee} is correctly piping all events.
 * 
 * @author Markus Michael Geipel
 *
 */
public final class StreamTeeTest {
	
	@Test
	public void testCorrectTeeFunction() {
		final EventList list = new EventList();
		execTestEvents(list);
		
		final StreamValidator validator1 = new StreamValidator(list.getEvents());
		final StreamValidator validator2 = new StreamValidator(list.getEvents());
		final StreamTee tee = new StreamTee();
		tee.addReceiver(validator1)
			.addReceiver(validator2);
		
		try {
			execTestEvents(tee);
			tee.closeStream();
		} catch (FormatException e) {
			fail("Tee did not forward data as expected: " + e);
		}
	}
	
	private void execTestEvents(final StreamReceiver receiver) {
		receiver.startRecord("1");
		receiver.literal("l1", "value1");
		receiver.literal("l1", "value2");
		receiver.startEntity("e1");
		receiver.literal("l2", "value3");
		receiver.endEntity();
		receiver.endRecord();
		receiver.startRecord("2");
		receiver.literal("l3", "value4");
		receiver.endRecord();
	}

}

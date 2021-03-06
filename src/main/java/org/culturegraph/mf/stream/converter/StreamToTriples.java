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
package org.culturegraph.mf.stream.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.mf.framework.DefaultStreamPipe;
import org.culturegraph.mf.framework.ObjectReceiver;
import org.culturegraph.mf.framework.StreamReceiver;
import org.culturegraph.mf.framework.annotations.Description;
import org.culturegraph.mf.framework.annotations.In;
import org.culturegraph.mf.framework.annotations.Out;
import org.culturegraph.mf.types.Triple;

/**
 * 
 * @author Markus Michael Geipel
 * 
 */
@Description("Takes literals from a stream and emits them as triples such "
		+ "that the name and value become predicate and object and the record id the subject."
		+ "If 'redirect' is true, use '_id' to change the id, or '{to:ID}NAME' to change the id of a single literal.")
@In(StreamReceiver.class)
@Out(Triple.class)
public final class StreamToTriples extends DefaultStreamPipe<ObjectReceiver<Triple>> {
	public static final char SEPARATOR = '\u001e';

	private static final Pattern REDIRECT_PATTERN = Pattern.compile("^\\{to:(.+)}(.+)$");
	private static final String ID = "_id";

	private final List<String> nameBuffer = new ArrayList<String>();
	private final List<String> valueBuffer = new ArrayList<String>();
	private String currentId;
	private boolean redirect;

	public void setRedirect(final boolean redirect) {
		this.redirect = redirect;
	}

	@Override
	public void startRecord(final String identifier) {
		assert !isClosed();
		this.currentId = identifier;
	}

	@Override
	public void literal(final String name, final String value) {
		assert !isClosed();
		if (redirect) {
			if (ID.equals(name)) {
				currentId = value;
			} else {
				final Matcher matcher = REDIRECT_PATTERN.matcher(name);
				if (matcher.find()) {
					getReceiver().process(new Triple(matcher.group(1), matcher.group(2), value));
				} else {
					nameBuffer.add(name);
					valueBuffer.add(value);
				}
			}
		} else {
			getReceiver().process(new Triple(currentId, name, value));
		}
	}

	@Override
	public void endRecord() {
		assert !isClosed();
		if (redirect) {
			for (int i = 0; i < nameBuffer.size(); ++i) {
				getReceiver().process(new Triple(currentId, nameBuffer.get(i), valueBuffer.get(i)));
			}
			nameBuffer.clear();
			valueBuffer.clear();
		}
	}
}

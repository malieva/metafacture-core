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
package org.culturegraph.mf.types;

import java.util.Collection;
import java.util.Map;

/**
 * Simple value store with following a  Map&lt;String, Map&lt;String, String&gt;&gt; model. Just keeping the interface simpler. 
 * 
 * @author Markus Michael Geipel
 *
 */
public interface MultiMap {
	

	String DEFAULT_MAP_KEY = "__default";
	
	/**
	 * 
	 * @param mapName
	 * @return map corresponding to mapName. Never return <code>null</code>. If there is no corresponding {@link Map}, return empty one.
	 */
	Collection<String> getMapNames();
	Map<String, String> getMap(String mapName);
	String getValue(String mapName, String key);
	
	Map<String, String> putMap(String mapName, Map<String, String> map);
	String putValue(String mapName, String key, String value);

}

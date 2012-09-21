package org.jenkinsci.plugins.imagegallery.comparative;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FilePairTree implements Iterable<Map.Entry<String, Set<FilePair>>> {
	Map<String, Set<FilePair>> pairs = new TreeMap<String, Set<FilePair>>();

	public void addToBranch(String branch, FilePair leaf) {
		Set<FilePair> subs = pairs.get(branch);
        if (subs == null) {
            subs = new TreeSet<FilePair>();
            pairs.put(branch, subs);
        }
        subs.add(leaf);
	}

	public Iterator<Map.Entry<String, Set<FilePair>>> iterator() {
		return pairs.entrySet().iterator();
	}
}

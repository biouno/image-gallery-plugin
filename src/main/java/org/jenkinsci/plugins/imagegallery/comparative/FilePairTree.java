package org.jenkinsci.plugins.imagegallery.comparative;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class FilePairTree {
	//private Map<String, Set<FilePair>> pairs = new TreeMap<String, Set<FilePair>>();

	private Map<String, FilePairTree> nodes = new TreeMap<String, FilePairTree>();
	
	private Set<FilePair> leafs = new TreeSet<FilePair>(new Comparator<FilePair>() {
		public int compare(FilePair o1, FilePair o2) {
			return o1.getName().compareTo(o2.getName());
		}
	});
	
	public void addToBranch(List<String> branch, FilePair leaf) {
		if (branch == null) {
			return;
		}
		if (branch.size() == 0) {
			leafs.add(leaf);
		} else {
			String branchRoot = branch.remove(0);
			FilePairTree subs = nodes.get(branchRoot);
	        if (subs == null) {
	            subs = new FilePairTree();
	            nodes.put(branchRoot, subs);
	        }
            subs.addToBranch(branch, leaf);
		}
	}

	public Map<String, FilePairTree> getNodes() {
		return nodes;
	}
	
	public Set<FilePair> getLeafs() {
		return leafs;
	}
	
	//public Iterator<Map.Entry<String, Set<FilePair>>> iterator() {
		//return pairs.entrySet().iterator();
	//}
}

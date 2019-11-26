/*
 * The MIT License
 *
 * Copyright (c) 2012-2015 Richard Lavoie, BioUno
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.imagegallery.comparative;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Richard Lavoie
 * @since 1.0
 */
public class FilePairTree implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, FilePairTree> nodes = new TreeMap<String, FilePairTree>();
    
    @SuppressWarnings("unchecked")
    private Set<FilePair> leafs = new TreeSet<>(
        (Comparator<FilePair> & Serializable) (o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        }
    );
    
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
    
}

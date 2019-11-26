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

/**
 * File association group for a path and a set of files
 * 
 * @author Richard Lavoie
 * @since 1.0
 */
public class FilePair implements Comparable<FilePair>, Serializable {

    private static final long serialVersionUID = 1L;
    private String baseRoot;
	private String name;

	public FilePair(String folder, String name) {
		baseRoot = folder;
		this.name = name;
	}

	public String getBaseRoot() {
		return baseRoot;
	}

	public String getName() {
		return name;
	}

	public int compareTo(FilePair o) {
		return baseRoot.compareTo(o.baseRoot);
	}

	public String toString() {
		return baseRoot + ":" + name;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baseRoot == null) ? 0 : baseRoot.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FilePair other = (FilePair) obj;
        if (baseRoot == null) {
            if (other.baseRoot != null)
                return false;
        } else if (!baseRoot.equals(other.baseRoot))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}

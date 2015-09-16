/*
 * The MIT License
 *
 * Copyright (c) 2012-2015 Bruno P. Kinoshita, BioUno
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
package org.jenkinsci.plugins.imagegallery;

/**
 * Archived images gallery super type.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 1.1
 */
public abstract class AbstractArchivedImagesGallery extends ImageGallery {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = -2264764254582937499L;
    /**
	 * If checked, marks the build as unstable if no archives were found.
	 */
	private final boolean markBuildAsUnstableIfNoArchivesFound;
	
	@Deprecated
	public AbstractArchivedImagesGallery(String title, Integer imageWidth, 
			Boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, imageWidth);
		this.markBuildAsUnstableIfNoArchivesFound = markBuildAsUnstableIfNoArchivesFound;
	}
	
	public AbstractArchivedImagesGallery(String title, String imageWidth,
	        Boolean markBuildAsUnstableIfNoArchivesFound) {
	    super(title, imageWidth);
	    this.markBuildAsUnstableIfNoArchivesFound = markBuildAsUnstableIfNoArchivesFound;
	}
	
	/**
	 * @return the markBuildAsUnstableIfNoArchivesFound
	 */
	public boolean isMarkBuildAsUnstableIfNoArchivesFound() {
		return markBuildAsUnstableIfNoArchivesFound;
	}

}

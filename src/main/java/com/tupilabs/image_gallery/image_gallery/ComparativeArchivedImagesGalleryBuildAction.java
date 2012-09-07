/*
 * The MIT License
 *
 * Copyright (c) <2012> <Bruno P. Kinoshita>
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
package com.tupilabs.image_gallery.image_gallery;

import hudson.model.Action;
import org.apache.xalan.xsltc.util.IntegerArray;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * A project action with the list of archived image file names 
 * to be displayed.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class ComparativeArchivedImagesGalleryBuildAction implements Action, Serializable {

	private static final long serialVersionUID = -5987342090954152424L;
	
	/**
	 * The title.
	 */
	private final String title;
	/**
	 * The array of images.
	 */
	private final Map<String, Set<FilePair>> pairs;
	/**
	 * The image width.
	 */
	private final Integer imageWidth;

    /**
     * The inner image width
     */
    private Integer imageInnerWidth;
	
	/**
	 * Constructor with args.
	 * @param title
	 * @param imageWidth2 
	 * @param pairs 
	 * @param images
	 * @param imageWidth
	 */
	public ComparativeArchivedImagesGalleryBuildAction(String title, Map<String, Set<FilePair>> pairs, Integer imageWidth, Integer imageInnerWidth) {
		this.title = title;
		this.pairs = pairs;
        this.imageInnerWidth = imageInnerWidth;
		if(imageWidth != null) {
			this.imageWidth = imageWidth;
		} else {
			this.imageWidth = new Integer(0);
		}
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the images
	 */
	public Map<String, Set<FilePair>> getImages() {
		return pairs;
	}
	
	/* (non-Javadoc)
	 * @see hudson.model.Action#getIconFileName()
	 */
	public String getIconFileName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see hudson.model.Action#getDisplayName()
	 */
	public String getDisplayName() {
		return "Comparative Image Gallery";
	}

	/* (non-Javadoc)
	 * @see hudson.model.Action#getUrlName()
	 */
	public String getUrlName() {
		return "comparativeImageGallery";
	}

	/**
	 * @return the imageWidth
	 */
	public Integer getImageWidth() {
		return imageWidth;
	}

    /**
     * @return the imageInnerWidth
     */
    public Integer getImageInnerWidth() {
        return imageInnerWidth;
    }
}

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
package org.jenkinsci.plugins.imagegallery.imagegallery;

import java.io.Serializable;

import hudson.model.Action;

/**
 * A project action with the list of archived image file names 
 * to be displayed.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class ArchivedImagesGalleryBuildAction implements Action, Serializable {
	
	/*
     * serial version UID.
     */
    private static final long serialVersionUID = 3528624615017173605L;
    /**
	 * The title.
	 */
	private final String title;
	/**
	 * The array of images.
	 */
	private final String[] images;
	/**
	 * The image width.
	 */
	@Deprecated
	private Integer imageWidth;
	/**
	 * The image width.
	 */
	private final String imageWidthText;
	
	/**
	 * Constructor with args.
	 * @param title
	 * @param images
	 * @param imageWidth
	 */
	@Deprecated
	public ArchivedImagesGalleryBuildAction(String title, String[] images, Integer imageWidth) {
		this.title = title;
		this.images = images;
		if(imageWidth != null) {
			this.imageWidth = imageWidth;
		} else {
			this.imageWidth = Integer.valueOf(0);
		}
		imageWidthText = Integer.toString(this.imageWidth);
	}

	/**
     * Constructor with args.
     * @param title
     * @param images
     * @param imageWidth
     */
    public ArchivedImagesGalleryBuildAction(String title, String[] images, String imageWidth) {
        this.title = title;
        this.images = images;
        imageWidthText = imageWidth;
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
	public String[] getImages() {
		return images;
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
		return "Build Image Gallery";
	}

	/* (non-Javadoc)
	 * @see hudson.model.Action#getUrlName()
	 */
	public String getUrlName() {
		return "buildImageGallery";
	}

	/**
	 * @return the imageWidth
	 */
	@Deprecated
	public int getImageWidth() {
		return imageWidth;
	}

	public String getImageWidthText() {
	    return imageWidthText;
	}

    public Object readResolve() {
        String width = 
                (imageWidth != null && imageWidth > 0) ? Integer.toString(imageWidth) : "0";
        return new ArchivedImagesGalleryBuildAction(
                title,
                images,
                width /*imageWidthText*/);
    }

}

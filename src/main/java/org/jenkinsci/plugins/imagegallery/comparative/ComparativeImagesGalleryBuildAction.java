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

import hudson.model.Action;

import java.io.Serializable;

/**
 * A project action with a group of image files associated by a path and a set of files
 *
 * @author Richard Lavoie
 * @since 1.0
 */
public class ComparativeImagesGalleryBuildAction implements Action, Serializable {
	
	/*
     * serial version UID.
     */
    private static final long serialVersionUID = -3667733389967779689L;
    /**
	 * The title.
	 */
	private final String title;
	/**
	 * The array of images.
	 */
	private final FilePairTree tree;
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
     * The inner image width
     */
	@Deprecated
    private Integer imageInnerWidth;
	/**
	 * The image inner width.
	 */
	private final String imageInnerWidthText;
	/**
	 * Constructor with args.
	 * @param title gallery title
	 * @param tree file pair tree 
	 * @param imageWidth image width
	 * @param imageInnerWidth image inner width
	 */
	@Deprecated
	public ComparativeImagesGalleryBuildAction(String title, FilePairTree tree, Integer imageWidth, Integer imageInnerWidth) {
		this.title = title;
		this.tree = tree;
        this.imageInnerWidth = imageInnerWidth;
        imageWidthText = Integer.toString(imageInnerWidth);
		if(imageWidth != null) {
			this.imageWidth = imageWidth;
		} else {
		    this.imageWidth = Integer.valueOf(0);
		}
		imageInnerWidthText = Integer.toString(this.imageWidth);
	}

	/**
     * Constructor with args.
     * @param title gallery title
     * @param tree file pair tree
     * @param imageWidthText image width
     * @param imageInnerWidthText image inner width
     */
    public ComparativeImagesGalleryBuildAction(String title, FilePairTree tree, String imageWidthText, String imageInnerWidthText) {
        this.title = title;
        this.tree = tree;
        this.imageInnerWidthText = imageInnerWidthText;
        if(imageWidthText != null) {
            this.imageWidthText = imageWidthText;
        } else {
            this.imageWidthText = "";
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
	public FilePairTree getImages() {
		return tree;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getIconFileName() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDisplayName() {
		return "Comparative Image Gallery";
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUrlName() {
		return "comparativeImageGallery";
	}

	/**
	 * @return the imageWidth
	 */
	@Deprecated
	public Integer getImageWidth() {
		return imageWidth;
	}

	/**
	 * @return the imageWidthText
	 */
	public String getImageWidthText() {
        return imageWidthText;
    }

    /**
     * @return the imageInnerWidth
     */
	@Deprecated
    public Integer getImageInnerWidth() {
        return imageInnerWidth;
    }
	/**
	 * @return the imageInnerWidthText
	 */
	public String getImageInnerWidthText() {
        return imageInnerWidthText;
    }

	public Object readResolve() {
	    String width = 
	            (imageWidth != null && imageWidth > 0) ? Integer.toString(imageWidth) : "";
        String innerWidth = 
                (imageInnerWidth != null && imageInnerWidth > 0) ? Integer.toString(imageInnerWidth) : "0";
	    return new ComparativeImagesGalleryBuildAction(
	            title,
	            tree,
	            width /*imageWidthText*/,
	            innerWidth /*imageInnerWidthText*/);
	}

}

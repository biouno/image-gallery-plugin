/*
 * The MIT License
 *
 * Copyright (c) <2012> <Richard Lavoie>
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

import hudson.FilePath;
import hudson.util.FormValidation;

import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.imagegallery.AbstractArchivedImagesGallery;
import org.jenkinsci.plugins.imagegallery.ImageGalleryDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;


/**
 * Base image gallery to compare images in different way.
 *
 * @author Richard Lavoie
 * @since 1.0
 */
public abstract class ComparativeArchivedImagesGallery extends AbstractArchivedImagesGallery {


	/*
     * serial UID.
     */
    private static final long serialVersionUID = 8022990812020649261L;

    /**
	 * Title.
	 */
	private final String title;
	
	/**
	 * Include pattern.
	 */
	private final String baseRootFolder;
	
	/**
     * Images width.
     */
    private final String imageInnerWidth;

	/**
	 * Constructor called from jelly.
	 * @param title
	 * @param baseRootFolder
	 * @param imageWidth
	 * @param imageInnerWidth
	 * @param markBuildAsUnstableIfNoArchivesFound
	 */
	@DataBoundConstructor
	public ComparativeArchivedImagesGallery(String title, String baseRootFolder, String imageWidth, String imageInnerWidth,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, imageWidth, markBuildAsUnstableIfNoArchivesFound);
		this.title = title;
		this.baseRootFolder = baseRootFolder;
        this.imageInnerWidth = imageInnerWidth;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return the includes
	 */
	public String getBaseRootFolder() {
		return baseRootFolder;
	}

    /**
     * @return the imageInnerWidth
     */
    public String getImageInnerWidth() {
        return imageInnerWidth;
    }

	public static abstract class ComparativeDescriptorImpl extends ImageGalleryDescriptor {

        public FormValidation doCheckImageInnerWidth(StaplerRequest req, StaplerResponse resp, @QueryParameter String imageInnerWidth) throws Exception {
            return FormValidation.validateRequired(imageInnerWidth);
        }

	}

	protected List<String> getRelativeFrom(FilePath file, FilePath parent) {
		List<String> path = new ArrayList<String>();
		FilePath temp = file;
		while(temp.getParent() != null && !temp.getParent().equals(parent)) {
			path.add(0,temp.getParent().getName());
			temp = temp.getParent();
		}
		path.add(file.getName());
		return path;
	}
	
	
}

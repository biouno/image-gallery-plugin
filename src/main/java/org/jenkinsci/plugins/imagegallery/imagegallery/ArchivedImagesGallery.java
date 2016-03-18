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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jenkinsci.plugins.imagegallery.AbstractArchivedImagesGallery;
import org.jenkinsci.plugins.imagegallery.ImageGalleryDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;

/**
 * An image gallery of archived artifacts. Its descriptor is a static inner 
 * class.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class ArchivedImagesGallery extends AbstractArchivedImagesGallery {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = 165095831454744155L;

    private static Logger LOGGER = Logger.getLogger("com.tupilabs.image_gallery");
	
	/**
	 * Include pattern.
	 */
	private final String includes;

	/**
	 * Constructor called from jelly.
	 * @param includes
	 * @param imageWidth
	 * @param markBuildAsUnstableIfNoArchivesFound
	 */
	@Deprecated
	public ArchivedImagesGallery(String title, String includes, Integer imageWidth,
			Boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, imageWidth, markBuildAsUnstableIfNoArchivesFound);
		this.includes = includes;
	}

	/**
     * Constructor called from jelly.
     * @param includes
     * @param imageWidth
     * @param markBuildAsUnstableIfNoArchivesFound
     */
    @DataBoundConstructor
    public ArchivedImagesGallery(String title, String includes, String imageWidth,
            Boolean markBuildAsUnstableIfNoArchivesFound) {
        super(title, imageWidth, markBuildAsUnstableIfNoArchivesFound);
        this.includes = includes;
    }

	@Extension
	public static class DescriptorImpl extends ImageGalleryDescriptor {
		/* (non-Javadoc)
		 * @see hudson.model.Descriptor#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return "Archived images gallery";
		}
	}
	
	/**
	 * @return the includes
	 */
	public String getIncludes() {
		return includes;
	}

	/* (non-Javadoc)
	 * @see com.tupilabs.image_gallery.image_gallery.ImageGallery#createImageGallery(hudson.model.AbstractBuild, hudson.model.BuildListener)
	 */
	@Override
	public boolean createImageGallery(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().append("Creating archived images gallery.");
		if(build.getHasArtifacts()) {
			File artifactsDir = build.getArtifactsDir();
			FilePath artifactsPath = new FilePath(artifactsDir);
			FilePath[] foundFiles = artifactsPath.list(getIncludes());
			if(LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Found " + (foundFiles != null ? foundFiles.length : 0) + " files.");
			}
			if(foundFiles != null && foundFiles.length > 0) {
				List<String> images = new ArrayList<String>();
				for(FilePath foundFile : foundFiles) {
					String fileName = "";
					FilePath temp = foundFile;
					while(!temp.getParent().equals(artifactsPath)) {
						fileName = temp.getParent().getName() + "/" + fileName;
						temp = temp.getParent();
					}
					if(fileName.length() > 0) {
						fileName += "/";
					}
					fileName += foundFile.getName();
					images.add(fileName);
				}
				String title = Util.replaceMacro(build.getEnvironment(listener).expand(getTitle()), build.getBuildVariableResolver());
				build.addAction(new ArchivedImagesGalleryBuildAction(title, images.toArray(new String[0]), getImageWidthText()));
			} else {
				listener.getLogger().append("No files found for image gallery.");
			}
		} else {
			if(isMarkBuildAsUnstableIfNoArchivesFound()) {
				build.setResult(Result.UNSTABLE);
			}
			listener.getLogger().append("This build has no artifacts. Skipping image gallery in this build.");
		}
		return true;
	}

	public Object readResolve() {
	    Integer imageWidth = getImageWidth();
	    String width = getImageWidthText();
	    if (imageWidth != null && imageWidth > 0) {
	        width = Integer.toString(imageWidth);
	    }
	    return new ArchivedImagesGallery(getTitle(),
	            getIncludes(),
	            width,
	            isMarkBuildAsUnstableIfNoArchivesFound());
	}

}

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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;

/**
 * An image gallery of archived artifacts Comparing same files in different folders.
 *
 * @author Richard Lavoie
 * @since 1.0
 */
public class MultipleFolderComparativeArchivedImagesGallery extends ComparativeArchivedImagesGallery {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = -4861153536599621098L;

    private static Logger LOGGER = Logger.getLogger(MultipleFolderComparativeArchivedImagesGallery.class.getName());
	
	/**
	 * Constructor called from jelly.
	 * @param includes
	 * @param imageWidth
	 * @param markBuildAsUnstableIfNoArchivesFound
	 */
	@Deprecated
	public MultipleFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
			boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, baseRootFolder, imageWidth, imageInnerWidth, markBuildAsUnstableIfNoArchivesFound);
	}
	
	/**
     * Constructor called from jelly.
     * @param includes
     * @param imageWidth
     * @param markBuildAsUnstableIfNoArchivesFound
     */
	@DataBoundConstructor
    public MultipleFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, String imageWidth, String imageInnerWidth,
            boolean markBuildAsUnstableIfNoArchivesFound) {
        super(title, baseRootFolder, imageWidth, imageInnerWidth, markBuildAsUnstableIfNoArchivesFound);
    }

	@Extension
	public static class DescriptorImpl extends ComparativeDescriptorImpl {

		@Override
		public String getDisplayName() {
			return "Multiple folder comparative archived images gallery";
		}

	}

	@Override
	public boolean createImageGallery(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().append("Creating archived images gallery.");
		if (build.getHasArtifacts()) {
			File artifactsDir = build.getArtifactsDir().getAbsoluteFile();
			FilePath artifactsPath = new FilePath(new File(artifactsDir.getAbsoluteFile(), getBaseRootFolder()));
			List<FilePath> baseFolders = artifactsPath.list();
  			if(LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Found " + (baseFolders != null ? baseFolders.size() : 0) + " files.");
			}
			if(baseFolders != null && baseFolders.size() > 0) {
				FilePairTree tree = new FilePairTree();
				for (FilePath folder : baseFolders) {
					FilePath[] files = folder.list("**");
					for (FilePath path : files) {
						List<String> filepath = getRelativeFrom(path, folder);
                        List<String> artifactsRelativeFile = getRelativeFrom(path, artifactsPath.getParent());
						tree.addToBranch(filepath, new FilePair(folder.getName(), StringUtils.join(artifactsRelativeFile, '/')));
					}
				}
				String title = Util.replaceMacro(build.getEnvironment(listener).expand(getTitle()), build.getBuildVariableResolver());
				build.addAction(new ComparativeImagesGalleryBuildAction(title, tree, getImageWidthText(), getImageInnerWidthText()));
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
        Integer imageInnerWidth = getImageInnerWidth();
        String innerWidth = getImageInnerWidthText();
        if (imageInnerWidth != null && imageInnerWidth > 0) {
            innerWidth = Integer.toString(imageInnerWidth);
        }
        return new MultipleFolderComparativeArchivedImagesGallery(
                getTitle(),
                getBaseRootFolder(),
                width,
                innerWidth,
                isMarkBuildAsUnstableIfNoArchivesFound());
	}
}

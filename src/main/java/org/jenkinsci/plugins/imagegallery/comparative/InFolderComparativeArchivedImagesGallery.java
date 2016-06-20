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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.AbortException;
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
public class InFolderComparativeArchivedImagesGallery extends ComparativeArchivedImagesGallery {

	/*
     * serial version UID.
     */
    private static final long serialVersionUID = 5537875107916417555L;

    /**
	 * Constructor called from jelly.
	 * 
	 * @param title Title of the image gallery
	 * @param baseRootFolder Root folder where the images will be retrieve from
	 * @param imageWidth thumbnail width of each images
	 * @param imageInnerWidth Width for the images in the popup display
	 * @param markBuildAsUnstableIfNoArchivesFound Mark the build as unstable if no archives found
	 */
	@Deprecated
	public InFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, baseRootFolder, imageWidth, imageInnerWidth, markBuildAsUnstableIfNoArchivesFound);
	}

	 /**
     * Constructor called from jelly.
     * 
     * @param title Title of the image gallery
     * @param baseRootFolder Root folder where the images will be retrieve from
     * @param imageWidthText thumbnail width of each images
     * @param imageInnerWidthText Width for the images in the popup display
     * @param markBuildAsUnstableIfNoArchivesFound Mark the build as unstable if no archives found
     */
    @DataBoundConstructor
    public InFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, String imageWidthText, String imageInnerWidthText,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
        super(title, baseRootFolder, imageWidthText, imageInnerWidthText, markBuildAsUnstableIfNoArchivesFound);
    }

	@Extension
	public static class DescriptorImpl extends ComparativeDescriptorImpl {
		@Override
		public String getDisplayName() {
			return "In folder comparative archived images gallery";
		}

	}

	@Override
	public boolean createImageGallery(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().append("Creating archived images gallery.");
		if (build.getHasArtifacts()) {
            File artifactsRootDir = build.getArtifactsDir().getAbsoluteFile();
            File artifactsDir = new File(artifactsRootDir, getBaseRootFolder());

            // abort build if the gallery is using a file outside of the project artifacts directory
            if (!isChild(artifactsRootDir, artifactsDir)) {
                throw new AbortException("Invalid base root folder: " + getBaseRootFolder());
            }

            FilePath artifactsPath = new FilePath(artifactsDir);
            FilePath[] files = artifactsPath.list("**");

            if (files != null && files.length > 0) {
            	FilePairTree tree = new FilePairTree();
                for (FilePath path : files) {
                        List<String> folder = getRelativeFrom(path.getParent(), artifactsPath);
                        List<String> artifactsRelativeFile = getRelativeFrom(path, artifactsPath);
                        tree.addToBranch(folder, new FilePair(path.getName(), StringUtils.join(artifactsRelativeFile, '/')));
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

    /**
     * Verify that the {@code child} file is a child node of the {@code parent} directory tree. File names are
     * normalised before the comparison (i.e. ../ and ./ are evaluated). Uses Java 8 {@link java.nio.file.Path}
     * methods.
     *
     * @param parent parent file
     * @param child child file
     * @return {@code true} iff child is child directory of parent directory tree
     */
    private boolean isChild(File parent, File child) {
        Path parentPath = Paths.get(parent.getAbsolutePath()).normalize().toAbsolutePath();
        Path childPath = Paths.get(child.getAbsolutePath()).normalize().toAbsolutePath();
        return childPath.startsWith(parentPath);
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
        return new InFolderComparativeArchivedImagesGallery(
                getTitle(),
                getBaseRootFolder(),
                width,
                innerWidth,
                isMarkBuildAsUnstableIfNoArchivesFound());
	}

}

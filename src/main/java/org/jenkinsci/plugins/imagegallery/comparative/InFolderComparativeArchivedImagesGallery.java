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

import hudson.Extension;
import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * An image gallery of archived artifacts Comparing same files in different folders.
 *
 * @author Richard Lavoie
 * @since 0.2
 */
public class InFolderComparativeArchivedImagesGallery extends ComparativeArchivedImagesGallery {

	private static final long serialVersionUID = -1981209232197421074L;

	/**
	 * Constructor called from jelly.
	 * 
	 * @param title Title of the image gallery
	 * @param baseRootFolder Root folder where the images will be retrieve from
	 * @param imageWidth thumbnail width of each images
	 * @param imageInnerWidth Width for the images in the popup display
	 * @param markBuildAsUnstableIfNoArchivesFound Mark the build as unstable if no archives found
	 */
	@DataBoundConstructor
	public InFolderComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
                                                    boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, baseRootFolder, imageWidth, imageInnerWidth, markBuildAsUnstableIfNoArchivesFound);
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
			File artifactsDir = build.getArtifactsDir().getAbsoluteFile();
			FilePath artifactsPath = new FilePath(new File(artifactsDir.getAbsoluteFile(), getBaseRootFolder()));
            FilePath[] files = artifactsPath.list("**");

            if (files != null && files.length > 0) {
            	FilePairTree tree = new FilePairTree();
                for (FilePath path : files) {
                        List<String> folder = getRelativeFrom(path.getParent(), artifactsPath);
                        List<String> artifactsRelativeFile = getRelativeFrom(path, artifactsPath.getParent());
                        tree.addToBranch(folder, new FilePair(path.getName(), StringUtils.join(artifactsRelativeFile, '/')));
                }

				build.addAction(new ComparativeImagesGalleryBuildAction(getTitle(), tree, getImageWidth(), getImageInnerWidth()));
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
	
}

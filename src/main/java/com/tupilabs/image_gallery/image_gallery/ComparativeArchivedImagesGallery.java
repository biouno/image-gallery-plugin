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

import hudson.Extension;
import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.util.FormValidation;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * An image gallery of archived artifacts Comparing same files in different folders.
 *
 * @author Richard Lavoie
 * @since 0.1
 */
public class ComparativeArchivedImagesGallery extends ImageGallery {

	private static Logger LOGGER = Logger.getLogger("com.tupilabs.image_gallery");
	
	private static final long serialVersionUID = -1981209232197421074L;

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
	private final Integer imageWidth;
    /**
     * Images width.
     */
    private final Integer imageInnerWidth;

	/**
	 * If checked, marks the build as unstable if no archives were found.
	 */
	private final boolean markBuildAsUnstableIfNoArchivesFound;
	
	/**
	 * Constructor called from jelly.
	 * @param includes
	 * @param imageWidth
	 * @param markBuildAsUnstableIfNoArchivesFound
	 */
	@DataBoundConstructor
	public ComparativeArchivedImagesGallery(String title, String baseRootFolder, Integer imageWidth, Integer imageInnerWidth,
			boolean markBuildAsUnstableIfNoArchivesFound) {
		super();
		this.title = title;
		this.baseRootFolder = baseRootFolder;
		this.imageWidth = imageWidth;
        this.imageInnerWidth = imageInnerWidth;
		this.markBuildAsUnstableIfNoArchivesFound = markBuildAsUnstableIfNoArchivesFound;
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

    /**
	 * @return the markBuildAsUnstableIfNoArchivesFound
	 */
	public boolean isMarkBuildAsUnstableIfNoArchivesFound() {
		return markBuildAsUnstableIfNoArchivesFound;
	}

	@Extension
	public static class DescriptorImpl extends ImageGalleryDescriptor {
		/* (non-Javadoc)
		 * @see hudson.model.Descriptor#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return "Comparative archived images gallery";
		}

        public FormValidation doCheckImageInnerWidth(StaplerRequest req, StaplerResponse resp, @QueryParameter String imageInnerWidth) throws Exception {
            return FormValidation.validateRequired(imageInnerWidth);
        }

	}

	@Override
	public boolean createImageGallery(AbstractBuild<?, ?> build, BuildListener listener) throws InterruptedException, IOException {
		listener.getLogger().append("Creating archived images gallery.");
		if(build.getHasArtifacts()) {
			File artifactsDir = build.getArtifactsDir().getAbsoluteFile();
			FilePath artifactsPath = new FilePath(new File(artifactsDir.getAbsoluteFile(), baseRootFolder));
			List<FilePath> baseFolders = artifactsPath.list();
  			if(LOGGER.isLoggable(Level.FINE)) {
				LOGGER.log(Level.FINE, "Found " + (baseFolders != null ? baseFolders.size() : 0) + " files.");
			}
			if(baseFolders != null && baseFolders.size() > 0) {
				Map<String, Set<FilePair>> pairs = new TreeMap<String, Set<FilePair>>();
				for (FilePath folder : baseFolders) {
					FilePath[] files = folder.list("**");
					for (FilePath path : files) {
						String fileName = getRelativeFrom(path, folder);
                        String artifactsRelativeFile = getRelativeFrom(path, artifactsPath.getParent());
						Set<FilePair> subs = pairs.get(fileName);
						if (subs == null) {
							subs = new TreeSet<FilePair>();
							pairs.put(fileName, subs);
						}					
						subs.add(new FilePair(folder.getName(), artifactsRelativeFile));
					}
				}
				
				
				build.addAction(new ComparativeArchivedImagesGalleryBuildAction(this.title, pairs, imageWidth, imageInnerWidth));
			} else {
				listener.getLogger().append("No files found for image gallery.");
			}
		} else {
			if(markBuildAsUnstableIfNoArchivesFound) {
				build.setResult(Result.UNSTABLE);
			}
			listener.getLogger().append("This build has no artifacts. Skipping image gallery in this build.");
		}
		return true;
	}

	private String getRelativeFrom(FilePath file, FilePath parent) {
		String fileName = "";
		FilePath temp = file;
		while(!temp.getParent().equals(parent)) {
			fileName = temp.getParent().getName() + "/" + fileName;
			temp = temp.getParent();
		}
		if(fileName.length() > 0) {
			fileName += "/";
		}
		fileName += file.getName();
		return fileName;
	}
	
	
}

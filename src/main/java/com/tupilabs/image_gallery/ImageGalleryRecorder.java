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
package com.tupilabs.image_gallery;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 * An image gallery recorder. This recorder is responsible for looking for 
 * image files and storing them in the master node.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
@SuppressWarnings("unchecked")
public class ImageGalleryRecorder extends Recorder {

	@Extension
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
	
	private final String includes;
	private final Integer imageWidth;
	
	
	@DataBoundConstructor
	public ImageGalleryRecorder(String includes, Integer imageWidth) {
		this.includes = includes;
		this.imageWidth = imageWidth;
	}
	
	/**
	 * @return the includes
	 */
	public String getIncludes() {
		return includes;
	}
	
	/**
	 * @return the imageWidth
	 */
	public Integer getImageWidth() {
		return imageWidth;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see hudson.tasks.BuildStep#getRequiredMonitorService()
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
	
	/* (non-Javadoc)
	 * @see hudson.tasks.BuildStepCompatibilityLayer#getProjectActions(hudson.model.AbstractProject)
	 */
	@Override
	public Collection<? extends Action> getProjectActions(
			AbstractProject<?, ?> project) {
		if(project.getLastBuild() != null) {
			try {
				List<String> images = new ArrayList<String>();
				FilePath ws = project.getSomeWorkspace();
				if(ws != null) {
					FilePath imagesFolder = ws.child("images");
					List<FilePath> existentImages = imagesFolder.list();
					if(existentImages != null && existentImages.size() > 0) {
						for(FilePath image : existentImages) {
							String imagePath = image.getName();
							images.add(imagePath);
						}
					}
				}
				ImageGalleryProjectAction action = new ImageGalleryProjectAction(images.toArray(new String[0]), this.imageWidth);
				return Arrays.asList(action);
			} catch(InterruptedException ie) {
				return Collections.emptyList();
			}
			catch(IOException ie) {
				return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}
	
	/* (non-Javadoc)
	 * @see hudson.tasks.BuildStepCompatibilityLayer#perform(hudson.model.AbstractBuild, hudson.Launcher, hudson.model.BuildListener)
	 */
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		FilePath imagesFolder = build.getWorkspace().child("images");
		if(!imagesFolder.exists()) {
			imagesFolder.mkdirs();
		}
		for(FilePath existentImage : imagesFolder.list()) {
			existentImage.delete();
		}
		FilePath[] foundFiles = build.getWorkspace().list(includes);
		if(foundFiles != null && foundFiles.length > 0) {
			for(FilePath foundFile : foundFiles) {
				if(foundFile.exists()) {
					FilePath dst = imagesFolder.child(foundFile.getName());
					foundFile.copyToWithPermission(dst);
				}
			}
		}
//		List<FilePath> images = imagesFolder.list();
//		if(images != null && images.size() > 0) {
//			for(FilePath image : images) {
//				String imagePath = image.getName();
//				this.images.add(imagePath);
//			}
//		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see hudson.tasks.Recorder#getDescriptor()
	 */
	@Override
	public BuildStepDescriptor getDescriptor() {
		return DESCRIPTOR;
	}
	
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		public DescriptorImpl() {
			super(ImageGalleryRecorder.class);
		}
		
		/* (non-Javadoc)
		 * @see hudson.model.Descriptor#getDisplayName()
		 */
		@Override
		public String getDisplayName() {
			return "Create lightbox image gallery";
		}

		/* (non-Javadoc)
		 * @see hudson.tasks.BuildStepDescriptor#isApplicable(java.lang.Class)
		 */
		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
			return true;
		}
		
		/*
		 * --- Validation methods ---
		 */
		public FormValidation doCheckMandatory(@QueryParameter String value) {
			FormValidation returnValue = FormValidation.ok();
			if (StringUtils.isBlank(value)) {
				returnValue = FormValidation.error("This option is required");
			}
			return returnValue;
		}
		
	}

}

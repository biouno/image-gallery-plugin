/*
 * The MIT License
 *
 * Copyright (c) 2012-2016 Bruno P. Kinoshita, BioUno
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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.jenkinsci.plugins.imagegallery.ImageGallery;
import org.jenkinsci.plugins.imagegallery.ImageGalleryRecorder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.queue.QueueTaskFuture;
import hudson.tasks.ArtifactArchiver;
import hudson.tasks.Shell;

/**
 * Tests for JENKINS-32447. Make sure that the includes variable is being
 * expanded with build variables. These tests are specific for the
 * {@link ArchivedImagesGallery} class.
 * 
 * @author Bruno P. Kinoshita
 * @since 1.3
 */
public class TestExpandingIncludes {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    private ArchivedImagesGallery gallery;

    @Before
    public void setUp() {
        gallery = new ArchivedImagesGallery("Le title", "image-*-$BUILD_NUMBER.jpg", "300px", Boolean.FALSE);
    }

    @Test
    public void testIncludesIsExpanded() throws Exception {
        // Create test project
        FreeStyleProject project = jenkins.getInstance().createProject(FreeStyleProject.class, "test job");
        // Create a fake image file with the correct name
        Shell shell = new Shell("touch image-test-${BUILD_NUMBER}.jpg");
        project.getBuildersList().add(shell);
        // Archive images
        ArtifactArchiver archiver = new ArtifactArchiver("**/*.jpg");
        project.getPublishersList().add(archiver);
        // Add the image gallery
        ImageGalleryRecorder recorder = new ImageGalleryRecorder(Arrays.<ImageGallery> asList(gallery));
        project.getPublishersList().add(recorder);
        // Trigger the build
        QueueTaskFuture<FreeStyleBuild> build = project.scheduleBuild2(0);
        // Executing this build should take less than 1.5 minutes
        FreeStyleBuild freeStyleBuild = build.get(90, TimeUnit.SECONDS);
        // And get the action with the images
        ArchivedImagesGalleryBuildAction action = freeStyleBuild.getAction(ArchivedImagesGalleryBuildAction.class);
        String[] images = action.getImages();
        assertTrue("Given we used the right variable in the job configuration, we should have one image in this job",
                images.length == 1);
    }

}

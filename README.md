This plug-in reads a job workspace and collects images to produce an
image gallery using
[colorbox](http://www.jacklmoore.com/colorbox) lightbox
Javascript library.

## Description

This plug-in reads a job workspace and collects images to produce an
image gallery using
[colorbox](http://www.jacklmoore.com/colorbox) lightbox
Javascript library.

This plug-in was created to mimic the behaviour of
[Bioportal](http://www.bioportal.uio.no/) (now
[Lifeportal](https://lifeportal.uio.no/)) jobs that
display images of submitted jobs. The development started
in [BioUno](http://www.biouno.org/), a project that uses
Jenkins for running bioinformatics tools and managing clusters.

## Gallery Types

### Archived images gallery

This gallery is created from archived images. It means that you have to
use the post build step to archive artifacts and give the gallery a
pattern that will match these files.

The images in the Screenshot section were taken from a gallery of this
type.

### In folder comparative archived images gallery

This gallery is created from archived images. It means that you have to
use the post build step to archive artifacts and give the gallery a
pattern that will match these files.

This gallery is useful when your build generates images in multiple
folder, but with a common base root dir. It receives a base root dir as
parameter, and it must be relative to the workspace. Then it will create
a navigation tree with the images per folder. The screen shot below
should help to clarify the explanation. In the example below the
directories 3 and 4 both had images, and a common ancestor directory,
used as base root dir.

![](https://wiki.jenkins.io/download/attachments/63144496/in_folder_gallery.png?version=1&modificationDate=1397700427000&api=v2)

### Multiple folder comparative archived images gallery

This gallery is created from archived images. It means that you have to
use the post build step to archive artifacts and give the gallery a
pattern that will match these files.

It is similar to the previous gallery type, but each image will be an
entry in the navigation tree. It means that if your directory has three
pictures, each one will become an entry in the navigation tree. If you
were using the in folder gallery, then you would have a gallery with
three pictures. The screen shot below shows the difference between the
multiple folder (top) and the in folder (bottom) galleries.

Note that by clicking structure01.png folder in the upper gallery, you
would see the picture thumbnail. While, on the other hand, the other
gallery displays the two pictures in a single category.

![](https://wiki.jenkins.io/download/attachments/63144496/multiple_gallery_01.png?version=1&modificationDate=1397703017000&api=v2)

## Using environment variables in your gallery title

Since 1.1 you can use [Jenkins set environment
variables](https://wiki.jenkins-ci.org/display/JENKINS/Building+a+software+project#Buildingasoftwareproject-JenkinsSetEnvironmentVariables)
and other variables in your build in your gallery title. 

![](https://wiki.jenkins.io/download/attachments/63144496/expand_variables.png?version=1&modificationDate=1397693563000&api=v2)

![](https://wiki.jenkins.io/download/attachments/63144496/expanded_variable.png?version=2&modificationDate=1397693749000&api=v2)

## Screenshots

![](https://wiki.jenkins.io/download/attachments/63144496/screenshot1.png?version=1&modificationDate=1343939269000&api=v2)

![](https://wiki.jenkins.io/download/attachments/63144496/screenshot2.png?version=1&modificationDate=1343939274000&api=v2)

![](https://wiki.jenkins.io/download/attachments/63144496/screenshot3.png?version=1&modificationDate=1343939278000&api=v2)

![](https://wiki.jenkins.io/download/attachments/63144496/screenshot4.png?version=1&modificationDate=1343939284000&api=v2)

You can use the arrow keys of your keyboard to navigate in your gallery.

## Change Log

### Release 2.0.1 (1/Dec/2019)

- Migrated docs from Confluence to GitHub

### Release 2.0 (30/Nov/2019)

This release was in the experimental update center one week before the final release.

- [Upgrade colorbox to 1.6.4 (was v1.3.19.3)](https://github.com/jenkinsci/image-gallery-plugin/pull/2)
- Upgraded minimum Jenkins requirement
- Fix FindBugs issues due to new requirements from Jenkins parent
- Replaced tabs by spaces in some files
- Fix Jelly security issues due to new requirements from Jenkins parent
- Removed 1.x deprecated code
- Fix Jenkins.getInstance() deprecated call, use Jenkins.get()

### Release 1.4 (21/Jun/2016)

- [SECURITY-278: image-gallery: Information
  disclosure](https://issues.jenkins-ci.org/browse/SECURITY-278)

### Release 1.3 (19/Mar/2016)

- JENKINS-32447: BUILD\_NUMBER in include pattern
- JENKINS-31364: Image width is set to "0" in previous builds

### Release 1.2 (16/Sep/2015)

- JENKINS-23772: Percent for image width attribute
- JENKINS-22888: Colorbox viewer does not appear to work
- JENKINS-22887: Initial graph horizontal layout does not fit browser
  width
- JENKINS-22625: Allow customization of gallery title with
  Build/Project Parameters

### Release 1.1 (17/Apr/2014)

- JENKINS-22625: Allow customization of gallery title with
  Build/Project Parameters

### Release 1.0 (21/Sep/2012)

- Two more types of galleries contributed by Richard Lavoie (darkrift)

### Release 0.1 (07/Aug/2012)

- Initial release, with only one image-gallery type supported
  (Archived Images Gallery).

## Roadmap

- Add more gallery types
- Add support to different mime-types
- Add ability to display non-flat galleries. i.e. if a image directory
  has sub directories, each directory becomes a gallery, displayed
  accordingly by the plug-in.
- a gallery where you can have multiple images with the same name but
  from different folders, useful to compare images with selenium
  coming from different browsers, where images would have the same
  name, but coming from a different folder, recursively of course
  (darkrift)

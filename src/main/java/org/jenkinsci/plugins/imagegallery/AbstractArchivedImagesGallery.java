package org.jenkinsci.plugins.imagegallery;

/**
 * Archived images gallery super type.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 1.1
 */
public abstract class AbstractArchivedImagesGallery extends ImageGallery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2264764254582937499L;
	
	/**
	 * If checked, marks the build as unstable if no archives were found.
	 */
	private final boolean markBuildAsUnstableIfNoArchivesFound;
	
	public AbstractArchivedImagesGallery(String title, Integer imageWidth, 
			Boolean markBuildAsUnstableIfNoArchivesFound) {
		super(title, imageWidth);
		this.markBuildAsUnstableIfNoArchivesFound = markBuildAsUnstableIfNoArchivesFound;
	}
	
	/**
	 * @return the markBuildAsUnstableIfNoArchivesFound
	 */
	public boolean isMarkBuildAsUnstableIfNoArchivesFound() {
		return markBuildAsUnstableIfNoArchivesFound;
	}

}

package com.tupilabs.image_gallery.image_gallery;



public class FilePair implements Comparable<FilePair> {

	private String baseRoot;
	private String name;

	public FilePair(String folder, String name) {
		baseRoot = folder;
		this.name = name;
	}
	
	public String getBaseRoot() {
		return baseRoot;
	}
	
	public String getName() {
		return name;
	}

	public int compareTo(FilePair o) {
		return baseRoot.compareTo(o.baseRoot);
	}
	
	public String toString() {
		return baseRoot + ":" + name;
	}
	
}

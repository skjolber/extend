package com.github.skjolber.extend.core.read;

@FunctionalInterface
public interface ArchiveFileReader {

	void readFile(String file, byte[] classFile);
	
}

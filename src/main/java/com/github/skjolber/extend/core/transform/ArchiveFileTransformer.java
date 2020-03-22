package com.github.skjolber.extend.core.transform;

@FunctionalInterface
public interface ArchiveFileTransformer {

	byte[] transformFile(String file, byte[] classFile);
	
}

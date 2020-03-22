package com.github.skjolber.extend.core.read;

import org.objectweb.asm.ClassReader;

public class DefaultArchiveFileReader implements ArchiveFileReader {

	protected ReadClassVisitorFactory factory;
	
	public DefaultArchiveFileReader(ReadClassVisitorFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public void readFile(String file, byte[] classFile) {
	    ClassReader reader = new ClassReader(classFile);
	    
        reader.accept(factory.createReader(file, classFile), 0);
	}

}

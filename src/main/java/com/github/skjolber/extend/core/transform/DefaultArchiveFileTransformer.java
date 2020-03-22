package com.github.skjolber.extend.core.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class DefaultArchiveFileTransformer implements ArchiveFileTransformer {

	protected TransformClassVisitorFactory factory;
	
	public DefaultArchiveFileTransformer(TransformClassVisitorFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public byte[] transformFile(String file, byte[] classFile) {
	    ClassReader reader = new ClassReader(classFile);
	    ClassWriter writer = new ClassWriter(reader, 0);
	    
        reader.accept(factory.createTransformer(file, classFile, writer), 0);
        return writer.toByteArray();
	}

}

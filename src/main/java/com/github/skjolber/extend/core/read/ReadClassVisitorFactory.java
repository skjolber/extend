package com.github.skjolber.extend.core.read;

import org.objectweb.asm.ClassVisitor;

@FunctionalInterface
public interface ReadClassVisitorFactory {

	ClassVisitor createReader(String file, byte[] classFile);
}

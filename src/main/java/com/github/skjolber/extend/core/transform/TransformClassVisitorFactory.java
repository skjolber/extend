package com.github.skjolber.extend.core.transform;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

@FunctionalInterface
public interface TransformClassVisitorFactory {

	ClassVisitor createTransformer(String file, byte[] classFile, ClassWriter writer);
}

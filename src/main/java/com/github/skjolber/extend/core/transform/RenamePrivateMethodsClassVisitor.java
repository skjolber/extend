package com.github.skjolber.extend.core.transform;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM7;

import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.skjolber.extend.core.AbstractAccessClassVisitor;
import com.github.skjolber.extend.core.read.VisitedMethod;

public class RenamePrivateMethodsClassVisitor extends AbstractAccessClassVisitor {
	
	protected static final String OBJECT = "java/lang/Object";
	
	private static Logger log = LoggerFactory.getLogger(RenamePrivateMethodsClassVisitor.class);
	
	private String superClassName;
	private String className;
	private String format;
	private List<VisitedMethod> methods;
	
	public RenamePrivateMethodsClassVisitor(ClassVisitor cv, List<VisitedMethod> methods, String format) {
		super(cv);
		this.methods = methods;

		this.format = format;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		this.className = name;
		this.superClassName = superName;
		
		if(hasFlag(access, ACC_FINAL)) {
			access = remove(access, ACC_FINAL);
		}
		if((!hasFlag(access, ACC_PROTECTED) && !hasFlag(access, ACC_PUBLIC))) {
			access = add(access, ACC_PUBLIC);
		}
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	public MethodVisitor visitMethod(
			int access,
			String name,
			String desc,
			String signature,
			String[] exceptions) {
		
		boolean isPrivate = hasFlag(access, ACC_PRIVATE);
		if(isPrivate || (!hasFlag(access, ACC_PROTECTED) && !hasFlag(access, ACC_PUBLIC))) {
			access = remove(access, ACC_PRIVATE);
			access = add(access, ACC_PUBLIC);
		}

		if(hasFlag(access, ACC_FINAL)) {
			access = remove(access, ACC_FINAL);
		}
		
		if(isPrivate && !OBJECT.equals(superClassName) && methods != null && !name.equals("<init>")) {
			String renamedMethodName = String.format(format, name);
			log.info("Rename " + className + " method " + name + " to " + renamedMethodName);
			name = renamedMethodName;
		}
		MethodVisitor visitMethod = super.visitMethod(access, name, desc, signature, exceptions);
		
		return new RenamePrivateMethodVisitor(ASM7, visitMethod, className, methods, format);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		if(hasFlag(access, ACC_PRIVATE) || (!hasFlag(access, ACC_PROTECTED) && !hasFlag(access, ACC_PUBLIC))) {
			access = remove(access, ACC_PRIVATE);
			access = add(access, ACC_PROTECTED);
		}

		if(hasFlag(access, ACC_FINAL) && !hasFlag(access, ACC_STATIC)) {
			access = remove(access, ACC_FINAL);
		}
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	protected String getRenamedPrefix() {
		int index = className.lastIndexOf('/');
		
		return className.substring(index + 1);
	}
}
package com.github.skjolber.extend.core;

import static org.objectweb.asm.Opcodes.ASM7;

import org.objectweb.asm.ClassVisitor;

public abstract class AbstractAccessClassVisitor extends ClassVisitor {
	
	public AbstractAccessClassVisitor(ClassVisitor cv) {
		super(ASM7, cv);
		this.cv = cv;
	}

	public AbstractAccessClassVisitor() {
		super(ASM7);
	}

	public AbstractAccessClassVisitor(int api) {
		super(api);
	}

	protected int remove(int code, int ... flags) {
		for(int flag : flags) {
			code &= ~flag;
		}
			
		return code;
	}
	
	protected int add(int code, int ... flags ) {
		for(int flag : flags) {
			code |= flag;
		}
			
		return code;
	}
	
	protected boolean hasFlag(int code, int flag) {
		return (code & flag) != 0;
	}
	
	protected boolean isConstructorMethod(String name) {
		return name.equals("<init>");
	}
	
}
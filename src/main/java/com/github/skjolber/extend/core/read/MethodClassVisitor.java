package com.github.skjolber.extend.core.read;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.MethodVisitor;

import com.github.skjolber.extend.core.AbstractAccessClassVisitor;

public class MethodClassVisitor extends AbstractAccessClassVisitor {

	public static final String OBJECT = "java/lang/Object";

	private List<VisitedMethod> methods = new ArrayList<>();
	private String superClassName;
	private String className;

	public MethodClassVisitor() {
		super();
	}

	public MethodClassVisitor(int api) {
		super(api);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		
		this.className = name;
		this.superClassName = superName;
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		methods.add(new VisitedMethod(access, name, descriptor, signature, exceptions));
		
		return super.visitMethod(access, name, descriptor, signature, exceptions);
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getSuperClassName() {
		return superClassName;
	}
	
	public List<VisitedMethod> getMethods() {
		return methods;
	}
	
	public List<VisitedMethod> getMethods(int acccess) {
		return methods.stream()                // convert list to stream
                .filter(line -> hasFlag(line.getAccess(), acccess))     // we dont like mkyong
                .collect(Collectors.toList());      
	}
	
}

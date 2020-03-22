package com.github.skjolber.extend.core.transform;

import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.skjolber.extend.core.read.VisitedMethod;

/**
 * 
 * Class for updating invokations of previously private methods. Technically, private methods are invokes using another instruction than protected / public methods.
 *
 */

public class RenamePrivateMethodVisitor extends MethodVisitor {

	private static Logger log = LoggerFactory.getLogger(RenamePrivateMethodVisitor.class);

	protected List<VisitedMethod> methods;
	
	protected String owner;
	protected String format;
	
	public RenamePrivateMethodVisitor(int api, MethodVisitor methodVisitor, String owner, List<VisitedMethod> methods, String format) {
		super(api, methodVisitor);
		
		this.owner = owner;
		this.methods = methods;
		
		this.format = format;
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		if(owner.equals(this.owner) && !isConstructorMethod(name)) {
			// method call within the class
			// check if one of the previously private methods
			for (VisitedMethod method : methods) {
				if(method.is(name, descriptor)) {
					if(method.isRename()) {
						String renamed = String.format(format, name);
						
						log.info("Update invokation of " + owner + "." + name + " to " + owner + "." + renamed);
						
						name = renamed;
					}
					
					if(opcode == Opcodes.INVOKESPECIAL) {
						// private methods are invoked with a special code, so this has to change.
						opcode = Opcodes.INVOKEVIRTUAL;
					}
					
					break;
				}
				
			}
		}
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
	}
	
	
	protected boolean isConstructorMethod(String name) {
		return name.equals("<init>");
	}	
}

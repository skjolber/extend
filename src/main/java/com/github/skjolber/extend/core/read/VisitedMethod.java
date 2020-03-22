package com.github.skjolber.extend.core.read;

import java.util.Arrays;

public class VisitedMethod {

	protected int access;
	protected String name;
	protected String descriptor;
	protected String signature;
	protected String[] exceptions;
	protected boolean rename = true;
	
	public VisitedMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
		super();
		this.access = access;
		this.name = name;
		this.descriptor = descriptor;
		this.signature = signature;
		this.exceptions = exceptions;
	}

	public int getAccess() {
		return access;
	}

	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public String getSignature() {
		return signature;
	}

	public String[] getExceptions() {
		return exceptions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + access;
		result = prime * result + ((descriptor == null) ? 0 : descriptor.hashCode());
		result = prime * result + Arrays.hashCode(exceptions);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VisitedMethod other = (VisitedMethod) obj;
		if (access != other.access)
			return false;
		if (descriptor == null) {
			if (other.descriptor != null)
				return false;
		} else if (!descriptor.equals(other.descriptor))
			return false;
		if (!Arrays.equals(exceptions, other.exceptions))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Method [access=" + access + ", name=" + name + ", descriptor=" + descriptor + ", signature=" + signature
				+ ", exceptions=" + Arrays.toString(exceptions) + "]";
	}

	public boolean is(String name, String descriptor) {
		return this.name.equals(name) && this.descriptor.equals(descriptor);
	}

	public boolean isRename() {
		return rename;
	}
	
	public void setRename(boolean rename) {
		this.rename = rename;
	}
	
}

package pt.iscte.pidesco.codegenerator.AST_Processing;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.TypeDeclaration;

public class CGClass {
	private TypeDeclaration classNode;
	private ArrayList<CGAttribute> atributes;
	private ArrayList<CGMethod> methods;
	
	public CGClass(TypeDeclaration classNode, ArrayList<CGAttribute> atributes, ArrayList<CGMethod> methods) {
		this.classNode=classNode;
		this.atributes=atributes;
		this.methods=methods;
	}

	public TypeDeclaration getClassNode() {
		return classNode;
	}
	
	public String getClassName() {
		return classNode.getName().toString();
	}

	public ArrayList<CGAttribute> getAtributes() {
		return atributes;
	}
	
	public CGAttribute getAtribute(String name) {
		for(CGAttribute result : atributes)
			if(result.getAtributeName().equals(name))
				return result;
		return null;
	}

	public ArrayList<CGMethod> getMethods() {
		return methods;
	}
	
	public CGMethod getMethod(String name) {
		for(CGMethod result : methods)
			if(result.getMethodName().equals(name))
				return result;
		return null;
	}

}
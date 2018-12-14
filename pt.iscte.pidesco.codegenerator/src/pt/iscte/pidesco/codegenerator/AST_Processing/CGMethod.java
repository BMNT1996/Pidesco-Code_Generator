package pt.iscte.pidesco.codegenerator.AST_Processing;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class CGMethod {
	private MethodDeclaration methodDeclaration;
	
	public CGMethod(MethodDeclaration methodNode) {
		this.methodDeclaration=methodNode;
	}
	
	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}
	
	public String getMethodName() {
		return methodDeclaration.getName().toString();
	}
	
	@SuppressWarnings("deprecation")
	public String getMethodReturnType() {
		return methodDeclaration.getReturnType().toString();
	}
	
	public boolean isStatic() {
		 return Modifier.isStatic(methodDeclaration.getModifiers());
	}
	
	public int getNumberOfParameters() {
		return methodDeclaration.parameters().size();
	}

}
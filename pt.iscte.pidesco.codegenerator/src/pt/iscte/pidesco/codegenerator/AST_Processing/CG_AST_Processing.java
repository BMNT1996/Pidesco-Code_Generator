package pt.iscte.pidesco.codegenerator.AST_Processing;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorActivator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class CG_AST_Processing {
	static TypeDeclaration classNode;
	static ArrayList<CGAttribute> atributes;
	static ArrayList<CGMethod> methods;
	
	public static CGClass GenerateCGClass() {		
		BundleContext context = CodeGeneratorActivator.getContext();
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServ = context.getService(serviceReference);
		
		return GenerateCGClass(javaEditorServ.getOpenedFile());
	}
	
	public static CGClass GenerateCGClass(File file) {
		atributes = new ArrayList<CGAttribute>();
		methods = new ArrayList<CGMethod>();
		
		BundleContext context = CodeGeneratorActivator.getContext();
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServ = context.getService(serviceReference);
		
		javaEditorServ.parseFile(file, new GetClassInformation() {
        });
		
		return new CGClass(classNode, atributes, methods);
	}

	private static class GetClassInformation extends ASTVisitor {
		private ASTNode rightParent;
		// visits class/interface declaration
		@Override
		public boolean visit(TypeDeclaration node) {
			classNode=node;
			return true;
		}

		// visits attributes
		@Override
		public boolean visit(FieldDeclaration node) {
			// loop for several variables in the same declaration
			for(Object o : node.fragments()) {
				VariableDeclarationFragment var = (VariableDeclarationFragment) o;
				atributes.add(new CGAttribute(node,var));
			}
			return false; // false to avoid child VariableDeclarationFragment to be processed again
		}
		
		@Override
		public boolean visit(MethodDeclaration node) {
			if(rightParent==null)
				rightParent=node.getParent();
			if(node.getParent().equals(rightParent)){
				methods.add(new CGMethod(node));
			}
			return true;
		}
		
		// visits variable declarations
		@Override
		public boolean visit(VariableDeclarationFragment node) {
			return true;
		}
	}
}

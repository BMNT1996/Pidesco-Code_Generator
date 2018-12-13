package pt.iscte.pidesco.codegenerator.AST_Processing;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CGAttribute {
	private VariableDeclarationFragment atributeDeclarationFragment;
	private FieldDeclaration atributeDeclaration;
	
	public CGAttribute(FieldDeclaration atributeDeclaration, VariableDeclarationFragment atributeNode) {
		this.atributeDeclarationFragment=atributeNode;
		this.atributeDeclaration=atributeDeclaration;
	}
	
	public VariableDeclarationFragment getAtributeDeclarationFragment() {
		return atributeDeclarationFragment;
	}
	
	public String getAtributeName() {
		return atributeDeclarationFragment.getName().toString();
	}
	
	public String getAtributeType() {
		if(atributeDeclarationFragment.getParent() instanceof FieldDeclaration){
            FieldDeclaration declaration = ((FieldDeclaration) atributeDeclarationFragment.getParent());
            if(declaration.getType().isSimpleType()){
                return declaration.getType().toString();
            }
        }
		
		return "";
	}
	
	public boolean isStatic() {
		 return Modifier.isStatic(atributeDeclaration.getModifiers());
	}
}

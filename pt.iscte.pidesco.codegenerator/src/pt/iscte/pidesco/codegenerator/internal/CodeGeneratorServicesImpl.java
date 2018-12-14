package pt.iscte.pidesco.codegenerator.internal;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import pt.iscte.pidesco.codegenerator.AST_Processing.CGAttribute;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGClass;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGMethod;
import pt.iscte.pidesco.codegenerator.AST_Processing.CG_AST_Processing;
import pt.iscte.pidesco.codegenerator.codes.SimpleCode;
import pt.iscte.pidesco.codegenerator.extensibility.CGCode;
import pt.iscte.pidesco.codegenerator.features.GettersAndSetters;
import pt.iscte.pidesco.codegenerator.features.ToString;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;

public class CodeGeneratorServicesImpl implements CodeGeneratorServices {

	@Override
	public CGClass getOpendedFileAsCGClass() {
		return CG_AST_Processing.GenerateCGClass();
	}

	@Override
	public CGClass getFileAsCGClass(File file) {
		return CG_AST_Processing.GenerateCGClass(file);
	}

	@Override
	public CGMethod[] getCGMethods() {
		CGClass openedCGClass = CG_AST_Processing.GenerateCGClass();
		ArrayList<CGMethod> openedCGMethods = openedCGClass.getMethods();
		CGMethod[] result = new CGMethod[openedCGMethods.size()];
		for (int i = 0; i < openedCGMethods.size(); i++) {
			result[i] = openedCGMethods.get(i);
		}
		return result;
	}

	@Override
	public String[] getMethodsNames() {
		CGClass openedCGClass = CG_AST_Processing.GenerateCGClass();
		ArrayList<CGMethod> openedCGMethods = openedCGClass.getMethods();
		String[] result = new String[openedCGMethods.size()];
		for (int i = 0; i < openedCGMethods.size(); i++) {
			result[i] = openedCGMethods.get(i).getMethodName();
		}
		return result;
	}

	@Override
	public CGAttribute[] getCGAtributes() {
		CGClass openedCGClass = CG_AST_Processing.GenerateCGClass();
		ArrayList<CGAttribute> openedCGAtributes = openedCGClass.getAtributes();
		CGAttribute[] result = new CGAttribute[openedCGAtributes.size()];
		for (int i = 0; i < openedCGAtributes.size(); i++) {
			result[i] = openedCGAtributes.get(i);
		}
		return result;
	}

	@Override
	public String[] getAtributesNames() {
		CGClass openedCGClass = CG_AST_Processing.GenerateCGClass();
		ArrayList<CGAttribute> openedCGAtributes = openedCGClass.getAtributes();
		String[] result = new String[openedCGAtributes.size()];
		for (int i = 0; i < openedCGAtributes.size(); i++) {
			result[i] = openedCGAtributes.get(i).getAtributeName();
		}
		return result;
	}
	
	@Override
	public void addCode(CGCode code) {
		CodeGeneratorView.getInstance().addCode(code);
	}

	@Override
	public void generateGettersAndSettersFromArrayList(ArrayList<String> Getters, ArrayList<String> Setters) {
		GettersAndSetters.generateGettersAndSettersFromArrayList(Getters, Setters);
	}

	@Override
	public void generateGettersAndSettersFromArrayList(File file, ArrayList<String> Getters,
			ArrayList<String> Setters) {
		GettersAndSetters.generateGettersAndSettersFromArrayList(file, Getters, Setters);
	}

	@Override
	public void generateGettersAndSettersFromArrayList(ArrayList<CGAttribute> atributes, ArrayList<String> Getters,
			ArrayList<String> Setters) {
		GettersAndSetters.generateGettersAndSettersFromArrayList(atributes, Getters, Setters);
	}

	@Override
	public void generateToStringFromArrayList(ArrayList<String> Attributes, ArrayList<String> Methods) {
		ToString.generateToStringFromArrayList(Attributes, Methods);
	}

	@Override
	public void generateToStringFromArrayList(File file, ArrayList<String> Attributes, ArrayList<String> Methods) {
		ToString.generateToStringFromArrayList(file, Attributes, Methods);
	}

	@Override
	public void generateToStringFromArrayList(CGClass Class, ArrayList<String> Attributes, ArrayList<String> Methods) {
		ToString.generateToStringFromArrayList(Class, Attributes, Methods);
	}

	@Override
	public Display getDisplay() {
		return CodeGeneratorView.getInstance().getDisplay();
	}

	@Override
	public ArrayList<SimpleCode> getSimpleCodes() {
		return CodeGeneratorView.getInstance().getSimpleCodes();
	}

}

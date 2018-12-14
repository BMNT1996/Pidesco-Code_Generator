package pt.iscte.pidesco.codegenerator.features;

import java.io.File;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.AST_Processing.CGAttribute;
import pt.iscte.pidesco.codegenerator.AST_Processing.CG_AST_Processing;
import pt.iscte.pidesco.codegenerator.codes.ComplexCode;
import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorActivator;
import pt.iscte.pidesco.codegenerator.internal.WindowGenerator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class GettersAndSetters extends ComplexCode {

	public GettersAndSetters() {
		super("Getters and Setters");
	}

	@Override
	public CodeType getCodeType() {
		return CodeType.COMPLEX;
	}

	@Override
	public String getCodeName() {
		return "Getters and Setters";
	}

	@Override
	public String resultCodeToWrite() {
		WindowGenerator.getInstance()
				.generateGettersAndSettersWindow(CG_AST_Processing.GenerateCGClass().getAtributes());
		return "";
	}

	public static void generateGettersAndSettersFromArrayList(ArrayList<String> Getters, ArrayList<String> Setters) {
		generateGettersAndSettersFromArrayList(CG_AST_Processing.GenerateCGClass().getAtributes(), Getters, Setters);
	}

	public static void generateGettersAndSettersFromArrayList(File file, ArrayList<String> Getters,
			ArrayList<String> Setters) {
		generateGettersAndSettersFromArrayList(CG_AST_Processing.GenerateCGClass(file).getAtributes(), Getters,
				Setters);
	}

	public static void generateGettersAndSettersFromArrayList(ArrayList<CGAttribute> atributes,
			ArrayList<String> Getters, ArrayList<String> Setters) {
		String output = "";
		for (CGAttribute atribute : atributes) {
			if (Getters.contains(atribute.getAtributeName())) {
				output += "	public " + atribute.getAtributeType() + " get" + atribute.getAtributeName()
						+ "(){\n		return " + atribute.getAtributeName() + ";\n	}\n\n";
			}
			if (Setters.contains(atribute.getAtributeName())) {
				output += "	public void set" + atribute.getAtributeName() + "(" + atribute.getAtributeType() + " "
						+ atribute.getAtributeName() + "){\n		this." + atribute.getAtributeName() + " = "
						+ atribute.getAtributeName() + ";\n	}\n\n";
			}
		}

		BundleContext context = CodeGeneratorActivator.getContext();
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServ = context.getService(serviceReference);
		javaEditorServ.insertTextAtCursor(output);
	}

}

package pt.iscte.pidesco.codegenerator.features;

import java.io.File;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.AST_Processing.CGClass;
import pt.iscte.pidesco.codegenerator.AST_Processing.CG_AST_Processing;
import pt.iscte.pidesco.codegenerator.codes.ComplexCode;
import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorActivator;
import pt.iscte.pidesco.codegenerator.internal.WindowGenerator;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ToString extends ComplexCode {

	public ToString() {
		super("ToString");
	}

	@Override
	public CodeType getCodeType() {
		return CodeType.COMPLEX;
	}

	@Override
	public String getCodeName() {
		return "ToString";
	}

	@Override
	public String resultCodeToWrite() {
		WindowGenerator.getInstance().generateToStringWindow(CG_AST_Processing.GenerateCGClass());
		return "";
	}

	public static void generateToStringFromArrayList(ArrayList<String> Attributes, ArrayList<String> Methods) {
		generateToStringFromArrayList(CG_AST_Processing.GenerateCGClass(), Attributes, Methods);
	}

	public static void generateToStringFromArrayList(File file, ArrayList<String> Attributes,
			ArrayList<String> Methods) {
		generateToStringFromArrayList(CG_AST_Processing.GenerateCGClass(file), Attributes, Methods);
	}

	public static void generateToStringFromArrayList(CGClass Class, ArrayList<String> Attributes,
			ArrayList<String> Methods) {
		String output = "	@Override" + System.lineSeparator() + "	public String toString() {" + System.lineSeparator()
				+ "		return \"" + Class.getClassName()
				+ "\" + System.lineSeparator() + \"\tAttributes:\" + System.lineSeparator() ";
		for (String word : Attributes)
			output += "+ \"\t\t" + word + "=\" + " + word + " + System.lineSeparator() ";
		output += "+ \"\tMetodos:\" + System.lineSeparator() ";
		for (String word : Methods)
			output += "+ \"\t\t" + word + "()=\" + " + word + "() + System.lineSeparator() ";
		output += ";" + System.lineSeparator() + "	}" + System.lineSeparator();

		BundleContext context = CodeGeneratorActivator.getContext();
		ServiceReference<JavaEditorServices> serviceReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices javaEditorServ = context.getService(serviceReference);
		javaEditorServ.insertTextAtCursor(output);
	}

}

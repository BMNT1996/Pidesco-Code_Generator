package pt.iscte.pidesco.codegenerator.service;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import pt.iscte.pidesco.codegenerator.AST_Processing.CGAttribute;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGClass;
import pt.iscte.pidesco.codegenerator.AST_Processing.CGMethod;
import pt.iscte.pidesco.codegenerator.codes.SimpleCode;
import pt.iscte.pidesco.codegenerator.extensibility.CGCode;

/**
 * Service containing the operations offered by the Code Generator view.
 * 
 * @author Bruno Teles
 */
public interface CodeGeneratorServices {

	/**
	 * Generate a CGClass data structure of the opened file
	 * 
	 * @return a CGClass of the opened file
	 */
	CGClass getOpendedFileAsCGClass();

	/**
	 * Generate a CGClass data structure of the selected file
	 * 
	 * @param file Java file to get the CGClass structure
	 * @return a CGClass of the selected file
	 */
	CGClass getFileAsCGClass(File file);

	/**
	 * Obtains the list of methods of the opened file
	 * 
	 * @return a list of CGMethods from the opened file
	 */
	CGMethod[] getCGMethods();

	/**
	 * Obtains the list of methods names of the opened file
	 * 
	 * @return a list of String with the methods names from the opened file
	 */
	String[] getMethodsNames();

	/**
	 * Obtains the list of attributes of the opened file
	 * 
	 * @return a list of CGAtributes from the opened file
	 */
	CGAttribute[] getCGAtributes();

	/**
	 * Obtains the list of attributes names of the opened file
	 * 
	 * @return a list of String with the attributes names from the opened file
	 */
	String[] getAtributesNames();

	/**
	 * Add a code to the program
	 * 
	 * @param code that should be add to the program
	 */
	void addCode(CGCode code);

	/**
	 * Generate the getter and setters of the open file
	 * 
	 * @param Getters List of attributes names that will be generated the getters
	 * @param Setters List of attributes names that will be generated the setters
	 */
	void generateGettersAndSettersFromArrayList(ArrayList<String> Getters, ArrayList<String> Setters);

	/**
	 * Generate the getter and setters of a file
	 * 
	 * @param file    Java source file where exists attributes to be generated its
	 *                getters and setters
	 * @param Getters List of attributes names that will be generated the getters
	 * @param Setters List of attributes names that will be generated the setters
	 */
	void generateGettersAndSettersFromArrayList(File file, ArrayList<String> Getters, ArrayList<String> Setters);

	/**
	 * Generate the getter and setters from a CGAtribute ArrayList
	 * 
	 * @param atributes List of attributes in CGAtributes format
	 * @param Getters   List of attributes names that will be generated the getters
	 * @param Setters   List of List of attributes names that will be generated the
	 *                  getters
	 */
	void generateGettersAndSettersFromArrayList(ArrayList<CGAttribute> atributes, ArrayList<String> Getters,
			ArrayList<String> Setters);

	/**
	 * Generate the toString for the opened file
	 * 
	 * @param Attributes List of attributes names that will be included in toString
	 * @param Methods    List of attributes names that will be included in toString
	 */
	void generateToStringFromArrayList(ArrayList<String> Attributes, ArrayList<String> Methods);

	/**
	 * Generate the toString for a file
	 * 
	 * @param file       Java source file where exists attributes to be generated
	 *                   its getters and setters
	 * @param Attributes List of attributes names that will be included in toString
	 * @param Methods    List of methods names that will be included in toString
	 */
	void generateToStringFromArrayList(File file, ArrayList<String> Attributes, ArrayList<String> Methods);

	/**
	 * Generate toString for a CGClass
	 * 
	 * @param Class      CGClass of the class that contains the information to
	 *                   generate the toString
	 * @param Attributes List of attributes names that will be included in toString
	 * @param Methods    List of methods names that will be included in toString
	 */
	void generateToStringFromArrayList(CGClass Class, ArrayList<String> Attributes, ArrayList<String> Methods);

	/**
	 * Obtains the display object used in pidesco
	 * @return the display of pidesco
	 */
	Display getDisplay();

	/**
	 * Obtains the simple codes loaded in the program
	 * @return a list of all Simple Codes
	 */
	ArrayList<SimpleCode> getSimpleCodes();
}

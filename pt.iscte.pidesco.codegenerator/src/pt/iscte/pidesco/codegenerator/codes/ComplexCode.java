package pt.iscte.pidesco.codegenerator.codes;

import pt.iscte.pidesco.codegenerator.extensibility.CGCode;

/**
 * Class of Complex Code. That code that have its own behaviour before write in the file
 * @author Bruno Teles
 */
public abstract class ComplexCode implements CGCode{
	
	/**
	 * 
	 */
	private String name;
	
	/**
	 * Constructor of the class Complex Code
	 * @param name of the code that will be displayed
	 */
	public ComplexCode(String name) {
		this.name=name;	
	}

	/**
	 * Obtain the type of the code
	 * @return the code type, in this case will always return "COMPLEX"
	 */
	@Override
	public CodeType getCodeType() {
		return CodeType.COMPLEX;
	}

	/**
	 * Returns the code that should be write at the cursor
	 * @return the output of the code after its behaviour
	 */
	@Override
	public String resultCodeToWrite() {
		return "";
	}
	
	/**
	 * Obtains the name of the code
	 * @return name of the code
	 */
	@Override
	public String getCodeName() {
		return name;
	}

}
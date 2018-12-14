package pt.iscte.pidesco.codegenerator.codes;

import pt.iscte.pidesco.codegenerator.extensibility.CGCode;
/**
 * Class of Simple Code. That code that is only required to write in the file
 * @author Bruno Teles
 */
public class SimpleCode implements CGCode {
	
	/**
	 * Name used to identify the code
	 * Output of the code that should be write in the file
	 */
	private String output, name;
	
	/**
	 * Constructor of the Simple Code
	 * @param name of the code
	 * @param output of the code
	 */
	public SimpleCode(String name, String output) {
		this.output=output;
		this.name=name;
	}

	/**
	 * Obtain the type of the code
	 * @return the code type, in this case will always return "SIMPLE"
	 */
	@Override
	public CodeType getCodeType() {
		return CodeType.SIMPLE;
	}
	
	/**
	 * Returns the code that should be write at the cursor
	 * @return output of the code
	 */
	@Override
	public String resultCodeToWrite() {
		return output;
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
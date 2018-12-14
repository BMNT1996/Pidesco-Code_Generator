package pt.iscte.pidesco.codegenerator.extensibility;

/**
 * @author Bruno Teles
 * Represents the code interface that can be add as plug-in in the code generator tab
 */
public interface CGCode {
	
	/**
	 * Enumerate to define the type of code presented
	 * SIMPLE to simple code, that code that is only required to write in the file
	 * COMPLEX to complex code, that code that have a special behaviour
	 * PLUGIN, all the plug-ins developed to this program
	 */
	enum CodeType {
	    SIMPLE,
	    COMPLEX,
	    PLUGIN
	}
	
	/**
	 * Obtain the type of the code
	 * @return the code type as CodeType enumerate (by default is considered as PLUGIN for a easier extensibility)
	 */
	default CodeType getCodeType() {
		return CodeType.PLUGIN;
	}
	
	/**
	 * The name of the code used to be showed in the list of the graphic interface
	 * @return (non-null) The name of the code, defined by the user
	 */
	String getCodeName();
	
	/**
	 * Returns the code that should be write at the cursor
	 */
	String resultCodeToWrite();
	
}

package pt.iscte.pidesco.codegenerator.internal;

import pt.iscte.pidesco.codegenerator.extensibility.CGCode;

public class SimpleCode implements CGCode {
	
	private String output, name;
	
	public SimpleCode(String name, String output) {
		this.output=output;
		this.name=name;
	}

	@Override
	public CodeType getCodeType() {
		return CodeType.SIMPLE;
	}

	@Override
	public String resultCodeToWrite() {
		return output;
	}

	@Override
	public String getCodeName() {
		return name;
	}

	public String getOutput() {
		return output;
	}
}

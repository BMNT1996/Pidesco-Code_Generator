package pt.iscte.pidesco.codegenerator.internal;

import pt.iscte.pidesco.codegenerator.extensibility.CGCode;

public abstract class ComplexCode implements CGCode{
	private String name;
	
	public ComplexCode(String name) {
		this.name=name;	
	}

	@Override
	public CodeType getCodeType() {
		return CodeType.COMPLEX;
	}

	@Override
	public String resultCodeToWrite() {
		return "";
	}

	
	@Override
	public String getCodeName() {
		return name;
	}
}

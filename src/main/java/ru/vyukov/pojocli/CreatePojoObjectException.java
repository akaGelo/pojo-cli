package ru.vyukov.pojocli;

public class CreatePojoObjectException extends ParseException {

	private static final long serialVersionUID = -6355102749593284694L;

	
	public CreatePojoObjectException(Class<?> pojoClass) {
		super("Fail on create object by class " + pojoClass + ". No default conctructor");
	}
}
package ru.vyukov.pojocli.demo;

import ru.vyukov.pojocli.annotations.CliParam;

public class PojoEnumField {

	@CliParam(name = "-enum")
	private ExampleEnum enumField;

	public ExampleEnum getEnumField() {
		return enumField;
	}

	public enum ExampleEnum {
		ONE, TWO;
	}
}

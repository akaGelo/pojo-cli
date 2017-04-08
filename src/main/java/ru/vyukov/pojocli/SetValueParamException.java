package ru.vyukov.pojocli;

import java.lang.reflect.Field;

import ru.vyukov.pojocli.annotations.CliParam;

public class SetValueParamException extends ParseException {

	private static final long serialVersionUID = 6011962370178848383L;

	public SetValueParamException(CliParam cliParam, Field field, Exception e) {
		super("Problen on set \"" + cliParam.name() + "\" to field " + field, e);
	}

}

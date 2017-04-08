package ru.vyukov.pojocli;

import ru.vyukov.pojocli.annotations.CliParam;

public class MissingParameterTypeException extends ParseException {

	private static final long serialVersionUID = 40804693389451L;


	public MissingParameterTypeException(CliParam cliParam) {
		super("Missing parameter type of\"" + cliParam.name() + "\"");
	}

}

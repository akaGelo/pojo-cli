package ru.vyukov.pojocli;

import ru.vyukov.pojocli.annotations.CliParam;

public class MissingRequaredParamException extends ParseException {

	private static final long serialVersionUID = -3826879973425300867L;

	public MissingRequaredParamException(CliParam cliParam) {
		super("Missing requared param \"" + cliParam.name() + "\"");
	}

}

package ru.vyukov.pojocli.demo;

import ru.vyukov.pojocli.annotations.CliParam;

public class WrongParamName {

	//WRONG
	@CliParam(name = "port")
	private int port;

	public int getPort() {
		return port;
	}
}

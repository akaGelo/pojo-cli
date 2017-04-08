package ru.vyukov.pojocli.demo;

import ru.vyukov.pojocli.annotations.CliParam;

public class PojoPrivateFields {

	@CliParam(name = "-port")
	private int port;

	public int getPort() {
		return port;
	}
}

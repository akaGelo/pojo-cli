package ru.vyukov.pojocli.demo;

import ru.vyukov.pojocli.annotations.CliParam;

public class PojoPrivateConstructor {

	@CliParam(name = "-port")
	private int port;

	public int getPort() {
		return port;
	}

	public PojoPrivateConstructor(int port) {
		this.port = port;
	}
	
	
}

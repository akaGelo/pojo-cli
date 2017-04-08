package ru.vyukov.pojocli.demo;

import ru.vyukov.pojocli.annotations.CliParam;

public class DbPojoConfig {

	@CliParam(name = "-port")
	private int port;

	@CliParam(name = "-u")
	private String username;

	@CliParam(name = "--password")
	private String password;

	@CliParam(name = "-db", requared = false)
	private String dbName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}

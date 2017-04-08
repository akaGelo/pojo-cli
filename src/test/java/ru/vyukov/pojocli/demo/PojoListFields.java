package ru.vyukov.pojocli.demo;

import java.util.List;

import ru.vyukov.pojocli.annotations.CliParam;

public class PojoListFields {

	@CliParam(name = "-list",propertyType=Integer.class)
	private List<Integer> list;

	public List<Integer> getList() {
		return list;
	}
}

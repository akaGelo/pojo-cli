package ru.vyukov.pojocli.demo;

import java.util.List;

import ru.vyukov.pojocli.annotations.CliParam;

public class PojoListFieldsMissingType {

	@CliParam(name = "-list")
	private List<Integer> list;

	public List<Integer> getList() {
		return list;
	}
}

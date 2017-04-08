package ru.vyukov.pojocli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import ru.vyukov.pojocli.demo.DbPojoConfig;
import ru.vyukov.pojocli.demo.PojoEnumField;
import ru.vyukov.pojocli.demo.PojoListFields;
import ru.vyukov.pojocli.demo.PojoListFieldsMissingType;
import ru.vyukov.pojocli.demo.PojoPrivateConstructor;
import ru.vyukov.pojocli.demo.PojoPrivateFields;
import ru.vyukov.pojocli.demo.WrongParamName;

public class CliParserTest {

	@Test
	public void testHasArguments() throws Exception {
		CliParser cliParser = new CliParser(new String[] { "-a", "a", "-b", "b" });
		assertTrue(cliParser.hasArgument("-a"));
		assertFalse(cliParser.hasArgument("-c"));
		assertFalse(cliParser.hasArgument("c"));
	}

	@Test
	public void testParseSkippedArguments() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-a", "a", "b", "b", "--c", "c" });
		assertTrue(cliParser.hasArgument("-a"));
		assertTrue(cliParser.hasArgument("--c"));
		assertFalse(cliParser.hasArgument("b"));
	}

	@Test(expected = MissingParameterTypeException.class)
	public void testParseListArgumentsMissingListType() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-list", "1,2,3", "b", "b", "--c", "c" });

		cliParser.parse(PojoListFieldsMissingType.class);
	}

	@Test
	public void testParseListArguments() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-list", "1,2, 3", "b", "b", "--c", "c" });

		PojoListFields pojoListFields = cliParser.parse(PojoListFields.class);
		List<Integer> list = pojoListFields.getList();
		assertEquals(1, list.get(0).intValue());
		assertEquals(2, list.get(1).intValue());
		assertEquals(3, list.get(2).intValue());
	}

	@Test
	public void testParseEnum() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-enum", "ONE", "b", "b", "--c", "c" });

		PojoEnumField enumFields = cliParser.parse(PojoEnumField.class);
		assertEquals(PojoEnumField.ExampleEnum.ONE, enumFields.getEnumField());
	}

	@Test(expected = SetValueParamException.class)
	public void testParseEnumException() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-enum", "ANOTHER", "b", "b", "--c", "c" });

		cliParser.parse(PojoEnumField.class);
	}

	@Test
	public void testParseSkippedArgumentsEqualsFormat() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-a=a", "b", "b", "--c", "c" });
		assertTrue(cliParser.hasArgument("-a"));
		assertTrue(cliParser.hasArgument("--c"));
		assertFalse(cliParser.hasArgument("b"));
	}

	@Test
	public void testParseSimpleObject() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-u", "user", "--password", "qwerty", "-port", "5432" });
		DbPojoConfig parse = cliParser.parse(DbPojoConfig.class);

		assertEquals(parse.getUsername(), "user");
		assertEquals(parse.getPassword(), "qwerty");
		assertEquals(parse.getPort(), 5432);

		//
		assertNull(parse.getDbName());
	}

	@Test(expected = MissingRequaredParamException.class)
	public void testParseMissinRequared() throws Exception {
		CliParser cliParser = new CliParser(
				new String[] { "-u", "user", "--password", "qwerty", "-db", "example" });
		cliParser.parse(DbPojoConfig.class);
	}

	@Test
	public void testParsePrivateFields() throws Exception {
		CliParser cliParser = new CliParser(new String[] { "-port", "5432" });
		PojoPrivateFields parse = cliParser.parse(PojoPrivateFields.class);
		assertEquals(parse.getPort(), 5432);
	}

	@Test(expected = CreatePojoObjectException.class)
	public void testNoDefaultConstructor() throws Exception {
		CliParser cliParser = new CliParser(new String[] {});
		cliParser.parse(PojoPrivateConstructor.class);
	}

	@Test(expected = ParamNameFormatException.class)
	public void testWrongParamName() throws Exception {
		CliParser cliParser = new CliParser(new String[] {});
		cliParser.parse(WrongParamName.class);
	}

}

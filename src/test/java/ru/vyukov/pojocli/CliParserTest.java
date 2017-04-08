package ru.vyukov.pojocli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ru.vyukov.pojocli.demo.DbPojoConfig;
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

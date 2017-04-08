package ru.vyukov.pojocli;

import static org.apache.commons.beanutils.ConvertUtils.convert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import ru.vyukov.pojocli.annotations.CliParam;

/**
 * Command Line Arguments parser. Parse arguments to POJO
 * @author gelo
 *
 * @param <T>
 */
public class CliParser {

	private String[] args;

	private Map<String, String> argsMap;

	public CliParser(String[] args) {
		this.args = args;
	}

	private Map<String, String> getArgsMap() {
		if (null == argsMap) {
			Map<String, String> localArgsMap = new HashMap<>();
			for (int i = 0; i < args.length - 1; i++) {
				String key = args[i];
				if (!key.startsWith("-")) {
					i++;
					// skip arg if not started with '-'
					continue;
				}
				String val = args[++i];
				localArgsMap.put(key, val);
			}
			argsMap = localArgsMap;
		}
		return argsMap;
	}

	/**
	 * Parse CLI arguments to object fields
	 * @param pojoClass
	 * @return
	 * @throws ParseException
	 */
	public <T> T parse(Class<T> pojoClass) throws ParseException {
		T pojoObject = createPojoObject(pojoClass);

		List<Field> propertyDescriptors = findCliParamsPropertiesDescriptors(pojoObject);

		Map<String, String> localArgsMap = getArgsMap();
		for (Field field : propertyDescriptors) {

			CliParam cliParam = field.getAnnotation(CliParam.class);
			String key = cliParam.name();
			checkKeyName(key);
			String val = localArgsMap.get(key);

			if (null == val && cliParam.requared()) {
				throw new MissingRequaredParamException(cliParam);
			}
			else {
				try {
					Class<?> propertyType = field.getType();
					Object castedVal = convert(val, propertyType);
					FieldUtils.writeField(field, pojoObject, castedVal, true);
				}
				catch (IllegalAccessException e) {
					throw new SetValueParamException(cliParam, field, e);
				}
			}

		}

		return pojoObject;

	}

	private void checkKeyName(String key) throws ParamNameFormatException {
		if (!key.startsWith("-")) {
			throw new ParamNameFormatException("Param name must be start on '-'");
		}

	}

	private <T> List<Field> findCliParamsPropertiesDescriptors(T pojoObject) {

		return FieldUtils.getFieldsListWithAnnotation(pojoObject.getClass(),
				CliParam.class);
	}

	private static <T> T createPojoObject(Class<T> pojoClass)
			throws CreatePojoObjectException {
		T pojoObject = null;
		try {
			pojoObject = pojoClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			throw new CreatePojoObjectException(pojoClass);
		}
		return pojoObject;
	}

	/**
	 * Check argument exist
	 * @param argumentName "-user"
	 * @return
	 * @return true if argument exist
	 */
	public boolean hasArgument(String argumentName) {
		return getArgsMap().containsKey(argumentName);
	}

}

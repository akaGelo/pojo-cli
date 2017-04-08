package ru.vyukov.pojocli;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.beanutils.ConvertUtils.convert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

import ru.vyukov.pojocli.annotations.CliParam;
import ru.vyukov.pojocli.annotations.DefaultPropertyType;

/**
 * Command Line Arguments parser. Parse arguments to POJO. <br/>
 * 
 * Support formats:
 * <ul>
 * <li>-key1 val1 -key2 val2</li>
 * <li>-key1=val1 -key2=val2</li>
 * <li>-key1=val1,val2,val3 -key2=val7</li>
 * <li>-key1=val1, val2, val3 -key2=val7,val8</li>
 * </ul>
 * @author gelo
 *
 * @param <T>
 */
public class CliParser {

	private static final String LIST_DELIMITING = ",";

	private List<String> args;

	private Map<String, String> argsMap;

	public CliParser(String[] cliArgs) {
		// '-a=b' format to '-a b' format
		this.args = asList(cliArgs).stream().flatMap(e -> stream(e.split("=")))
				.collect(toList());

	}

	private Map<String, String> getArgsMap() {
		if (null == argsMap) {
			Map<String, String> localArgsMap = new HashMap<>();
			for (int i = 0; i < args.size() - 1; i++) {
				String key = args.get(i);
				if (!key.startsWith("-")) {
					i++;
					// skip arg if not started with '-'
					continue;
				}
				String val = args.get(++i);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
					Object castedVal = null;
					if (propertyType.isAssignableFrom(List.class)) {
						castedVal = convertToList(val, cliParam);
					}
					else if (propertyType.isEnum()) {
						castedVal = Enum.valueOf((Class<Enum>) propertyType, val);
					}
					else {
						castedVal = convert(val, propertyType);
					}

					FieldUtils.writeField(field, pojoObject, castedVal, true);
				}
				catch (IllegalAccessException | IllegalArgumentException e) {
					throw new SetValueParamException(cliParam, field, e);
				}
			}

		}

		return pojoObject;

	}

	private Object convertToList(String val, CliParam cliParam)
			throws MissingParameterTypeException {
		Class<?> propertyType = cliParam.propertyType();
		if (DefaultPropertyType.class.equals(propertyType)) {
			throw new MissingParameterTypeException(cliParam);
		}

		String[] splitedValue = val.split(LIST_DELIMITING);
		return Arrays.stream(splitedValue).map(v -> convert(v.trim(), propertyType))
				.collect(Collectors.toList());

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

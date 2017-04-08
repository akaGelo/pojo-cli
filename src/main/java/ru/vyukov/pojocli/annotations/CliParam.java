package ru.vyukov.pojocli.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface CliParam {

	/**
	 * CLI argument name. <br/>
	 * Examples:
	 * <ul>
	 * <li>-user</li>
	 * <li>--password</li>
	 * <ul>
	 * @return
	 */
	String name();

	/**
	 * 
	 * @return
	 */
	boolean requared() default true;

	/**
	 * Help message
	 * @return
	 */
	String help() default "no help message";

	/**
	 * Generic type for Collections params
	 * @return
	 */
	Class<?> propertyType() default DefaultPropertyType.class;

}

package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Description;

import java.lang.reflect.Field;

public class CliOption {

	protected String longopt;
	protected String shortopt;

	protected boolean required;

	private Field sourceField;

	protected CliOption(Field sourceField) {
		this.sourceField = sourceField;
	}

	public String getLongopt() {
		return longopt;
	}

	public String getShortopt() {
		return shortopt;
	}

	public String getDescription() {
		return sourceField.isAnnotationPresent(Description.class) ? sourceField.getAnnotation(Description.class).value() : "";
	}

	public boolean isRequired() {
		return required;
	}

	public Field getSourceField() {
		return sourceField;
	}
}

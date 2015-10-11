package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Option;

import java.lang.reflect.Field;

public class CliOption {

	protected String longopt;

	private Field sourceField;

	protected CliOption(Field sourceField) {
		this.sourceField = sourceField;
	}

	public String getLongopt() {
		return longopt;
	}

	public String getDescription() {
		return sourceField.getAnnotation(Option.class).description();
	}

	public boolean isRequired() {
		return sourceField.getAnnotation(Option.class).required();
	}

	public Field getSourceField() {
		return sourceField;
	}
}

package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Argument;

import java.lang.reflect.Field;

public class CliArgument {

	private Field sourceField;

	protected CliArgument(Field sourceField) {
		this.sourceField = sourceField;
	}

	public String getDescription() {
		return sourceField.getAnnotation(Argument.class).description();
	}

	public boolean isRequired() {
		return sourceField.getAnnotation(Argument.class).required();
	}

	public Field getSourceField() {
		return sourceField;
	}
}

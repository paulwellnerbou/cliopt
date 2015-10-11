package de.wellnerbou.cliopt;

import java.lang.reflect.Field;

public class CliOptionBuilder {
	private Field field;
	private boolean required;

	public CliOptionBuilder withField(final Field field) {
		this.field = field;
		return this;
	}

	public CliOption build() {
		final CliOption cliOption = new CliOption(field);
		cliOption.required = required;
		cliOption.longopt = parseNameToLongOpt(field.getName());
		return cliOption;
	}

	private String parseNameToLongOpt(final String name) {
		return name.replaceAll("(.)(\\p{Lu})", "$1-$2").toLowerCase();
	}

	public CliOptionBuilder isRequired(final boolean required) {
		this.required = required;
		return this;
	}
}

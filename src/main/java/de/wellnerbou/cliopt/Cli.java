package de.wellnerbou.cliopt;

import java.io.PrintStream;
import java.util.List;

public class Cli<T> {

	private List<CliOption> options;

	private T optionObject;

	public Cli(final List<CliOption> cliOptions, T optionObject) {
		this.options = cliOptions;
		this.optionObject = optionObject;
	}

	public List<CliOption> getOptions() {
		return options;
	}

	public T getOptionObject() {
		return optionObject;
	}

	public T parse(final String[] strings) {
		try {
			for (int i = 0; i < strings.length; i++) {
				String string = strings[i];
				for (CliOption option : options) {
					if (string.startsWith("--" + option.longopt + "=")) {
						option.getSourceField().set(optionObject, string.substring(string.indexOf('=') + 1));
					} else if (string.equals("--" + option.longopt) && !strings[i + 1].startsWith("-")) {
						option.getSourceField().set(optionObject, strings[++i]);
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		validate();
		return optionObject;
	}

	private void validate() {
		try {
			for (CliOption option : options) {
				if (option.isRequired() && (option.getSourceField().get(optionObject) == null || ((String) option.getSourceField().get(optionObject)).length() == 0)) {
					throw new MissingReqiredOptionException("Required option '" + option.getLongopt() + "' is missing.");
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void printUsage(PrintStream out) {
		new CliUsagePrinter(out).printUsage(this);
	}
}

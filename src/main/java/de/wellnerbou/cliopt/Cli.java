package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.AllowUnknownOptions;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Cli<T> {

	private List<CliOption> options;
	private List<CliArgument> cliArguments;
	private T optionObject;

	public Cli(final List<CliOption> cliOptions, final List<CliArgument> cliArguments, T optionObject) {
		this.options = cliOptions;
		this.cliArguments = cliArguments;
		this.optionObject = optionObject;
	}

	public List<CliOption> getOptions() {
		return options;
	}

	public T getOptionObject() {
		return optionObject;
	}

	public T parse(final String[] strings) {
		final Deque<String> args = new ArrayDeque<>();
		args.addAll(Arrays.asList(strings));

		while (!args.isEmpty()) {
			final String arg = args.poll();
			if (isOption(arg)) {
				parseOption(arg, args);
			} else {
				parseArgument(arg, args);
			}
		}

		validate();
		return optionObject;
	}

	private void parseArgument(final String arg, final Deque<String> args) {
		try {
			for (CliArgument argument : this.cliArguments) {
				if(argument.getSourceField().get(optionObject) == null) {
					argument.getSourceField().set(optionObject, arg);
					return;
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void parseOption(final String arg, final Deque<String> args) {
		try {
			for (CliOption option : options) {
				if (arg.startsWith("--" + option.longopt + "=")) {
					option.getSourceField().set(optionObject, arg.substring(arg.indexOf('=') + 1));
					return;
				} else if (arg.equals("--" + option.longopt)) {
					if (!isOption(args.peek())) {
						option.getSourceField().set(optionObject, args.poll());
					}
					return;
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		if (!optionObject.getClass().isAnnotationPresent(AllowUnknownOptions.class)) {
			throw new UnknownOptionException("Unknown option: " + arg);
		}
	}

	private boolean isOption(final String peek) {
		return peek.startsWith("--");
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

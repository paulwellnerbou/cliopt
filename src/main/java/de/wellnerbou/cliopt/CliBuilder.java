package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Argument;
import de.wellnerbou.cliopt.annotations.Option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CliBuilder<T> {

	private T opt;

	public CliBuilder(final T opt) {
		this.opt = opt;
	}

	public CliBuilder(final Class<T> optClass) {
		try {
			this.opt = optClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Cli<T> build() {
		List<CliOption> cliOptions = new ArrayList<>();
		List<CliArgument> cliArguments = new ArrayList<>();
		for (Field field : opt.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Option.class)) {
				final CliOption cliOption = createCliOption(field);
				cliOptions.add(cliOption);
			}
			if (field.isAnnotationPresent(Argument.class)) {
				final CliArgument cliArgument = createCliArgument(field);
				cliArguments.add(cliArgument);
			}
		}
		return new Cli<T>(cliOptions, cliArguments, opt);
	}

	private CliArgument createCliArgument(final Field field) {
		return new CliArgument(field);
	}

	private CliOption createCliOption(final Field field) {
		return new CliOptionBuilder()
				.withField(field)
				.build();
	}
}

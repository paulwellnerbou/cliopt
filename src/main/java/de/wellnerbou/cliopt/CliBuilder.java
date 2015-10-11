package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Description;
import de.wellnerbou.cliopt.annotations.Option;
import de.wellnerbou.cliopt.annotations.Required;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CliBuilder<T> {

	private T opt;

	public CliBuilder(final T opt) {
		this.opt = opt;
	}

	public Cli<T> build() {
		List<CliOption> cliOptions = new ArrayList<CliOption>();
		for (Field field : opt.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Option.class)) {
				final CliOption build = createCliOption(field);
				cliOptions.add(build);
			}
		}
		return new Cli<T>(cliOptions, opt);
	}

	private CliOption createCliOption(final Field field) {
		final CliOption cliOption = new CliOptionBuilder()
				.withField(field)
				.isRequired(field.isAnnotationPresent(Required.class))
				.build();
		return cliOption;
	}
}

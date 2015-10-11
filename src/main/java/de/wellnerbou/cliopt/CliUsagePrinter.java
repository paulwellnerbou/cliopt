package de.wellnerbou.cliopt;

import de.wellnerbou.cliopt.annotations.Usage;

import java.io.PrintStream;
import java.util.List;

public class CliUsagePrinter {

	private PrintStream out;

	public CliUsagePrinter(PrintStream out) {
		this.out = out;
	}

	public void printUsage(Cli<?> cli) {
		final Usage usageAnnotation = cli.getOptionObject().getClass().getAnnotation(Usage.class);
		if(usageAnnotation != null) {
			out.println(usageAnnotation.value());
			out.println();
		}

		for (CliOption cliOption : cli.getOptions()) {
			out.print("--" + cliOption.getLongopt() + "\t\t" + cliOption.getDescription());
			if(cliOption.isRequired()) {
				out.print(" (Required)");
			}
			out.println();
		}
	}
}

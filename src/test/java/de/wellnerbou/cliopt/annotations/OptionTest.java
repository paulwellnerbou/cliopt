package de.wellnerbou.cliopt.annotations;

import de.wellnerbou.cliopt.Cli;
import de.wellnerbou.cliopt.CliBuilder;
import de.wellnerbou.cliopt.CliOption;
import de.wellnerbou.cliopt.MissingReqiredOptionException;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest {

	@Test
	public void testAnnotationParsing() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli cli = new CliBuilder<>(opt).build();
		List<CliOption> options = cli.getOptions();
		assertThat(options).hasSize(2);
		assertThat(options.get(0).getLongopt()).isEqualTo("option");
		assertThat(options.get(1).getLongopt()).isEqualTo("my-option");
	}

	@Test
	public void testCorrectAutomaticOptionNaming() {
		TestOptWithoutRequiredOpts opt = new TestOptWithoutRequiredOpts();
		Cli<TestOptWithoutRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithoutRequiredOpts testOpt = cli.parse(new String[]{"--my-option=Value"});
		assertThat(testOpt.myOption).isEqualTo("Value");
	}

	@Test(expected = MissingReqiredOptionException.class)
	public void testMissingRequiredOption() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithRequiredOpts testOpt = cli.parse(new String[]{"--option", "--my-option=Value"});
	}

	@Test
	public void testPrintUsage() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		cli.printUsage(System.out);
	}

	@Usage("Usage: cmd [OPTS]")
	public class TestOptWithoutRequiredOpts {

		@Option
		@Description("First option")
		public String option;

		@Option
		public String myOption;
	}

	@Usage("Usage: cmd [OPTS]")
	public class TestOptWithRequiredOpts {

		@Required
		@Option
		@Description("First option")
		public String option;

		@Option
		public String myOption;
	}
}

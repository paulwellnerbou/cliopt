package de.wellnerbou.cliopt.annotations;

import de.wellnerbou.cliopt.Cli;
import de.wellnerbou.cliopt.CliBuilder;
import de.wellnerbou.cliopt.CliOption;
import de.wellnerbou.cliopt.MissingReqiredOptionException;
import de.wellnerbou.cliopt.UnknownOptionException;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest {

	@Test
	public void testAnnotationParsing() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		List<CliOption> options = cli.getOptions();
		assertThat(options).hasSize(2);
		assertThat(options.get(0).getLongopt()).isEqualTo("option");
		assertThat(options.get(1).getLongopt()).isEqualTo("my-option");
	}

	@Test
	public void testCorrectAutomaticOptionNaming() {
		TestOptWithoutRequiredOpts opt = new TestOptWithoutRequiredOpts();
		Cli<TestOptWithoutRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithoutRequiredOpts testOpt = cli.parse(new String[] { "--my-option=Value" });
		assertThat(testOpt.myOption).isEqualTo("Value");
	}

	@Test(expected = MissingReqiredOptionException.class)
	public void testMissingRequiredOption() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithRequiredOpts testOpt = cli.parse(new String[] { "--option", "--my-option=Value" });
	}

	@Test
	public void testLongOptWithSpaceInsteadOfEqual() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithRequiredOpts testOpt = cli.parse(new String[] { "--option", "Value", "--my-option", "Another Value" });
		assertThat(testOpt.option).isEqualTo("Value");
		assertThat(testOpt.myOption).isEqualTo("Another Value");
	}

	@Test
	public void testLongOptWithSpaceInsteadOfEqualMoreValues() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithRequiredOpts testOpt = cli.parse(new String[] { "--option", "Value", "--my-option", "Another", "Value" });
		assertThat(testOpt.option).isEqualTo("Value");
		assertThat(testOpt.myOption).isEqualTo("Another");
	}

	@Test(expected = UnknownOptionException.class)
	public void testDontAllowUnknownOption() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithRequiredOpts testOpt = cli.parse(new String[] { "--option", "Value", "--my-unknown-option", "Another Value" });
	}

	@Test
	public void testDontAllowArguments() {
		TestOptWithoutRequiredOpts opt = new TestOptWithoutRequiredOpts();
		Cli<TestOptWithoutRequiredOpts> cli = new CliBuilder<>(opt).build();
		TestOptWithoutRequiredOpts testOpt = cli.parse(new String[] { "--option=Value", "Argument", "AnotherArgument" });
		assertThat(testOpt.option).isEqualTo("Value");
	}

	@Test
	public void testAllowUnknownOption() {
		TestOptAllowUnknown opt = new TestOptAllowUnknown();
		Cli<TestOptAllowUnknown> cli = new CliBuilder<>(opt).build();
		TestOptAllowUnknown testOpt = cli.parse(new String[] { "--option", "Value", "--my-unknown-option", "Another Value" });
	}

	@Test
	public void testPrintUsage() {
		TestOptWithRequiredOpts opt = new TestOptWithRequiredOpts();
		Cli<TestOptWithRequiredOpts> cli = new CliBuilder<>(opt).build();
		cli.printUsage(System.out);
	}

	@Test
	public void testArguments() {
		Cli<TestOptWithArguments> cli = new CliBuilder<>(new TestOptWithArguments()).build();
		TestOptWithArguments testOpt = cli.parse(new String[]{"--option=value", "One", "Two"});
		assertThat(testOpt.option).isEqualTo("value");
		assertThat(testOpt.firstArgument).isEqualTo("One");
		assertThat(testOpt.secondArgumentButRequired).isEqualTo("Two");
	}

	@Usage("Usage: cmd [OPTS]")
	public class TestOptWithoutRequiredOpts {

		@Option(description = "First option")
		public String option;

		@Option
		public String myOption;
	}

	@Usage("Usage: cmd [OPTS]")
	public class TestOptWithRequiredOpts {

		@Option(description = "First option", required = true)
		public String option;

		@Option
		public String myOption;
	}

	@Usage("Usage: cmd [OPTS]")
	@AllowUnknownOptions
	public class TestOptAllowUnknown {
		@Option(description = "First option", required = true)
		public String option;
	}

	@Usage("Usage: cmd [OPTS]")
	public class TestOptWithArguments {
		@Option(description = "First option", required = true)
		public String option;

		@Argument
		public String firstArgument;

		@Argument(required = true)
		public String secondArgumentButRequired;
	}
}

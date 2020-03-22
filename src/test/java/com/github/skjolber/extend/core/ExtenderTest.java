package com.github.skjolber.extend.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ExtenderTest {

	protected static Path inputDirectory = Paths.get("src","test","resources");
	protected static Path outputDirectory = Paths.get("target");
	
	@BeforeAll
	public static void createOutputDirectory() throws IOException {
		if(!Files.exists(outputDirectory)) {
			Files.createDirectory(outputDirectory);
		}
	}
	
	@Test
	public void testExtender() {
		
		Path input = inputDirectory.resolve("classes.jar");

		Path output = outputDirectory.resolve("classes.jar");

		Extender extender = new Extender(input, output);

		extender.run();
	}
}

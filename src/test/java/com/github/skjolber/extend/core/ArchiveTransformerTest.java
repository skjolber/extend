package com.github.skjolber.extend.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.skjolber.extend.core.transform.ArchiveTransformer;
import com.github.skjolber.extend.core.transform.DefaultArchiveFileTransformer;
import com.github.skjolber.extend.core.transform.RenamePrivateMethodsClassVisitor;

public class ArchiveTransformerTest {

	protected static Path inputDirectory = Paths.get("src","test","resources");
	protected static Path outputDirectory = Paths.get("target");
	
	@BeforeAll
	public static void createOutputDirectory() throws IOException {
		if(!Files.exists(outputDirectory)) {
			Files.createDirectory(outputDirectory);
		}
	}
	
	@Test
	public void testByteTransformer() throws IOException {
		ArchiveTransformer transformer = new ArchiveTransformer();
		
		Path input = inputDirectory.resolve("classes.jar");
		
		Path output = outputDirectory.resolve("classes.ext.jar");
		
		transformer.transform(input, output, (fileName, bytes) -> bytes);
	}
	
	@Test
	public void testClassVisitorTransformer() throws IOException {
		ArchiveTransformer transformer = new ArchiveTransformer();
		
		Path input = inputDirectory.resolve("classes.jar");
		
		Path output = outputDirectory.resolve("classes.ext.jar");
		
		DefaultArchiveFileTransformer listener = new DefaultArchiveFileTransformer( (file, classFile, writer) -> new RenamePrivateMethodsClassVisitor(writer, Collections.emptyList(), "%s"));
		
		transformer.transform(input, output, listener);
	}
	
}

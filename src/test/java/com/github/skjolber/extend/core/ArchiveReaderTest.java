package com.github.skjolber.extend.core;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.skjolber.extend.core.read.ArchiveReader;
import com.github.skjolber.extend.core.read.DefaultArchiveFileReader;
import com.github.skjolber.extend.core.read.MethodClassVisitor;

public class ArchiveReaderTest {

	protected static Path inputDirectory = Paths.get("src","test","resources");
	
	@Test
	public void testReader() throws IOException {
		ArchiveReader reader = new ArchiveReader();
		
		Path input = inputDirectory.resolve("classes.jar");
		
		reader.read(input, (fileName, bytes) -> {
			System.out.println(fileName);
		});
	}
	
	@Test
	public void testPrivateMethodReader() throws IOException {
		ArchiveReader reader = new ArchiveReader();
		
		Path input = inputDirectory.resolve("classes.jar");
		
		List<MethodClassVisitor> classes = new ArrayList<>();
		
		DefaultArchiveFileReader defaultArchiveFileReader = new DefaultArchiveFileReader( (file, classFile) -> {
			MethodClassVisitor visitor = new MethodClassVisitor();
			
			classes.add(visitor);
			
			return visitor;
		});
		reader.read(input, defaultArchiveFileReader);
		
		assertThat(classes).isNotEmpty();
	}
	
}

package com.github.skjolber.extend.core;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.skjolber.extend.core.read.ArchiveReader;
import com.github.skjolber.extend.core.read.DefaultArchiveFileReader;
import com.github.skjolber.extend.core.read.MethodClassVisitor;
import com.github.skjolber.extend.core.read.VisitedMethod;
import com.github.skjolber.extend.core.transform.ArchiveTransformer;
import com.github.skjolber.extend.core.transform.DefaultArchiveFileTransformer;
import com.github.skjolber.extend.core.transform.RenamePrivateMethodsClassVisitor;

public class Extender implements Runnable {

	private static Logger log = LoggerFactory.getLogger(RenamePrivateMethodsClassVisitor.class);

	protected Path input;
	protected Path output;

	public Extender(Path input, Path output) {
		super();
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {
		try {
			ArchiveReader reader = new ArchiveReader();
			
			List<MethodClassVisitor> classes = new ArrayList<>();
			
			DefaultArchiveFileReader defaultArchiveFileReader = new DefaultArchiveFileReader( (file, classFile) -> {
				MethodClassVisitor visitor = new MethodClassVisitor();
				
				classes.add(visitor);
				
				return visitor;
			});
			reader.read(input, defaultArchiveFileReader);
			
			log.info("Found " + classes.size() + " classes in " + input);
			
			Map<String, MethodClassVisitor> result = classes.stream().collect(Collectors.toMap(MethodClassVisitor::getClassName, Function.identity()));
			
			DefaultArchiveFileTransformer fileTransform = new DefaultArchiveFileTransformer( (file, classFile, writer) ->   {
				String className = fileToClass(file);
				MethodClassVisitor privateMethodClassVisitor = result.get(className);
				
				detectRenames(result, className);
				return new RenamePrivateMethodsClassVisitor(writer, privateMethodClassVisitor.getMethods(ACC_PRIVATE), "%s" + fileToSimpleName(file));
			});
			
			ArchiveTransformer transformer = new ArchiveTransformer();
	
			transformer.transform(input, output, fileTransform);
		} catch(Exception e) {
			log.warn("Problem transforming classes for " + input, e);
			
			try {
				Files.deleteIfExists(output);
			} catch (IOException e1) {
				// ignore
			}
		}
	}

	protected void detectRenames(Map<String, MethodClassVisitor> result, String className) {
		// naive implemenation
		// TODO a better approach would be to check if a rename is really necessary by checking against collisions 
		// in superclasses
		MethodClassVisitor methodClassVisitor = result.get(className);
		for(VisitedMethod vm : methodClassVisitor.getMethods(ACC_PRIVATE)) {
			vm.setRename(true);
		}
	}


	protected String fileToClass(String file) {
		return file.substring(0, file.length() - 6);
	}
	
	protected String fileToSimpleName(String file) {
		String fileToClass = fileToClass(file);
		
		return fileToClass.substring(fileToClass.lastIndexOf('/') + 1);
	}
	
	public static final void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Please specify two parameters: <input> <output>");
			System.out.println("Example parameters: classes.jar classes.extendable.jar");
			return;
		}
		
		Path input = new File(args[0]).toPath();

		Path output = new File(args[1]).toPath();

		Extender extender = new Extender(input, output);

		extender.run();
	}
	
}

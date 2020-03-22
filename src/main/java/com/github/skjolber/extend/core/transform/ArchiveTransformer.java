package com.github.skjolber.extend.core.transform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ArchiveTransformer {

	public void transform(Path input, Path output, ArchiveFileTransformer listener) throws IOException {
		try (
				InputStream is = Files.newInputStream(input);
				OutputStream os = Files.newOutputStream(output);
				) {
			transform(is, os, listener);
		}
	}
	
	public void transform(InputStream is, OutputStream os, ArchiveFileTransformer listener) throws IOException {

		try (
				ZipInputStream zis = new ZipInputStream(is);	
				ZipOutputStream zipOut = new ZipOutputStream(os);
				) {

			ZipEntry inputEntry = zis.getNextEntry();
			while (inputEntry != null) {
				byte[] transformed = listener.transformFile(inputEntry.getName(), toBytes(zis));
				if(transformed != null) {
					ZipEntry outputEntry = new ZipEntry(inputEntry.getName());
					zipOut.putNextEntry(outputEntry);
					zipOut.write(transformed);
				}
				inputEntry = zis.getNextEntry();
			}
		}
	}

	private byte[] toBytes(ZipInputStream zis) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024 * 16];

		int len;
		while ((len = zis.read(buffer)) > 0) {
			bout.write(buffer, 0, len);
		}

		return bout.toByteArray();
	}
}

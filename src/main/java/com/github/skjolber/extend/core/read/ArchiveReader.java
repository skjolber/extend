package com.github.skjolber.extend.core.read;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ArchiveReader {

	public void read(Path input, ArchiveFileReader listener) throws IOException {
		try (
				InputStream is = Files.newInputStream(input);
				) {
			read(is, listener);
		}
	}

	public void read(InputStream is, ArchiveFileReader listener) throws IOException {

		try (
				ZipInputStream zis = new ZipInputStream(is);	
				) {

			ZipEntry inputEntry = zis.getNextEntry();
			while (inputEntry != null) {
				listener.readFile(inputEntry.getName(), toBytes(zis));
				inputEntry = zis.getNextEntry();
			}
		}
	}

	protected byte[] toBytes(ZipInputStream zis) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024 * 16];

		int len;
		while ((len = zis.read(buffer)) > 0) {
			bout.write(buffer, 0, len);
		}

		return bout.toByteArray();
	}
}

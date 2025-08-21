package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtils {

	public static String encodeFileToBase64(String filePath) {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			throw new RuntimeException("Failed to encode log file: " + filePath, e);
		}
	}
}

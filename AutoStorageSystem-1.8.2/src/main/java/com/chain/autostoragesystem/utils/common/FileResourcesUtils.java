package com.chain.autostoragesystem.utils.common;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileResourcesUtils {

    // The class loader that loaded the class
    private final static ClassLoader classLoader = FileResourcesUtils.class.getClassLoader();

    public static String mapInputStreamToString(InputStream is) throws IOException {
        Objects.requireNonNull(is);

        StringBuilder stringBuilder = new StringBuilder();

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        return stringBuilder.toString();
    }

    // print input stream
    public static void printInputStream(InputStream is) {
        Objects.requireNonNull(is);

        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // print a file
    public static void printFile(File file) {
        Objects.requireNonNull(file);

        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    public static InputStream getFileFromResourceAsStream(String filePath) {
        StringUtils.requiredNonBlank(filePath);

        Optional<InputStream> inputStream = getFileFromResourceAsStreamOptional(filePath);

        // the stream holding the file content
        return inputStream
                .orElseThrow(() -> new IllegalArgumentException("file not found! " + filePath));
    }

    public static String getFileFromResourceAsString(String filePath) throws IOException {
        StringUtils.requiredNonBlank(filePath);

        InputStream is = getFileFromResourceAsStream(filePath);
        return mapInputStreamToString(is);
    }

    public static void assertFileExists(String filePath) {
        StringUtils.requiredNonBlank(filePath);

        if (!getFileFromResourceAsStreamOptional(filePath).isPresent()) {
            throw new IllegalArgumentException("file not found! " + filePath);
        }
    }

    /*
        The resource URL is not working in the JAR
        If we try to access a file that is inside a JAR,
        It throws NoSuchFileException (linux), InvalidPathException (Windows)

        Resource URL Sample: file:java-io.jar!/json/file1.json
     */
    public static File getFileFromResource(String filePath) throws URISyntaxException {
        StringUtils.requiredNonBlank(filePath);

        URL resource = classLoader.getResource(filePath);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + filePath);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());

            return new File(resource.toURI());
        }

    }

    public static Optional<InputStream> getFileFromResourceAsStreamOptional(String filePath) {
        StringUtils.requiredNonBlank(filePath);
        InputStream is = classLoader.getResourceAsStream(filePath);
        return Optional.ofNullable(is);
    }
}
package me.cortex.TreeCracker.program;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class CudaProgram {
    private String currentSource;

    public void loadFromResources(String name) throws IOException {
        URL file = Thread.currentThread().getContextClassLoader().getResource(name);
        if (file == null)
            throw new IllegalStateException("Resource file not found");
        currentSource = IOUtils.toString(file, Charset.defaultCharset());
    }

    public void replaceFirstUsingKeyword(String keyword, String replaceWith) {
        if (!currentSource.contains(keyword))
            throw new IllegalStateException("Keyword not in source");
        currentSource = currentSource.replaceFirst(keyword, replaceWith);
    }

    public void replaceAllUsingKeyword(String keyword, String replaceWith) {
        if (!currentSource.contains(keyword))
            throw new IllegalStateException("Keyword not in source");
        currentSource = currentSource.replaceAll(keyword, replaceWith);
    }

    public String getSource() {
        return currentSource;
    }



    public void exportSource(File file) throws IOException {
        Files.write(file.toPath(), currentSource.getBytes());
    }

    /*
    public void exportSource() throws IOException {
        Files.write(file.toPath(), currentSource.getBytes());
    }
    */
}

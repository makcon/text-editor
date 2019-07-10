package test.maksim.editor.ws.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import test.maksim.editor.common.dto.ModifyLinesRequest.ModifiedLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class FileTextRepository implements TextRepository {

    @Value("${texts.folder:texts}")
    private String textsFolder;

    @Override
    public String save(List<String> lines) {
        var textId = generateId();
        log.debug("Generated textId: {}", textId);

        createTextFolder(textId);
        saveToFiles(textId, lines, 1);

        return textId;
    }

    @Override
    public Optional<String> findByLineNumber(String textId,
                                             int lineNumber) {
        return Files.notExists(buildLineFilePath(textId, lineNumber))
                ? Optional.empty()
                : Optional.ofNullable(readLines(buildLineFilePath(textId, lineNumber)).get(0));
    }

    @Override
    public List<String> getAllLines(String textId) {
        if (Files.notExists(buildTextFolderPath(textId))) {
            log.warn("Folder not found for textId: {}", textId);
            return List.of();
        }

        try (Stream<Path> stream = Files.walk(buildTextFolderPath(textId))) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(this::readLines)
                    .flatMap(Collection::stream)
                    .collect(toList());
        } catch (IOException e) {
            log.error("Failed to read text: {}", textId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addLines(String textId,
                         List<String> lines) {
        try (Stream<Path> files = Files.list(buildTextFolderPath(textId))) {
            saveToFiles(textId, lines, Math.toIntExact(files.count() + 1));
        } catch (IOException e) {
            log.error("Failed to open files for text: {}", textId, e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Integer> modifyLines(String textId,
                                     List<ModifiedLine> lines) {
        List<Integer> modifiedLines = new ArrayList<>();
        for (ModifiedLine line : lines) {
            if (Files.exists(buildLineFilePath(textId, line.getLineNumber()))) {
                saveToFile(textId, line.getLineNumber(), line.getText());
                modifiedLines.add(line.getLineNumber());
            } else {
                log.warn("File not found, text: {}, line: {}", textId, line.getLineNumber());
            }
        }

        return modifiedLines;
    }

    @Override
    public void deleteLines(String textId,
                            List<Integer> lineNumbers) {
        lineNumbers.forEach(it -> deleteLine(textId, it));
        reorganizeLineFiles(textId);
    }

    private void reorganizeLineFiles(String textId) {
        List<Path> linePaths = getLinePaths(textId);
        for (int i = 0; i < linePaths.size(); i++) {
            try {
                Files.move(linePaths.get(i), buildLineFilePath(textId, i + 1));
            } catch (IOException e) {
                log.error("Failed to rename file: {}", linePaths.get(i), e);
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteLine(String textId,
                            Integer lineNumber) {
        try {
            Files.delete(buildLineFilePath(textId, lineNumber));
        } catch (NoSuchFileException e) {
            log.warn("File not found: {}", buildLineFilePath(textId, lineNumber));
        } catch (IOException e) {
            log.error("Failed to delete file: {}", lineNumber, e);
            throw new RuntimeException(e);
        }
    }

    private List<String> readLines(Path path) {
        try (Stream<String> stream = Files.lines(path)) {
            return stream.collect(toList());
        } catch (IOException e) {
            log.error("Failed to read file: {}", path, e);
            throw new RuntimeException(e);
        }
    }

    private void createTextFolder(String textId) {
        try {
            Files.createDirectories(buildTextFolderPath(textId));
        } catch (IOException e) {
            log.error("Failed to create folder for text: {}", textId, e);
            throw new RuntimeException(e);
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private void saveToFiles(String textId,
                             List<String> lines,
                             int startIndex) {
        for (int i = 0; i < lines.size(); i++) {
            saveToFile(textId, i + startIndex, lines.get(i));
        }
    }

    private void saveToFile(String textId,
                            int lineNumber,
                            String line) {
        try {
            Files.writeString(buildLineFilePath(textId, lineNumber), line);
        } catch (IOException e) {
            log.error("Failed to save text for id: {}", textId, e);
            throw new RuntimeException(e);
        }
    }

    private Path buildTextFolderPath(String textId) {
        return Path.of(textsFolder, File.separator, textId);
    }

    private Path buildLineFilePath(String textId,
                                   int lineNumber) {
        return Path.of(textsFolder, File.separator, textId, File.separator, String.valueOf(lineNumber));
    }

    private List<Path> getLinePaths(String textId) {
        try (Stream<Path> files = Files.list(buildTextFolderPath(textId))){
            return files.collect(toList());
        } catch (IOException e) {
            log.error("Failed to get files for text: {}", textId, e);
            throw new RuntimeException(e);
        }
    }
}

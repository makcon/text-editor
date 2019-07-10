package test.maksim.editor.ws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import test.maksim.editor.ws.repository.FileTextRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * NOTE: this is a very primitive and not efficient search service.
 * In real life I'd put texts to search engine like Solr, Elasticsearch.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FileLinesSearchService implements LinesSearchService {

    private final FileTextRepository textRepository;

    @Override
    public List<String> search(String textId,
                               String query) {
        return textRepository.getLinePaths(textId).stream()
                .map(it -> findLine(it, query))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<String> findLine(Path path,
                                      String query) {
        try (Stream<String> lines = Files.lines(path)){
            return lines.filter(it -> it.contains(query))
                    .findFirst();
        } catch (IOException e) {
            log.error("Failed to get files for path: {}", path, e);
            throw new RuntimeException(e);
        }
    }
}

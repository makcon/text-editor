package test.maksim.editor.ws.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;
import test.maksim.editor.ws.repository.FileTextRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileLinesSearchServiceTest {

    private static final String TEXT_FOLDER = "testTexts";

    @InjectMocks
    private FileLinesSearchService service;

    @Mock
    private FileTextRepository textRepository;

    @Before
    public void setUp() throws Exception {
        Files.createDirectory(Path.of(TEXT_FOLDER));
    }

    @After
    public void tearDown() throws Exception {
        FileSystemUtils.deleteRecursively(Path.of(TEXT_FOLDER));
    }

    @Test
    public void search_stored4Lines_2matches_shouldReturn2() throws Exception {
        var textId = "textId";
        var line1 = "word1 word2";
        var line2 = "word2 word3";
        var line3 = "word1 word3";
        var line4 = "word3";
        List<String> storedLines = List.of(line1, line2, line3, line4);
        storeLines(storedLines);
        when(textRepository.getLinePaths(anyString())).thenReturn(createPaths(storedLines.size()));

        List<String> foundLines = service.search(textId, "word1");

        assertThat(foundLines, equalTo(List.of(line1, line3)));
        verify(textRepository).getLinePaths(textId);
    }

    @Test
    public void search_stored2Lines_noMatches_shouldReturnEmpty() throws Exception {
        List<String> storedLines = List.of("word1 word2", "word2 word3");
        storeLines(storedLines);
        when(textRepository.getLinePaths(anyString())).thenReturn(createPaths(storedLines.size()));

        List<String> foundLines = service.search("textId", "unknown");

        assertThat(foundLines, hasSize(0));
    }

    // Util methods

    private void storeLines(List<String> storedLines) throws Exception {
        for (int i = 0; i < storedLines.size(); i++) {
            Files.writeString(createPath(i), storedLines.get(i));
        }
    }

    private Path createPath(int index) {
        return Path.of(TEXT_FOLDER, "/", String.valueOf(index));
    }

    private List<Path> createPaths(int size) {
        return IntStream.range(0, size)
                .mapToObj(this::createPath)
                .collect(toList());
    }
}
package test.maksim.editor.ws.repository;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileSystemUtils;
import test.maksim.editor.common.dto.ModifyLinesRequest.ModifiedLine;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class FileTextRepositoryTest {

    private static final String TEXT_FOLDER = "testTexts";
    private static final String LINE_1 = "line1";
    private static final String LINE_2 = "line2";
    private static final String LINE_3 = "line3";
    private static final String LINE_4 = "line4";

    private final FileTextRepository repository = new FileTextRepository();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(repository, "textsFolder", TEXT_FOLDER);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        FileSystemUtils.deleteRecursively(Path.of(TEXT_FOLDER));
    }

    @Test
    public void save_shouldReturnTextId() {
        String textId = repository.save(List.of(LINE_1));

        assertThat(textId, notNullValue());
    }

    @Test
    public void findByLineNumber_saved2Lines_shouldReturn2First() {
        String textId = saveLines(List.of(LINE_1, LINE_2));

        Optional<String> line1 = repository.getByLineNumber(textId, 1);
        Optional<String> line2 = repository.getByLineNumber(textId, 2);
        Optional<String> line3 = repository.getByLineNumber(textId, 3);

        assertThat(line1.isPresent(), is(true));
        assertThat(line2.isPresent(), is(true));
        assertThat(line3.isPresent(), is(false));
        assertThat(line1.get(), equalTo(LINE_1));
        assertThat(line2.get(), equalTo(LINE_2));
    }

    @Test
    public void findByLineNumber_textNotFound_shouldReturnEmpty() {
        Optional<String> line1 = repository.getByLineNumber("unknownId", 1);

        assertThat(line1.isPresent(), is(false));
    }

    @Test
    public void getAllLines_saved2Lines_shouldReturn2() {
        String textId = saveLines(List.of(LINE_1, LINE_2));

        List<String> allLines = repository.getAllLines(textId);

        assertThat(allLines, equalTo(List.of(LINE_1, LINE_2)));
    }

    @Test
    public void getAllLines_textNotFound_shouldReturnEmpty() {
        List<String> allLines = repository.getAllLines("unknownId");

        assertThat(allLines, hasSize(0));
    }

    @Test
    public void addLines_saved2Lines_added1_shouldReturn3() {
        String textId = saveLines(List.of(LINE_1, LINE_2));

        repository.addLines(textId, List.of(LINE_3));

        List<String> allLines = repository.getAllLines(textId);
        assertThat(allLines, equalTo(List.of(LINE_1, LINE_2, LINE_3)));
    }

    @Test(expected = RuntimeException.class)
    public void addLines_unknownTextId_shouldThrownException() {
        repository.addLines("unknownId", List.of(LINE_3));
    }

    @Test
    public void modifyLines() {
        String textId = saveLines(List.of(LINE_1, LINE_2));
        List<ModifiedLine> lines = List.of(
                new ModifiedLine(1, LINE_3),
                new ModifiedLine(3, LINE_1)
        );

        List<Integer> modifyLines = repository.modifyLines(textId, lines);

        assertThat(modifyLines, equalTo(List.of(1)));
        List<String> allLines = repository.getAllLines(textId);
        assertThat(allLines, equalTo(List.of(LINE_3, LINE_2)));
    }

    @Test
    public void deleteLines_saved4Lines_delete2_shouldReturn2() {
        String textId = saveLines(List.of(LINE_1, LINE_2, LINE_3, LINE_4));

        repository.deleteLines(textId, List.of(1, 3));

        List<String> allLines = repository.getAllLines(textId);
        assertThat(allLines, equalTo(List.of(LINE_2, LINE_4)));
        Optional<String> line2 = repository.getByLineNumber(textId, 1);
        Optional<String> line4 = repository.getByLineNumber(textId, 2);
        assertThat(line2.isPresent(), is(true));
        assertThat(line4.isPresent(), is(true));
        assertThat(line2.get(), equalTo(LINE_2));
        assertThat(line4.get(), equalTo(LINE_4));
    }

    @Test
    public void deleteLines_saved2Lines_deleteUnknown_shouldDeleteNothing() {
        String textId = saveLines(List.of(LINE_1, LINE_2));

        repository.deleteLines(textId, List.of(3));

        List<String> allLines = repository.getAllLines(textId);
        assertThat(allLines, equalTo(List.of(LINE_1, LINE_2)));
    }

    // Util methods

    private String saveLines(List<String> lines) {
        return repository.save(lines);
    }
}
package test.maksim.editor.ws.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import test.maksim.editor.common.dto.*;
import test.maksim.editor.ws.repository.TextRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static test.maksim.editor.common.dto.ConcatenateSymbol.DOT;

@RunWith(MockitoJUnitRunner.class)
public class EditorServiceTest {

    private static final String TEXT_ID = "textId";
    private static final String LINE_1 = "line1";
    private static final String LINE_2 = "line2";

    @InjectMocks
    private EditorService service;

    @Mock
    private TextRepository textRepository;

    @Test
    public void saveText_shouldCallRepositoryAndReturnReposponse() {
        when(textRepository.save(anyList())).thenReturn(TEXT_ID);
        List<String> lines = List.of(LINE_1);

        List<SaveTextResponse> responses = service.saveTexts(List.of(new SaveTextRequest(lines)));

        assertThat(responses, equalTo(List.of(new SaveTextResponse(TEXT_ID, 1))));
        verify(textRepository).save(lines);
    }

    @Test
    public void findByLineNumber_shouldCallRepositoryAndReturnLine() {
        when(textRepository.getByLineNumber(anyString(), anyInt())).thenReturn(Optional.of(LINE_1));

        Optional<String> line = service.getByLineNumber(TEXT_ID, 1);

        assertThat(line.isPresent(), is(true));
        assertThat(line.get(), equalTo(LINE_1));
        verify(textRepository).getByLineNumber(TEXT_ID, 1);
    }

    @Test
    public void addLines_shouldCallRepository() {
        service.addLines(List.of(new AddLineRequest(TEXT_ID, List.of(LINE_1))));

        verify(textRepository).addLines(TEXT_ID, List.of(LINE_1));
    }

    @Test
    public void modifyLines_shouldCallRepositoryAndReturnModifiedLines() {
        List<Integer> modifiedLines = List.of(2);
        when(textRepository.modifyLines(anyString(), anyList())).thenReturn(modifiedLines);
        List<ModifyLinesRequest.ModifiedLine> lines = List.of(new ModifyLinesRequest.ModifiedLine(2, LINE_1));

        Map<String, List<Integer>> actualModifiedLines = service.modifyLines(List.of(new ModifyLinesRequest(TEXT_ID, lines)));

        assertThat(actualModifiedLines, equalTo(Map.of(TEXT_ID, modifiedLines)));
        verify(textRepository).modifyLines(TEXT_ID, lines);
    }

    @Test
    public void concatenate_repositoryReturnsLines_shouldConcatenate() {
        when(textRepository.getAllLines(anyString())).thenReturn(List.of(LINE_1, LINE_2));

        Optional<String> concatenated = service.concatenate(TEXT_ID, DOT);

        assertThat(concatenated.isPresent(), is(true));
        assertThat(concatenated.get(), equalTo(String.join(DOT.getSymbol(), List.of(LINE_1, LINE_2))));
        verify(textRepository).getAllLines(TEXT_ID);
    }

    @Test
    public void concatenate_repositoryReturnsEmpty_shouldReturnEmpty() {
        when(textRepository.getAllLines(anyString())).thenReturn(List.of());

        Optional<String> concatenated = service.concatenate(TEXT_ID, DOT);

        assertThat(concatenated.isPresent(), is(false));
        verify(textRepository).getAllLines(TEXT_ID);
    }

    @Test
    public void deleteLines_shouldCallRepository() {
        service.deleteLines(List.of(new DeleteLinesRequest(TEXT_ID, List.of(1))));

        verify(textRepository).deleteLines(TEXT_ID, List.of(1));
    }
}
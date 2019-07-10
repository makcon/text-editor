package test.maksim.editor.ws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.maksim.editor.common.dto.*;
import test.maksim.editor.ws.repository.TextRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EditorService {

    private final TextRepository textRepository;

    public SaveTextResponse saveText(SaveTextRequest request) {
        String textId = textRepository.save(request.getLines());

        return new SaveTextResponse(textId, request.getLines().size());
    }

    public Optional<String> findByLineNumber(String textId,
                                             int lineNumber) {
        return textRepository.findByLineNumber(textId, lineNumber);
    }

    public void addLines(AddLineRequest request) {
        textRepository.addLines(request.getTextId(), request.getLines());
    }

    public List<Integer> modifyLines(ModifyLinesRequest request) {
        return textRepository.modifyLines(request.getTextId(), request.getLines());
    }

    public Optional<String> concatenate(String textId,
                                        ConcatenateSymbol symbol) {
        List<String> allLines = textRepository.getAllLines(textId);

        return allLines.isEmpty()
                ? Optional.empty()
                : Optional.of(String.join(symbol.getSymbol(), allLines));
    }
}

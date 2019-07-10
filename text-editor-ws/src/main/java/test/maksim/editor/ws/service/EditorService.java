package test.maksim.editor.ws.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.maksim.editor.common.dto.*;
import test.maksim.editor.ws.repository.TextRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class EditorService {

    private final TextRepository textRepository;

    public List<SaveTextResponse> saveTexts(List<SaveTextRequest> requests) {
        return requests.stream()
                .map(this::saveText)
                .collect(toList());
    }

    public Optional<String> findByLineNumber(String textId,
                                             int lineNumber) {
        return textRepository.findByLineNumber(textId, lineNumber);
    }

    public void addLines(List<AddLineRequest> requests) {
        requests.forEach(it -> textRepository.addLines(it.getTextId(), it.getLines()));
    }

    public Map<String, List<Integer>> modifyLines(List<ModifyLinesRequest> requests) {
        return requests.stream()
                .collect(toMap(ModifyLinesRequest::getTextId, it -> textRepository.modifyLines(it.getTextId(), it.getLines())));
    }

    public Optional<String> concatenate(String textId,
                                        ConcatenateSymbol symbol) {
        List<String> allLines = textRepository.getAllLines(textId);

        return allLines.isEmpty()
                ? Optional.empty()
                : Optional.of(String.join(symbol.getSymbol(), allLines));
    }

    private SaveTextResponse saveText(SaveTextRequest request) {
        String textId = textRepository.save(request.getLines());
        log.info("Text with id: {} is created", textId);

        return new SaveTextResponse(textId, request.getLines().size());
    }
}

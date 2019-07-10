package test.maksim.editor.ws.repository;

import test.maksim.editor.common.dto.ModifyLinesRequest.ModifiedLine;

import java.util.List;
import java.util.Optional;

public interface TextRepository {

    String save(List<String> lines);

    Optional<String> findByLineNumber(String textId,
                                      int lineNumber);

    List<String> getAllLines(String textId);

    void addLines(String textId,
                  List<String> lines);

    List<Integer> modifyLines(String textId,
                              List<ModifiedLine> lines);
}

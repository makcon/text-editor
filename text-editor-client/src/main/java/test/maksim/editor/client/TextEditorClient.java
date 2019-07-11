package test.maksim.editor.client;

import test.maksim.editor.common.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TextEditorClient {

    CompletableFuture<List<SaveTextResponse>> saveText(List<SaveTextRequest> requests);

    CompletableFuture<Optional<String>> getLine(String textId,
                                                int lineNumber);

    CompletableFuture<Void> addLines(List<AddLineRequest> requests);

    CompletableFuture<Map<String, List<Integer>>> modifyLines(List<ModifyLinesRequest> requests);

    CompletableFuture<Optional<String>> concatenate(String textId,
                                                    ConcatenateSymbol concatenateSymbol);

    CompletableFuture<Void> deleteLines(List<DeleteLinesRequest> requests);

    CompletableFuture<List<String>> findLines(String textId,
                                              String query);
}

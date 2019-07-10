package test.maksim.editor.ws.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import test.maksim.editor.common.constants.Endpoints;
import test.maksim.editor.common.dto.*;
import test.maksim.editor.ws.service.EditorService;
import test.maksim.editor.ws.service.LinesSearchService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EditorController {

    private final EditorService editorService;
    private final LinesSearchService searchService;
    private final AsyncListenableTaskExecutor serviceExecutor;

    @PostMapping(Endpoints.SAVE_TEXTS)
    public ListenableFuture<List<SaveTextResponse>> saveText(@RequestBody List<SaveTextRequest> requests) {
        log.info("Receive {} save text requests", requests.size());
        return serviceExecutor.submitListenable(() -> editorService.saveTexts(requests));
    }

    @GetMapping(Endpoints.GET_LINE)
    public ListenableFuture<Optional<String>> getLine(@RequestParam("textId") String textId,
                                                      @RequestParam("lineNumber") int lineNumber) {
        log.info("Received request to find line {} of text: {}", lineNumber, textId);
        return serviceExecutor.submitListenable(() -> editorService.getByLineNumber(textId, lineNumber));
    }

    @PutMapping(Endpoints.ADD_LINES)
    public void addLines(@RequestBody List<AddLineRequest> requests) {
        log.info("Receive {} add lines requests", requests.size());
        serviceExecutor.submitListenable(() -> editorService.addLines(requests));
    }

    @PutMapping(Endpoints.MODIFY_LINES)
    public ListenableFuture<Map<String, List<Integer>>> modifyLines(@RequestBody List<ModifyLinesRequest> requests) {
        log.info("Receive {} modify lines requests", requests.size());
        return serviceExecutor.submitListenable(() -> editorService.modifyLines(requests));
    }

    @GetMapping(Endpoints.CONCATENATE)
    public ListenableFuture<Optional<String>> concatenate(@RequestParam("textId") String textId,
                                                          @RequestParam("symbol") ConcatenateSymbol symbol) {
        log.info("Received request to concatenate text: {} by symbol: {}", textId, symbol);
        return serviceExecutor.submitListenable(() -> editorService.concatenate(textId, symbol));
    }

    @PutMapping(Endpoints.DELETE_LINES)
    public void deleteLines(@RequestBody List<DeleteLinesRequest> requests) {
        log.info("Receive {} delete lines requests", requests.size());
        serviceExecutor.submitListenable(() -> editorService.deleteLines(requests));
    }

    @GetMapping(Endpoints.FIND_LINES)
    public ListenableFuture<List<String>> findLines(@RequestParam("textId") String textId,
                                                    @RequestParam("query") String query) {
        log.info("Received request to find lines of text: {}, query: {}", textId, query);
        return serviceExecutor.submitListenable(() -> searchService.search(textId, query));
    }
}

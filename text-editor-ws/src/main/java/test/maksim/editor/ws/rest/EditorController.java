package test.maksim.editor.ws.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import test.maksim.editor.common.constants.Endpoints;
import test.maksim.editor.common.dto.*;
import test.maksim.editor.ws.service.EditorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EditorController {

    private final EditorService editorService;
    private final AsyncListenableTaskExecutor serviceExecutor;

    @PostMapping(Endpoints.SAVE_TEXT)
    public ListenableFuture<SaveTextResponse> saveText(@RequestBody SaveTextRequest request) {
        log.info("Receive save text request to save {} lines", request.getLines().size());
        return serviceExecutor.submitListenable(() -> editorService.saveText(request));
    }

    @GetMapping(Endpoints.FIND_LINE)
    public ListenableFuture<Optional<String>> findLine(@RequestParam("textId") String textId,
                                                       @RequestParam("lineNumber") int lineNumber) {
        log.info("Received request to find line {} of text: {}", lineNumber, textId);
        return serviceExecutor.submitListenable(() -> editorService.findByLineNumber(textId, lineNumber));
    }

    @PutMapping(Endpoints.ADD_LINES)
    public void addLines(@RequestBody AddLineRequest request) {
        log.info("Receive add lines request for text {}", request.getTextId());
        serviceExecutor.submitListenable(() -> editorService.addLines(request));
    }

    @PutMapping(Endpoints.MODIFY_LINES)
    public ListenableFuture<List<Integer>> modifyLines(@RequestBody ModifyLinesRequest request) {
        log.info("Receive modify lines request for text {}", request.getTextId());
        return serviceExecutor.submitListenable(() -> editorService.modifyLines(request));
    }

    @GetMapping(Endpoints.CONCATENATE)
    public ListenableFuture<Optional<String>> concatenate(@RequestParam("textId") String textId,
                                                          @RequestParam("symbol") ConcatenateSymbol symbol) {
        log.info("Received request to concatenate text: {} by symbol: {}", textId, symbol);
        return serviceExecutor.submitListenable(() -> editorService.concatenate(textId, symbol));
    }
}

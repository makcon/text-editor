package test.maksim.editor.client;

import test.maksim.editor.common.dto.*;
import test.maksim.editor.common.dto.ModifyLinesRequest.ModifiedLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TextEditorClientUsage {

    public static void main(String[] args) {
        TextEditorClientFactory factory = new TextEditorClientFactory();
        TextEditorClient client = factory.defaultClient("http://localhost:8080");

        String textId = executeSaveText(client);
        executeGetLine(client, textId);
        executeAddLines(client, textId);
        executeModifyLines(client, textId);
        executeConcatenate(client, textId);
        executeDeleteLines(client, textId);
        executeFindLines(client, textId);
    }

    private static String executeSaveText(TextEditorClient client) {
        var request = new SaveTextRequest(List.of("Line 1", "Line 2"));

        List<SaveTextResponse> textResponses = client.saveText(List.of(request)).join();
        System.out.println(textResponses);

        return textResponses.get(0).getTextId();
    }

    private static void executeGetLine(TextEditorClient client,
                                       String textId) {
        Optional<String> line1 = client.getLine(textId, 1).join();
        Optional<String> line2 = client.getLine(textId, 3).join();
        System.out.println("Get line1: " + line1);
        System.out.println("Get line2: " + line2);
    }

    private static void executeAddLines(TextEditorClient client,
                                        String textId) {
        var request = new AddLineRequest(textId, List.of("Line 3", "Line 4"));

        client.addLines(List.of(request)).join();
        System.out.println("Lines added");
    }

    private static void executeModifyLines(TextEditorClient client,
                                           String textId) {
        List<ModifiedLine> lines = List.of(
                new ModifiedLine(2, "One line for search"),
                new ModifiedLine(4, "Second line for search"),
                new ModifiedLine(6, "Unknown line")
        );
        var request = new ModifyLinesRequest(textId, lines);

        Map<String, List<Integer>> result = client.modifyLines(List.of(request)).join();
        System.out.println("Modify lines: " + result);
    }

    private static void executeConcatenate(TextEditorClient client,
                                           String textId) {
        Optional<String> concatenated = client.concatenate(textId, ConcatenateSymbol.DOT).join();
        System.out.println("Concatenated: " + concatenated);
    }

    private static void executeDeleteLines(TextEditorClient client,
                                           String textId) {
        client.deleteLines(List.of(new DeleteLinesRequest(textId, List.of(1)))).join();
        System.out.println("Lines deleted");
    }

    private static void executeFindLines(TextEditorClient client,
                                         String textId) {
        List<String> foundLines = client.findLines(textId, "search").join();
        System.out.println("Found lines: " + foundLines);
    }
}
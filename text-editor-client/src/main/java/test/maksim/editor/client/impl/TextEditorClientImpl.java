package test.maksim.editor.client.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import test.maksim.editor.client.TextEditorClient;
import test.maksim.editor.common.constants.Endpoints;
import test.maksim.editor.common.constants.RequestParams;
import test.maksim.editor.common.dto.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscribers;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Builder
public class TextEditorClientImpl implements TextEditorClient {

    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_GET = "GET";

    private static final Type TYPE_VOID_RESPONSE = new TypeToken<Void>() {}.getType();
    private static final Type TYPE_SAVE_TEXT_RESPONSE = new TypeToken<List<SaveTextResponse>>() {}.getType();
    private static final Type TYPE_MODIFY_LINES_RESPONSE = new TypeToken<Map<String, List<Integer>>>() {}.getType();
    private static final Type TYPE_FIND_LINES_RESPONSE = new TypeToken<List<String>>() {}.getType();

    private final String serviceUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    @Override
    public CompletableFuture<List<SaveTextResponse>> saveText(List<SaveTextRequest> requests) {
        return executePost(Endpoints.SAVE_TEXTS, METHOD_POST, TYPE_SAVE_TEXT_RESPONSE, requests);
    }

    @Override
    public CompletableFuture<Optional<String>> getLine(String textId,
                                                       int lineNumber) {
        Map<String, String> params = Map.of(
                RequestParams.TEXT_ID, textId,
                RequestParams.LINE_NUMBER, String.valueOf(lineNumber)
        );

        return executeGet(Endpoints.GET_LINE, params, getStringBodyHandler())
                .thenApply(HttpResponse::body)
                .thenApply(Optional::ofNullable);
    }

    @Override
    public CompletableFuture<Void> addLines(List<AddLineRequest> requests) {
        return executePost(Endpoints.ADD_LINES, METHOD_PUT, TYPE_VOID_RESPONSE, requests);
    }

    @Override
    public CompletableFuture<Map<String, List<Integer>>> modifyLines(List<ModifyLinesRequest> requests) {
        return executePost(Endpoints.MODIFY_LINES, METHOD_PUT, TYPE_MODIFY_LINES_RESPONSE, requests);
    }

    @Override
    public CompletableFuture<Optional<String>> concatenate(String textId,
                                                           ConcatenateSymbol concatenateSymbol) {
        Map<String, String> params = Map.of(
                RequestParams.TEXT_ID, textId,
                RequestParams.CONCATENATE_SYMBOL, concatenateSymbol.name()
        );

        return executeGet(Endpoints.CONCATENATE, params, getStringBodyHandler())
                .thenApply(HttpResponse::body)
                .thenApply(Optional::ofNullable);
    }

    @Override
    public CompletableFuture<Void> deleteLines(List<DeleteLinesRequest> requests) {
        return executePost(Endpoints.DELETE_LINES, METHOD_DELETE, TYPE_VOID_RESPONSE, requests);
    }

    @Override
    public CompletableFuture<List<String>> findLines(String textId,
                                                     String query) {
        Map<String, String> params = Map.of(
                RequestParams.TEXT_ID, textId,
                RequestParams.QUERY, query
        );

        return executeGet(Endpoints.FIND_LINES, params, getStringBodyHandler())
                .thenApply(response -> gson.fromJson(response.body(), TYPE_FIND_LINES_RESPONSE));
    }

    private <T> CompletableFuture<T> executePost(String endpoint,
                                                 String method,
                                                 Type type,
                                                 Object payload) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceUrl + endpoint))
                .header("Content-Type", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(gson.toJson(payload)))
                .build();

        return httpClient.sendAsync(request, getStringBodyHandler())
                .thenApply(response -> gson.fromJson(response.body(), type));
    }

    private <T> CompletableFuture<HttpResponse<T>> executeGet(String endpoint,
                                                               Map<String, String> params,
                                                               HttpResponse.BodyHandler<T> bodyHandler) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceUrl + endpoint + '?' + buildParamsAsString(params)))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, bodyHandler);
    }

    private String buildParamsAsString(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(e -> e.getKey() + '=' + URLEncoder.encode(e.getValue(), UTF_8))
                .collect(Collectors.joining("&"));
    }

    private HttpResponse.BodyHandler<String> getStringBodyHandler() {
        return responseInfo -> BodySubscribers.ofString(UTF_8);
    }
}

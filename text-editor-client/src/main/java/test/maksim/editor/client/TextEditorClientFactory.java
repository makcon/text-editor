package test.maksim.editor.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import test.maksim.editor.client.impl.TextEditorClientImpl;

import java.net.http.HttpClient;

public class TextEditorClientFactory {

    public TextEditorClient defaultClient(String serviceUrl) {
        return TextEditorClientImpl.builder()
                .gson(createGson())
                .httpClient(HttpClient.newHttpClient())
                .serviceUrl(serviceUrl)
                .build();
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }
}

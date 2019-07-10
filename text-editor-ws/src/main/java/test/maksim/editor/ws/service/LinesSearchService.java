package test.maksim.editor.ws.service;

import java.util.List;

public interface LinesSearchService {

    List<String> search(String textId,
                        String query);
}

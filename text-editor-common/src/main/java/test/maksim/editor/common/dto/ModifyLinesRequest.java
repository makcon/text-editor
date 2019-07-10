package test.maksim.editor.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyLinesRequest {

    private String textId;
    private List<ModifiedLine> lines;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifiedLine {

        private int lineNumber;
        private String text;
    }
}

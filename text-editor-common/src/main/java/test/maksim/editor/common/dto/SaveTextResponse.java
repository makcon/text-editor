package test.maksim.editor.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveTextResponse {

    private String textId;
    private int lineNumbers;
}

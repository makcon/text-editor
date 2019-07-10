package test.maksim.editor.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteLinesRequest {

    private String textId;
    private List<Integer> lineNumbers;
}

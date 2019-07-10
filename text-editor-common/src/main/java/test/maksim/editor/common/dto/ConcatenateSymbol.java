package test.maksim.editor.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcatenateSymbol {

    SPACE(" "),
    SEMICOLON("; "),
    DOT(". ");

    private final String symbol;
}

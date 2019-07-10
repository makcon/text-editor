package test.maksim.editor.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    private static final String V1_PREFIX = "/v1";
    public static final String SAVE_TEXT = V1_PREFIX + "/text/save";
    public static final String FIND_LINE = V1_PREFIX + "/text/find/line";
    public static final String ADD_LINES = V1_PREFIX + "/text/add/lines";
    public static final String MODIFY_LINES = V1_PREFIX + "/text/modify/lines";
    public static final String CONCATENATE = V1_PREFIX + "/text/concatenate";
}

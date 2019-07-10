package test.maksim.editor.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    private static final String V1_PREFIX = "/v1";
    public static final String SAVE_TEXTS = V1_PREFIX + "/texts/save";
    public static final String FIND_LINE = V1_PREFIX + "/text/find/line";
    public static final String ADD_LINES = V1_PREFIX + "/texts/add/lines";
    public static final String MODIFY_LINES = V1_PREFIX + "/texts/modify/lines";
    public static final String CONCATENATE = V1_PREFIX + "/text/concatenate";
    public static final String DELETE_LINES = V1_PREFIX + "/texts/delete/lines";
}

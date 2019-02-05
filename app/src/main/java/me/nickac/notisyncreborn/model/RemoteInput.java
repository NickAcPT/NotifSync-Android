package me.nickac.notisyncreborn.model;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import me.nickac.notisyncreborn.utils.MiscUtils;

public class RemoteInput {
    private String label;
    private String[] choices;
    private Set<String> allowedDataTypes;
    private Map<String, Object> extras;
    private boolean allowsFreeFormInput;
    private boolean isDataOnly;
    private String resultKey;

    RemoteInput(android.app.RemoteInput input) {
        this.label = input.getLabel() != null ? input.getLabel().toString() : "";

        if (input.getChoices() != null)
            this.choices = Arrays.stream(input.getChoices()).map(CharSequence::toString).toArray(String[]::new);
        else
            this.choices = new String[]{};

        if (input.getExtras() != null)
            extras = MiscUtils.bundleToMap(input.getExtras());

        this.allowedDataTypes = input.getAllowedDataTypes();
        this.allowsFreeFormInput = input.getAllowFreeFormInput();
        this.resultKey = input.getResultKey();
        this.isDataOnly = input.isDataOnly();
    }

    public String getLabel() {
        return label;
    }

    public String[] getChoices() {
        return choices;
    }

    public Set<String> getAllowedDataTypes() {
        return allowedDataTypes;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public boolean isAllowFreeFormInput() {
        return allowsFreeFormInput;
    }

    public boolean isDataOnly() {
        return isDataOnly;
    }

    public String getResultKey() {
        return resultKey;
    }
}

/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.menu.pluginapi.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A property validator based on a list of choices.
 */
public class ChoicesPropertyValidationRules implements PropertyValidationRules {

    final private Set<String> enumValues;

    /**
     * Create an instance with an array of values, generally from an Enum.
     * @param values the value array
     * @param <T> the type is automatically captured
     */
    public <T extends Enum> ChoicesPropertyValidationRules(T[] values) {
        enumValues = Arrays.stream(values)
                .map(Enum::toString).collect(Collectors.toSet());
    }

    @Override
    public boolean isValueValid(String value) {
        return enumValues.contains(value);
    }

    @Override
    public boolean hasChoices() {
        return true;
    }

    @Override
    public List<String> choices() {
        return new ArrayList<>(enumValues);
    }

    @Override
    public String toString() {
        return "Choice Validator accepting " + String.join(", ", enumValues);
    }

}

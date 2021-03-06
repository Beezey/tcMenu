/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.menu.pluginapi.model.parameter;

/**
 * a CodeParameter extension that renders the variable representing the root of the tree.
 */
public class RootItemCodeParameter extends CodeParameter {

    public RootItemCodeParameter() {
        super("");
    }

    @Override
    public String getParameterValue(CodeConversionContext context) {
        return "&" + context.getRootObject();
    }
}

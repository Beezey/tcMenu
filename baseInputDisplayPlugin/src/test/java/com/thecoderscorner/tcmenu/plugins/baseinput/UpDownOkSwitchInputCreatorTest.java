/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.tcmenu.plugins.baseinput;

import com.thecoderscorner.tcmenu.plugins.util.TestUtil;
import org.junit.jupiter.api.Test;

import static com.thecoderscorner.menu.pluginapi.CreatorProperty.PropType.TEXTUAL;
import static com.thecoderscorner.menu.pluginapi.CreatorProperty.PropType.USE_IN_DEFINE;
import static com.thecoderscorner.menu.pluginapi.SubSystem.INPUT;
import static com.thecoderscorner.tcmenu.plugins.util.TestUtil.findAndSetValueOnProperty;
import static org.assertj.core.api.Assertions.assertThat;

class UpDownOkSwitchInputCreatorTest {

    @Test
    public void testUpDownSwitchesInterrupt() {
        UpDownOkSwitchInputCreator creator = new UpDownOkSwitchInputCreator();
        findAndSetValueOnProperty(creator, "PULLUP_LOGIC", INPUT, TEXTUAL, "false");
        findAndSetValueOnProperty(creator, "INTERRUPT_SWITCHES", INPUT, TEXTUAL, "true");
        findAndSetValueOnProperty(creator, "SWITCH_IODEVICE", INPUT, TEXTUAL, "");
        findAndSetValueOnProperty(creator, "ENCODER_UP_PIN", INPUT, USE_IN_DEFINE, "2");
        findAndSetValueOnProperty(creator, "ENCODER_DOWN_PIN", INPUT, USE_IN_DEFINE, "3");
        findAndSetValueOnProperty(creator, "ENCODER_OK_PIN", INPUT, USE_IN_DEFINE, "5");
        creator.initialise("root");
        var extractor = TestUtil.extractorFor(creator);

        assertThat(extractor.mapDefines()).isEqualToIgnoringNewLines(
            "#define ENCODER_UP_PIN 2\n" +
            "#define ENCODER_DOWN_PIN 3\n" +
            "#define ENCODER_OK_PIN 5\n"
        );

        assertThat(extractor.mapFunctions(creator.getFunctionCalls())).isEqualToIgnoringNewLines(
            "    switches.initialiseInterrupt(ioUsingArduino(), false);\n" +
            "    menuMgr.initForUpDownOk(&renderer, &root, ENCODER_UP_PIN, ENCODER_DOWN_PIN, ENCODER_OK_PIN);\n"
        );

        assertThat(creator.getVariables()).isEmpty();
        assertThat(creator.getIncludes()).isEmpty();
        assertThat(creator.getRequiredFiles()).isEmpty();

    }

    @Test
    public void testUpDownSwitchesNoInterrupt() {
        UpDownOkSwitchInputCreator creator = new UpDownOkSwitchInputCreator();
        creator.initialise("root");
        findAndSetValueOnProperty(creator, "PULLUP_LOGIC", INPUT, TEXTUAL, "true");
        findAndSetValueOnProperty(creator, "INTERRUPT_SWITCHES", INPUT, TEXTUAL, "false");
        findAndSetValueOnProperty(creator, "SWITCH_IODEVICE", INPUT, TEXTUAL, "io8574");
        findAndSetValueOnProperty(creator, "ENCODER_UP_PIN", INPUT, USE_IN_DEFINE, "55");
        findAndSetValueOnProperty(creator, "ENCODER_DOWN_PIN", INPUT, USE_IN_DEFINE, "67");
        findAndSetValueOnProperty(creator, "ENCODER_OK_PIN", INPUT, USE_IN_DEFINE, "88");
        creator.initialise("root");
        var extractor = TestUtil.extractorFor(creator);

        assertThat(extractor.mapDefines()).isEqualToIgnoringNewLines(
                "#define ENCODER_UP_PIN 55\n" +
                "#define ENCODER_DOWN_PIN 67\n" +
                "#define ENCODER_OK_PIN 88\n"
        );

        assertThat(extractor.mapExports(creator.getVariables())).isEqualToIgnoringNewLines(
                "extern IoAbstractionRef io8574;"
        );

        assertThat(extractor.mapFunctions(creator.getFunctionCalls())).isEqualToIgnoringNewLines(
            "    switches.initialise(io8574, true);\n" +
            "    menuMgr.initForUpDownOk(&renderer, &root, ENCODER_UP_PIN, ENCODER_DOWN_PIN, ENCODER_OK_PIN);\n"
        );

    }
}
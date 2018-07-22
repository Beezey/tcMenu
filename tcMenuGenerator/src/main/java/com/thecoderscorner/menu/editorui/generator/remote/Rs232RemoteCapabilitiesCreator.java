/*
 * Copyright (c) 2018 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 */

package com.thecoderscorner.menu.editorui.generator.remote;

import com.thecoderscorner.menu.editorui.generator.AbstractCodeCreator;
import com.thecoderscorner.menu.editorui.generator.ui.CreatorProperty;
import com.thecoderscorner.menu.editorui.project.CurrentEditorProject;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.thecoderscorner.menu.editorui.generator.arduino.ArduinoItemGenerator.LINE_BREAK;
import static com.thecoderscorner.menu.editorui.generator.ui.CreatorProperty.SubSystem.REMOTE;

public class Rs232RemoteCapabilitiesCreator extends AbstractCodeCreator {
    private final CurrentEditorProject project;
    private final List<CreatorProperty> creatorProperties = List.of(
            new CreatorProperty("SERIAL_BAUD", "Serial baud rate", "115200", REMOTE),
            new CreatorProperty("SERIAL_PORT", "Serial port name", "Serial", REMOTE)
    );

    public Rs232RemoteCapabilitiesCreator(CurrentEditorProject project) {
        this.project = project;
    }

    @Override
    public List<String> getIncludes() {
        return Arrays.asList(
                "#include <RemoteConnector.h>",
                "#include <SerialTransport.h>"
        );
    }

    @Override
    public String getGlobalVariables() {
        String projectName = Paths.get(project.getFileName()).getFileName().toString();
        return "const char PROGMEM applicationName[] = \"" +  projectName + "\";" + LINE_BREAK;
    }

    @Override
    public String getExportDefinitions() {
        return super.getExportDefinitions() +
               "extern const char applicationName[];" + LINE_BREAK;
    }

    @Override
    public List<CreatorProperty> properties() {
        return creatorProperties;
    }

    @Override
    public String getSetupCode(String rootItem) {
        String serialPort = findPropertyValue("SERIAL_PORT").getLatestValue();
        return "    " + serialPort +  ".begin(SERIAL_BAUD);" + LINE_BREAK +
               "    serialServer.begin(&" + serialPort + ", applicationName);" + LINE_BREAK;
    }
}

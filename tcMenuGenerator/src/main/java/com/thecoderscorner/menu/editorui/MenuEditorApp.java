/*
 * Copyright (c)  2016-2019 https://www.thecoderscorner.com (Nutricherry LTD).
 * This product is licensed under an Apache license, see the LICENSE file in the top-level directory.
 *
 */

package com.thecoderscorner.menu.editorui;

import com.thecoderscorner.menu.editorui.controller.ConfigurationStorage;
import com.thecoderscorner.menu.editorui.controller.MenuEditorController;
import com.thecoderscorner.menu.editorui.controller.PrefsConfigurationStorage;
import com.thecoderscorner.menu.editorui.generator.arduino.ArduinoLibraryInstaller;
import com.thecoderscorner.menu.editorui.generator.plugin.DirectoryCodePluginManager;
import com.thecoderscorner.menu.editorui.generator.plugin.EmbeddedPlatforms;
import com.thecoderscorner.menu.editorui.generator.plugin.PluginEmbeddedPlatformsImpl;
import com.thecoderscorner.menu.editorui.project.CurrentEditorProject;
import com.thecoderscorner.menu.editorui.project.FileBasedProjectPersistor;
import com.thecoderscorner.menu.editorui.uimodel.CurrentProjectEditorUIImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The application starting point for the JavaFX version of the application
 */
public class MenuEditorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        createDirsIfNeeded();

        primaryStage.setTitle("Embedded Menu Designer");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/menuEditor.fxml"));
        Pane myPane = loader.load();

        MenuEditorController controller = loader.getController();

        EmbeddedPlatforms platforms = new PluginEmbeddedPlatformsImpl();

        DirectoryCodePluginManager manager = new DirectoryCodePluginManager(platforms);
        manager.loadPlugins(System.getProperty("java.class.path") + System.getProperty("path.separator")
                          + System.getProperty("user.home") + "/.tcmenu", "plugins");

        ArduinoLibraryInstaller installer = new ArduinoLibraryInstaller();

        ConfigurationStorage prefsStore = new PrefsConfigurationStorage();

        CurrentProjectEditorUIImpl editorUI = new CurrentProjectEditorUIImpl(manager, primaryStage, platforms,
                installer, prefsStore);

        FileBasedProjectPersistor persistor = new FileBasedProjectPersistor();

        CurrentEditorProject project = new CurrentEditorProject(editorUI, persistor);


        controller.initialise(project, installer, editorUI, manager, prefsStore);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/menu-icon.png")));

        primaryStage.setOnCloseRequest((evt)-> {
            controller.persistPreferences();
            if(project.isDirty()) {
                evt.consume();
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Are you sure");
                alert.setHeaderText("There are unsaved changes, continue with exit anyway?");
                if(alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    Platform.exit();
                }
            }
        });
    }

    private void createDirsIfNeeded() {
        var homeDir = Paths.get(System.getProperty("user.home"));
        try {
            Path menuDir = homeDir.resolve(".tcmenu/logs");
            if(!Files.exists(menuDir)) {
                Files.createDirectories(menuDir);
            }
            Path pluginDir = homeDir.resolve(".tcmenu/plugins");
            if(!Files.exists(pluginDir)) {
                Files.createDirectories(pluginDir);
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Error creating user directory", ButtonType.CLOSE);
            alert.setContentText("Couldn't create user directory: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

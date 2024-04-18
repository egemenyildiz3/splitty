/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.prefs.BackingStoreException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;
import javassist.NotFoundException;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static ResourceBundle resourceBundle;
    private final UserConfig userConfig = new UserConfig();

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Entry point for the JavaFX application. Initializes the application and sets
     * up the primary stage.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they
     *                     will not be
     *                     primary stages.
     * @throws IOException If an error occurs while loading the FXML files.
     */
    @Override
    public void start(Stage primaryStage) throws IOException, NotFoundException, BackingStoreException {
        var addExpense = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var addTag = FXML.load(AddTagCtrl.class, "client", "scenes", "AddTag.fxml");
        var contactDetails = FXML.load(ContactDetailsCtrl.class, "client", "scenes", "ContactDetails.fxml");
        var invitation = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var openDebts = FXML.load(OpenDebtsCtrl.class, "client", "scenes", "OpenDebts.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes", "Statistics.fxml");
        var startScreen = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var eventOverview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var adminEventOverview = FXML.load(AdminEventOverviewCtrl.class, "client", "scenes", "AdminEventOverview.fxml");
        var settingsPage = FXML.load(SettingsPageCtrl.class, "client", "scenes", "SettingsPage.fxml");

        switchLocale("messages", userConfig.getLanguageConfig());

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, addExpense, contactDetails,
                invitation, openDebts, statistics, startScreen, eventOverview, adminEventOverview, addTag,
                settingsPage);
        primaryStage.setOnCloseRequest((e)->{
            adminEventOverview.getKey().stop();
        });
    }

    /**
     * Loads the language bundle when the application is initialized.
     *
     * @param languageCode The language code of the persisted language.
     * @throws NotFoundException Exception is thrown when the language file does not
     *                           exist.
     */
    public static void loadLanguageBundle(String languageCode) throws NotFoundException {
        Locale locale = new Locale(languageCode);
        String bundleName = "messages_" + locale;
        URL url = Main.class.getClassLoader().getResource(bundleName + ".properties");
        if (url != null) {
            resourceBundle = ResourceBundle.getBundle(bundleName, locale);
        } else {
            throw new NotFoundException("File not found for language: " + languageCode);
        }
    }

    /**
     * returns the resource bundle of the language.
     *
     * @return the language bundle.
     */
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Looks at those properties files and fetches the appropriate thing
     * 
     * @param key key of an element of the bundle
     * @return appropriate string
     */
    public static String getLocalizedString(String key) {
        if (resourceBundle != null && resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            // Handle missing key (e.g., return default text or log a warning)
            return "**Missing Key: " + key + "**";
        }
    }

    /**
     * Requires a localization string like nl, en, es etc
     * 
     * @param languageCode the language code
     */
    public static void switchLocale(String baseName, String languageCode) throws BackingStoreException {
        if (languageCode != null) {
            UserConfig uc = new UserConfig();
            uc.setLanguageConfig(languageCode);
            Locale locale = new Locale(languageCode);
            resourceBundle = ResourceBundle.getBundle(baseName, locale);
        } else {
            resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
        }
        updateUILanguage();
    }

    /**
     * Jam an interface into every controller, im sure this will go smoothly
     */
    public static void updateUILanguage() {
        List<UpdatableUI> controllers = Arrays.asList(
                INJECTOR.getInstance(AddExpenseCtrl.class),
                INJECTOR.getInstance(ContactDetailsCtrl.class),
                INJECTOR.getInstance(InvitationCtrl.class),
                INJECTOR.getInstance(OpenDebtsCtrl.class),
                INJECTOR.getInstance(OverviewCtrl.class),
                INJECTOR.getInstance(StartScreenCtrl.class),
                INJECTOR.getInstance(StatisticsCtrl.class),
                INJECTOR.getInstance(SettingsPageCtrl.class),
                INJECTOR.getInstance(AdminEventOverviewCtrl.class));
        for (UpdatableUI controller : controllers) {
            controller.updateUI();
        }
    }

    /**
     * The interface in question, the method just fetches the other language string
     */
    public interface UpdatableUI {
        void updateUI();
    }

}
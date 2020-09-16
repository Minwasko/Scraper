import belita.Scraper;
import belkosmex.ScraperOne;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {

    private static final int WINDOW_SIZE = 600;
    private Scraper belitaScraper = new Scraper();
    private ScraperOne belkosmexScraper = new ScraperOne();
    private boolean scrapingBelita, startedScraping, englishLanguageChosen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Adding text
        // hello
        StackPane root = new StackPane();
        Scene scene = new Scene(root, WINDOW_SIZE + 200, WINDOW_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
        Label title = new Label("        Hello! I am your scraper, \nplease choose a website you want me to work on");
        title.setFont(Font.font(30));
        title.setTranslateX(0);
        title.setTranslateY(-200);
        root.getChildren().add(title);
        // language choice
        Label language = new Label("Now choose a language you want scraped");
        language.setFont(Font.font(20));
        language.setTranslateX(0);
        language.setTranslateY(-30);
        root.getChildren().add(language);
        // result
        Label result = new Label("Please dont leave some button not pressed, I refuse to work in that case\n" +
                "If everything is ok, then I ll get going with this button");
        result.setFont(Font.font(20));
        result.setTranslateX(0);
        result.setTranslateY(130);
        root.getChildren().add(result);
        // Buttons
        // site names
        // belita
        Button belita = new Button("Belita");
        belita.setTranslateX(-250);
        belita.setTranslateY(-100);
        belita.setScaleX(2);
        belita.setScaleY(2);
        DropShadow shadow = new DropShadow();
        root.getChildren().add(belita);
        // belkosmex
        Button belkosmex = new Button("Belkosmex");
        belkosmex.setTranslateX(200);
        belkosmex.setTranslateY(-100);
        belkosmex.setScaleX(2);
        belkosmex.setScaleY(2);
        root.getChildren().add(belkosmex);
        // Website event handlers
        belita.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    belita.setEffect(shadow);
                    scrapingBelita = true;
                    if (belkosmex.getEffect() != null) {
                        belkosmex.setEffect(null);
                    }
                });
        belkosmex.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    belkosmex.setEffect(shadow);
                    scrapingBelita = false;
                    if (belita.getEffect() != null) {
                        belita.setEffect(null);
                    }
                });
        // Adding language buttons
        // Russian
        Button russian = new Button("Russian");
        russian.setTranslateX(-250);
        russian.setTranslateY(50);
        russian.setScaleX(2);
        russian.setScaleY(2);
        root.getChildren().add(russian);
        // English
        Button english = new Button("English");
        english.setTranslateX(200);
        english.setTranslateY(50);
        english.setScaleX(2);
        english.setScaleY(2);
        // Language event handlers
        english.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    english.setEffect(shadow);
                    englishLanguageChosen = true;
                    if (russian.getEffect() != null) {
                        russian.setEffect(null);
                    }
                });
        russian.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    russian.setEffect(shadow);
                    englishLanguageChosen = false;
                    if (english.getEffect() != null) {
                        english.setEffect(null);
                    }
                });
        root.getChildren().add(english);
        // START BUTTON
        Button startButton = new Button("Start scraping!");
        startButton.setTranslateX(0);
        startButton.setTranslateY(200);
        startButton.setScaleX(2);
        startButton.setScaleY(2);
        root.getChildren().add(startButton);
        // loading text
        Label loadingText = new Label();
        int loadingPercent = 10;
        loadingText.setText("Loading: " + loadingPercent + "%");
        loadingText.setFont(Font.font(20));
        loadingText.setTranslateX(-20);
        loadingText.setTranslateY(250);
        root.getChildren().add(loadingText);
        // start button event handler
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
            if (!startedScraping) {
                if (englishLanguageChosen) {
                    if (scrapingBelita) {
                        belitaScraper.setLanguage("English");
                        try {
                            belitaScraper.getAllLines();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        belkosmexScraper.setLanguage("English");
                        try {
                            belkosmexScraper.getAllLines();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    if (scrapingBelita) {
                        belitaScraper.setLanguage("Russian");
                        try {
                            belitaScraper.getAllLines();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        belkosmexScraper.setLanguage("Russian");
                        try {
                            belkosmexScraper.getAllLines();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            startedScraping = true;
                    });
    }
}

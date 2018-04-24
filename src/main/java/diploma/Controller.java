package diploma;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private TextArea textArea;
    @FXML
    private Button searchBt;
    @FXML
    private TextFlow explanationText;
    @FXML
    private TextFlow convertedText;
    @FXML
    private Button clearBt;
    @FXML
    private Menu addFile;
    @FXML
    private Menu help;
    @FXML
    private AnchorPane pane;

    private List<WordEntity> parsedWords;

    private static final String HELP = "Welcome to the \"Terms Extraction\" program. \n" +
            "This is effective and easy-to-use program that would be useful in learning the theory of software testing.\n" +
            "\n" +
            "How to use the Terms Extraction program:\n" +
            "Type any combination of words or text. Also you can use articles. Click on the option \"File\" and choose the txt-file in folder.\n" +
            "Click on the \"Search\" button and  you will be able to see the result. \n" +
            "The terms extracted from texts using glossaries are highlighted in blue. In this case you could read term definitions (in \"The Glossary Defiitions\" field). \n" +
            "The terms / term candidates extracted from texts using terms' peculairities (not stored in glossaries) are highlighted in green.\n" +
            "Click on the \"Clear\" button to clear all fields.\n" +
            "\n" +
            "We are doing our best to offer you quality results.\n" +
            "However, there can still be some inaccurate terms. \n" +
            "We apologize for the inconvenience.";

    @FXML
    private void initialize() {
//        DBConnection.insert();

        Label helpLabel = new Label("Help");
        helpLabel.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Help");
            alert.setHeaderText(null);
            alert.setContentText(HELP);
            alert.show();
        });
        help.setGraphic(helpLabel);

        Label addLabel = new Label("Add file");
        addLabel.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(pane.getScene().getWindow());

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }

                onClearButtonClicked(null);
                textArea.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addFile.setGraphic(addLabel);

        Platform.runLater(() -> textArea.requestFocus());
    }

    private void findQuotes() {
        for (int i = 0; i < parsedWords.size(); i++) {
            if (!parsedWords.get(i).isUsed()) {
                Matcher m = Pattern.compile("\".+?\"").matcher(((Text) parsedWords.get(i).node).getText());

                if (m.find()) {
                    String s = ((Text) parsedWords.get(i).node).getText();
                    if (m.group().split(" ").length <= 3) {
                        parsedWords.set(i, new WordEntity(false, new Text(s.substring(0, m.start()))));
                        Text foundText = new Text();
                        foundText.setFill(Paint.valueOf("#008000"));
                        foundText.setText(m.group());
                        parsedWords.add(i + 1, new WordEntity(true, foundText));
                        parsedWords.add(i + 2, new WordEntity(false, new Text(s.substring(m.end()))));
                        i = 0;
                    }
                }
            }
        }
    }

    private void findAbbreviations() {
        for (int i = 0; i < parsedWords.size(); i++) {
            if (!parsedWords.get(i).isUsed()) {
                String text = ((Text) parsedWords.get(i).node).getText();
                Matcher m = Pattern.compile("\\([A-Z]{2,}\\)").matcher(text);

                if (m.find()) {
                    String abbr = m.group();
                    int abbrLength = abbr.length() - 2;
                    int startAbbr = m.start();
                    if (text.charAt(startAbbr - 1) == ' ') {
                        startAbbr -= 1;
                    }

                    for (int j = abbrLength; j > 0; j--) {
                        startAbbr = text.lastIndexOf(' ', startAbbr - 1);
                    }

                    parsedWords.set(i, new WordEntity(false, new Text(text.substring(0, startAbbr))));
                    Text foundText = new Text();
                    foundText.setFill(Paint.valueOf("#008000"));
                    foundText.setText(text.substring(startAbbr, m.end()));
                    parsedWords.add(i + 1, new WordEntity(true, foundText));
                    parsedWords.add(i + 2, new WordEntity(false, new Text(text.substring(m.end()))));
                    i = 0;
                }
            }
        }
    }

    private void findTerms() {
        parsedWords = new ArrayList<>();
        parsedWords.add(new WordEntity(false, new Text(textArea.getText())));

        List<DefinitionEntry> found = DBConnection.select();
        found.sort(DefinitionEntry::compareTo);
        for (DefinitionEntry entry : found) {

            for (int i = 0; i < parsedWords.size(); i++) {
                if (!parsedWords.get(i).isUsed()) {
                    Matcher m = Pattern.compile("(?i)" + entry.getDefinition()).matcher(((Text) parsedWords.get(i).node).getText());

                    if (m.find()) {
                        String s = ((Text) parsedWords.get(i).node).getText();
                        if (!Character.isLetterOrDigit(s.charAt(m.end()))) {
                            parsedWords.set(i, new WordEntity(false, new Text(s.substring(0, m.start()))));
                            parsedWords.add(i + 1, new WordEntity(true, createLink(m.group(), entry)));
                            parsedWords.add(i + 2, new WordEntity(false, new Text(s.substring(m.end()))));
                            i = 0;
                        }
                    }
                }
            }
        }
    }

    private Hyperlink createLink(String linkText, DefinitionEntry foundValue) {
        Hyperlink link = new Hyperlink(linkText + " ");
        link.setPadding(new Insets(0));
        link.setBorder(Border.EMPTY);
        link.setOnAction(event -> {
            explanationText.getChildren().clear();
            explanationText.getChildren().add(
                    new Text(foundValue.getDefinition() + " - " + foundValue.getExplanation()));
        });
        return link;
    }

    private void printResult() {
        convertedText.getChildren().clear();
        for (WordEntity parsedWord : parsedWords) {
            convertedText.getChildren().add(parsedWord.node);
        }
    }

    public void onClearButtonClicked(ActionEvent actionEvent) {
        textArea.clear();
        explanationText.getChildren().clear();
        convertedText.getChildren().clear();
    }

    public void onSearchClicked(ActionEvent actionEvent) {
        findTerms();
        findQuotes();
        findAbbreviations();

        printResult();
    }

}

package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;


public class Main extends Application {

    static GameControllerImpl gameController = new GameControllerImpl();

    private Integer iteration = 0;

    private Button btn;
    private Button startBtn;
    private TextField userTextField;
    private TextField yourGuessText;
    private TextField yourGuessPoint;
    private TextField computerPointBox;
    private Text iterationText;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("CsTech Tahmin Oyunu");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 5, 25, 5));

        Scene scene = new Scene(gridPane, 750, 275);

        scene.getStylesheets().add
                (Main.class.getResource("game.css").toExternalForm());


        Text scenetitle = new Text("Sayıyı Tahmin Et");
        scenetitle.setId("welcome-text");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(scenetitle, 0, 0, 2, 1);


        iterationText = new Text();
        iterationText.setFill(Color.FIREBRICK);
        gridPane.add(iterationText, 2, 4);

        Label userName = new Label("Bilgisayar Tahmini:");
        gridPane.add(userName, 0, 1);
        userTextField = new TextField();
        userTextField.setDisable(true);
        gridPane.add(userTextField, 1, 1);

        Label yourGuess = new Label("Senin Tahminin:");
        gridPane.add(yourGuess, 2, 1);
        yourGuessText = new TextField();
        gridPane.add(yourGuessText, 3, 1);


        Label pw = new Label("Bilgisayar Skoru:");
        gridPane.add(pw, 0, 2);
        computerPointBox = new TextField();
        //  ComboBox comboBox = new ComboBox();
        //  comboBox.getItems().addAll("0,0","0,1","0,2","0,3","0,4","1,0","1,1","1,2","1,3","2,0");

        //    gridPane.add(comboBox, 1, 2);
        gridPane.add(computerPointBox, 1, 2);


        Label yourPoint = new Label("Senin Skorun:");
        gridPane.add(yourPoint, 2, 2);
        yourGuessPoint = new TextField();
        yourGuessPoint.setDisable(true);
        gridPane.add(yourGuessPoint, 3, 2);

        btn = new Button("Cevabı Gönder");
        btn.setDisable(true);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridPane.add(hbBtn, 3, 4);

        startBtn = new Button("Oyunu Başlat");
        HBox startHBtn = new HBox(10);
        startHBtn.setAlignment(Pos.BOTTOM_RIGHT);
        startHBtn.getChildren().add(startBtn);
        gridPane.add(startHBtn, 1, 4);

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    btn.setDisable(false);
                    startBtn.setDisable(true);
                    String compGuessNumber = gameController.newGame();
                    userTextField.setText(compGuessNumber);
                    iteration = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final Text actiontarget = new Text();
        gridPane.add(actiontarget, 1, 6);


        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {


                //   String computerPoint = (String) comboBox.getValue();
                String computerPoint = computerPointBox.getText();
                String yourGuess = yourGuessText.getText();
                String compGuess = userTextField.getText();


                //    actiontarget.setFill(Color.FIREBRICK);


                if (!gameController.checkUserGuess(yourGuess)) {
                    showAlert(Alert.AlertType.WARNING, "Uyarı", "Tahmininiz uygun değil", "Tahmin 4 basamaktan oluşan bir sayı olmalı.");
                } else if (!validatePoint(computerPoint)) {
                    showAlert(Alert.AlertType.WARNING, "Uyarı", "Skorlarda bir sorun var. Lutfen kontrol ederek oynayiniz.!", "Skor {[0-4],[0-4]} formatında olmalı");
                } else {

                    try {
                        boolean succeed = gameController.checkNewPossibleList(computerPoint, compGuess);
                        if (succeed) {
                            cleanFields();
                            showAlert(Alert.AlertType.INFORMATION, "Bilgilendirme", "Ben Kazandım!", "Senin numaran : " + gameController.guessNewNumber());
                        } else {
                            String guessedNewNumber = gameController.guessNewNumber();
                            userTextField.setText(guessedNewNumber);
                            String userScore = gameController.calculateUserScore(yourGuess);
                            if (userScore.equals("4,0")) {
                                cleanFields();
                                showAlert(Alert.AlertType.INFORMATION, "Bilgilendirme", "Sen Kazandın!", "Benim numaram : " + yourGuess);
                            }
                            yourGuessPoint.setText(userScore);
                            computerPointBox.setText(null);
                            yourGuessText.setText(null);

                            iterationText.setText(String.valueOf(++iteration));
                        }
                    } catch (Exception ex) {
                        showAlert(Alert.AlertType.WARNING, "Uyarı", "Skorlarda bir sorun var.Lütfen yeniden oynayınız", null);
                        cleanFields();
                    }
                }
            }
        });

        primaryStage.setScene(scene);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void cleanFields() {
        startBtn.setDisable(false);
        btn.setDisable(true);
        userTextField.setText(null);
        yourGuessText.setText(null);
        computerPointBox.setText(null);
        yourGuessPoint.setText(null);
        iterationText.setText(null);

    }

    public boolean validatePoint(String point) {
        String regex = "^([0-4],[0-4])$";
        return Pattern.matches(regex, point);
    }

    public void showAlert(Alert.AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.show();
    }


}

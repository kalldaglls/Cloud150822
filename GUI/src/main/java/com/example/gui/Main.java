package com.example.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Main {

//    @FXML
//    TableView<FileInfo> filesTable;
//
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to MY Dropbox!");
//    }

    @FXML
    VBox leftpanel, rightpanel;

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyAct(ActionEvent actionEvent) {
        PanelController lefPC = (PanelController) leftpanel.getProperties().get("ctrl");
        PanelController rigPC = (PanelController) rightpanel.getProperties().get("ctrl");

        if (lefPC.getSelectedFilename() == null && rigPC.getSelectedFilename() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "BOOM!", ButtonType.OK);
            alert.showAndWait();
            return;
            }
        PanelController srcPC = null, dstPC = null;
        if (lefPC.getSelectedFilename() != null){
            srcPC = lefPC;
            dstPC = rigPC;
        }

        if (rigPC.getSelectedFilename() != null){
            srcPC = rigPC;
            dstPC = lefPC;
        }

        Path srcPath = Paths.get(srcPC.getCurPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurPath()).resolve(srcPath.getFileName().toString());

        try {
            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurPath()));
        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "TIC!", ButtonType.OK);
            alert.showAndWait();
        }

    }


//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        TableColumn<FileInfo,String> fileTypeColumn = new TableColumn<>();//34:00 Здесь храним тип файла
//        //35:30 Создаем один столбец
//        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
//        fileTypeColumn.setPrefWidth(24);
//
//        TableColumn<FileInfo,String> fileNameColumn = new TableColumn<>("Имя");
//        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
//        fileNameColumn.setPrefWidth(240);
//
//        TableColumn<FileInfo,Long> fileSizeColumn = new TableColumn<>("Размер файла");
//        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
//        fileSizeColumn.setPrefWidth(240);
//        fileSizeColumn.setCellFactory(column -> {
//            return new TableCell<FileInfo,Long>() {
//                @Override
//                protected void updateItem(Long item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty){
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        String text = String.format("%,d bytes", item);
//                        if(item == -1L) {
//                            text = "[DIR]";
//                        }
//                        setText(text);
//                    }
//                }
//            };
//        });
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        TableColumn<FileInfo,String> fileTimeColumn = new TableColumn<>("Дата изменения");
//        fileTimeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
//        fileTimeColumn.setPrefWidth(240);
//
//        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileTimeColumn);
//        filesTable.getSortOrder().add(fileTypeColumn);
//
//        updateList(Paths.get("."));
//    }
//
//    public void updateList(Path path) {
//        try {
//            filesTable.getItems().clear();
//            filesTable.getItems().addAll(Files.list(path).map(FileInfo :: new).collect(Collectors.toList()));
//            filesTable.sort();
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.WARNING,
//                "Не удалось получить список файлов", ButtonType.OK);
//            alert.showAndWait();
//        }
//    }
}
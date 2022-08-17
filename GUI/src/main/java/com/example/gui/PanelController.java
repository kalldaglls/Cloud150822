package com.example.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {
    @FXML
    TableView<FileInfo> filesTable;

    @FXML
    private Label welcomeText;

    @FXML
    private ComboBox<String> disksBox;

    @FXML
    private TextField pathField;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to MY Dropbox!");
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<FileInfo,String> fileTypeColumn = new TableColumn<>();//34:00 Здесь храним тип файла
        //35:30 Создаем один столбец
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo,String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfo,Long> fileSizeColumn = new TableColumn<>("Размер файла");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(240);
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo,Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if(item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo,String> fileTimeColumn = new TableColumn<>("Дата изменения");
        fileTimeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileTimeColumn.setPrefWidth(240);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileTimeColumn);
        filesTable.getSortOrder().add(fileTypeColumn);

        disksBox.getItems().clear();
        for (Path p: FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }

        disksBox.getSelectionModel().select(0);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2){
                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getFileName());
                    if (Files.isDirectory(path)){
                        updateList(path);
                    }
                }
            }
        });

        updateList(Paths.get("."));
    }

    public void updateList(Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
            filesTable.getItems().addAll(Files.list(path).map(FileInfo :: new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Не удалось получить список файлов", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upPath = Paths.get(pathField.getText()).getParent();
        if (upPath != null){
            updateList(upPath);
        }
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> el = (ComboBox<String>) actionEvent.getSource();
        updateList(Paths.get(el.getSelectionModel().getSelectedItem()));
    }

    public String getSelectedFilename(){
        if (!filesTable.isFocused()){
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }

    public String getCurPath(){
        return pathField.getText();
    }
}

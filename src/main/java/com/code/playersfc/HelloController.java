package com.code.playersfc;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import logica.Playback;
import logica.Playlist;
import logica.Video;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


    @FXML
    private Button abrir;

    @FXML
    private Button cerrar;

    @FXML
    private Button play;

    @FXML
    private Button stop;

    @FXML
    private Button btnBajar;

    @FXML
    private Button btnSubir;

    @FXML
    private ListView<Video> lisView;

    @FXML
    private MediaView mediaView;

    @FXML
    private TextArea txtArea;

    Playlist playlist = new Playlist("mi lista");
    Playback playback = new Playback(playlist);
    Video currentVideo;
    Media media;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;


    public void setupButtonsAction(){

        //file open button
        abrir.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.mp4"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

            if (selectedFiles != null) {
                for(File i: selectedFiles){
                    System.out.println("Selected file: " + i.getAbsolutePath());
                    playlist.adicionarVideo(new Video(i.getName(), i.getAbsolutePath()));
                }
            } else {
                System.out.println("File selection canceled.");
            }
        });

        //close file
        cerrar.setOnAction(event -> {
            Video video = lisView.getSelectionModel().getSelectedItem();
            if(playlist.getSize() > 1 && video != null){
                playlist.removerVideo(video);
            }
        });

        play.setOnAction(event -> {
            if(!playlist.isEmpty()){
                if(currentVideo == null) loadVideo();
                mediaPlayer.play();
            }
        });

        stop.setOnAction(event -> {
            mediaPlayer.stop();
        });
    }

    private void setupUI(){
        lisView.setItems(playlist.getVideos());
    }

    private void setupPlayerListeners(){
        // Listener para cuando termina la reproducción
        mediaPlayer.setOnEndOfMedia(() -> {
            nextVideo();
            mediaPlayer.play();
        });

    }

    private void nextVideo(){
        currentVideo = playback.nextVideo();
        loadVideo();
    }

    void loadVideo(){
        if(currentVideo == null){
            currentVideo = playback.nextVideo();
            lisView.getSelectionModel().select(currentVideo);
        }
        media = new Media(new File(currentVideo.getRuta()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        setupPlayerListeners();
        mediaView.setMediaPlayer(mediaPlayer);
    }

    private void togglePlay(){
        if(isPlaying){
            mediaPlayer.pause();
            isPlaying = true;
        }else{
            mediaPlayer.play();
            isPlaying = false;
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Controller initialized!");
        setupButtonsAction();
        setupUI();
    }
}
package com.code.playersfc;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
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
    private Label lblTime;

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
            if(video != null){
                playlist.removerVideo(video);
            }
        });

        play.setOnAction(event -> {
            if(!playlist.isEmpty()){
                if(currentVideo == null) {
                    currentVideo = playback.nextVideo();
                    lisView.getSelectionModel().select(currentVideo);
                }
                media = new Media(new File(currentVideo.getRuta()).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                setupPlayerListeners();
                mediaPlayer.play();
                mediaView.setMediaPlayer(mediaPlayer);
            }
        });

        stop.setOnAction(event -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
            }
        });
    }

    public void pause(){
        if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();
        }
    }

    private void setupUI(){
        lisView.setItems(playlist.getVideos());

    }

    private void setupPlayerListeners(){
        // Listener para cuando termina la reproducción
        mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
            // Handle status changes
            switch (newStatus) {
                case READY:
                    // Media is ready to play
                    System.out.println("Media ready");
                    break;
                case PLAYING:
                    // Media is currently playing
                    System.out.println("Media playing");
                    break;
                case PAUSED:
                    // Media is paused
                    System.out.println("Media paused");
                    break;
                case STOPPED:
                    // Media is stopped
                    System.out.println("Media stopped");
                    break;
                case STALLED:
                    // Media playback has stalled
                    System.out.println("Media stalled");
                    break;
                case HALTED:
                    // Media playback has halted due to error
                    System.out.println("Media halted");
                    break;
                case UNKNOWN:
                    // Status is unknown
                    System.out.println("Media status unknown");
                    break;
                case DISPOSED:
                    // MediaPlayer has been disposed
                    System.out.println("Media player disposed");
                    break;
            }

        });

        // Listener para imprimir los segundos transcurridos
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            double segundosTranscurridos = newTime.toSeconds();
            double duracionTotal = mediaPlayer.getTotalDuration().toSeconds();

            System.out.printf("%.2f/%.2f segundos (%.1f%%)%n",
                    segundosTranscurridos,
                    duracionTotal,
                    (segundosTranscurridos/duracionTotal) * 100);
        });

        // Crear un binding que se actualice automáticamente
        StringBinding timeBinding = Bindings.createStringBinding(() -> {
            Duration current = mediaPlayer.getCurrentTime();
            Duration total = mediaPlayer.getTotalDuration();

            if (current == null || total == null) {
                return "00:00:00 / 00:00:00";
            }

            return formatearTiempo(current) + " / " + formatearTiempo(total);
        }, mediaPlayer.currentTimeProperty(), mediaPlayer.totalDurationProperty());

        lblTime.textProperty().bind(timeBinding);
        // Listener cuando termina el video
        mediaPlayer.setOnEndOfMedia(this::nextVideo);
    }

    private void nextVideo(){
        currentVideo = playback.nextVideo();
        lisView.getSelectionModel().select(playlist.getIndiceActual());
        media = new Media(new File(currentVideo.getRuta()).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        setupPlayerListeners();
        mediaPlayer.play();
        mediaView.setMediaPlayer(mediaPlayer);
    }

    void loadVideo(){
        if(currentVideo == null){

        }

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
        if(mediaPlayer != null){
            txtArea.setText(txtArea.getText() + "\n estado player: "+mediaPlayer.getStatus());
        }

    }

    private String formatearTiempo(Duration duration) {
        if (duration == null || duration.isUnknown()) {
            return "00:00:00";
        }

        int totalSegundos = (int) duration.toSeconds();
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;

        // Si el video dura menos de 1 hora, mostrar solo MM:SS
        if (horas == 0) {
            return String.format("%02d:%02d", minutos, segundos);
        } else {
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }
    }
}
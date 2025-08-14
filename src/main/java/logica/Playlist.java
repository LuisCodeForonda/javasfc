package logica;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;

public class Playlist  {

    private String nombre;
    private ObservableList<Video> lista;
    private int indiceActual;

    public Playlist(String nombre){
        this.nombre = nombre;
        lista = FXCollections.observableArrayList();
        indiceActual = -1;
    }

    public void adicionarVideo(Video video){
        lista.add(video);
    }

    public void removerVideo(Video video){
        lista.remove(video);
    }

    public Video getActualVideo(){
        if(lista.isEmpty()) return null;
        return lista.get(indiceActual);
    }

    public Video getSiguienteVideo(){
        if(lista.isEmpty()) return null;
        indiceActual = (indiceActual + 1) % lista.size();
        return lista.get(indiceActual);
    }

    public Video getPreviousVideo(){
        if(lista.isEmpty()) return null;
        indiceActual = (indiceActual -1 + lista.size()) % lista.size();
        return lista.get(indiceActual);
    }

    public ObservableList<Video> getVideos() {
        return FXCollections.unmodifiableObservableList(lista);
    }

    public void moveVideoUp(int index) {
        if (index > 0 && index < lista.size()) {
            Collections.swap(lista, index, index - 1);
        }
    }

    public void moveVideoDown(int index) {
        if (index >= 0 && index < lista.size() - 1) {
            Collections.swap(lista, index, index + 1);
        }
    }

    public void shuffle() {
        FXCollections.shuffle(lista);
    }

    public boolean isEmpty(){
        return lista.isEmpty();
    }

    public int getIndiceActual(){
        return indiceActual;
    }
}

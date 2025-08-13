package logica;

public class Playback {

    public enum Mode {SINGLE, SECUENCIAL, LOOP, ALEATORIO};
    Playlist playlist;
    Mode mode;
    Video actual;

    public Playback(Playlist playlist){
        this.playlist = playlist;
        mode = Mode.SECUENCIAL;
    }

    public Video nextVideo(){
        switch (mode){
            case SINGLE:
                return playlist.getActualVideo();
            case SECUENCIAL:
                return playlist.getSiguienteVideo();
            case LOOP:
                return playlist.getSiguienteVideo();
            case ALEATORIO:
                break;
        }
        return null;
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }
}

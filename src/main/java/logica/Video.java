package logica;

public class Video {

    String titulo;
    String ruta;

    public Video(String titulo, String ruta) {
        this.titulo = titulo;
        this.ruta = ruta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return titulo;
    }
}

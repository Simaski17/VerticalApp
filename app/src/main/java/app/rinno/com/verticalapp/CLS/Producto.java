package app.rinno.com.verticalapp.CLS;

/**
 * Created by Dev21 on 18-09-16.
 */
public class Producto {
    private int id;
    private String url;
    private String modelo;
    private String nombre;


    public Producto(int id, String nombre, String modelo, String url) {

        this.id =  id;
        this.url = url;
        this.nombre =  nombre;
        this.modelo =  modelo;

    }

    public String getUrl() {
        return url;
    }
    public String getNombre() {
        return nombre;
    }
    public String getModelo() {
        return modelo;
    }


    public int getId() {
        return id;
    }

}

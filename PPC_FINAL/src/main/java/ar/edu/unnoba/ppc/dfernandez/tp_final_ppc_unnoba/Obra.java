package ar.edu.unnoba.ppc.dfernandez.tp_final_ppc_unnoba;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Obra{
    @PrimaryKey(autoGenerate = true)
    private int oid;
    private String descripcion;
    private String detalle;
    private Double distancia;
    private String domicilio;
    private Double latitud;
    private Double longitud;
    private Long telefono;
    static final String TIPO = "OBRA";
    private Double valor;
    //una referencia a un recurso de imagen, tecnicamente deberia ser un enlace a un recurso web contenido en el json que provee el web service
    private int referenceImage;

    @Ignore
    public Obra(){};

    public Obra(String descripcion, String detalle, Double distancia, String domicilio, Double latitud, Double longitud, Long telefono, Double valor) {
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.distancia = distancia;
        this.domicilio = domicilio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.telefono = telefono;
        this.valor = valor;
    }


    //<editor-fold desc="Getters y Setters">
    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getOid() { return oid; }

    public void setOid(int oid) { this.oid = oid; }

    public static String getTIPO() { return TIPO; }

    public int getReferenceImage() { return referenceImage; }

    public void setReferenceImage(int referenceImage) { this.referenceImage = referenceImage; }

    //</editor-fold>

    @Override
    public String toString() {
        return "Obra{" +
                "descripcion='" + descripcion + '\'' +
                ", detalle='" + detalle + '\'' +
                ", distancia=" + distancia +
                ", domicilio='" + domicilio + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", telefono=" + telefono +
                ", valor=" + valor +
                '}';
    }


}

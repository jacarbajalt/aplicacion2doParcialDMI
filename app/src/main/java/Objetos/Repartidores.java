package Objetos;

public class Repartidores {
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String correoRep;
    private String passwordRep;
    private String fechaDeNacimiento;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getCorreoRep() {
        return correoRep;
    }

    public void setCorreoRep(String correoRep) {
        this.correoRep = correoRep;
    }

    public String getPasswordRep() {
        return passwordRep;
    }

    public void setPasswordRep(String passwordRep) {
        this.passwordRep = passwordRep;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Repartidores(){
        this.nombre="";
        this.apellidoP="";
        this.apellidoM="";
        this.correoRep="";
        this.passwordRep="";
        this.fechaDeNacimiento="";
    }

    public Repartidores(String n, String aPa, String aMa, String c, String p, String fN){
        this.nombre=n;
        this.apellidoP=aPa;
        this.apellidoM=aMa;
        this.correoRep=c;
        this.passwordRep=p;
        this.fechaDeNacimiento=fN;
    }
}

package Objetos;

public class Usuarios {
    private String nombre;
    private String apPat;
    private String apMat;
    private String correo;
    private String usuario;
    private String contra;
    private String fechaNac;

    public Usuarios(){
        this.nombre="";
        this.apPat="";
        this.apMat="";
        this.correo="";
        this.usuario="";
        this.contra="";
        this.fechaNac="";
    }
    public Usuarios(String n, String ap, String am, String cor, String u,String con, String fn){
        this.nombre=n;
        this.apPat=ap;
        this.apMat=am;
        this.correo=cor;
        this.usuario=u;
        this.contra=con;
        this.fechaNac=fn;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPat() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public String getApMat() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }
}

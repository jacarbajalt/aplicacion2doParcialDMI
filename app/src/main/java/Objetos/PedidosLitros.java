package Objetos;

public class PedidosLitros {

    private boolean ciliOesta;
    private int litrosPedidos;
    private double preciodelGas;
    private boolean efectOTarje;

    public boolean isCiliOesta() {
        return ciliOesta;
    }

    public void setCiliOesta(boolean ciliOesta) {
        this.ciliOesta = ciliOesta;
    }

    public int getLitrosPedidos() {
        return litrosPedidos;
    }

    public void setLitrosPedidos(int litrosPedidos) {
        this.litrosPedidos = litrosPedidos;
    }

    public double getPreciodelGas() {
        return preciodelGas;
    }

    public void setPreciodelGas(double preciodelGas) {
        this.preciodelGas = preciodelGas;
    }

    public boolean isEfectOTarje() {
        return efectOTarje;
    }

    public void setEfectOTarje(boolean efectOTarje) {
        this.efectOTarje = efectOTarje;
    }

    public PedidosLitros(){

        this.ciliOesta=false;
        this.litrosPedidos=0;
        this.preciodelGas=0.0;
        this.efectOTarje=false;
    }
    public PedidosLitros(boolean cOe, int lGas, double pGas, boolean eOt){
        this.ciliOesta=cOe;
        this.litrosPedidos=lGas;
        this.preciodelGas=pGas;
        this.efectOTarje=eOt;
    }

    public PedidosLitros(boolean cOe, int lGas, boolean eOt){
        this.ciliOesta=cOe;
        this.litrosPedidos=lGas;
        this.efectOTarje=eOt;
    }
    public String informacionActual() {
        String res = "";
        res = "Pediste: "+this.litrosPedidos+" litros "+"\nTotal: "+this.preciodelGas+" pesos";
        if(this.ciliOesta){
            res=res+"\nTu selección fue Cilindro";
        }else{
            res=res+"\nTu selección fue Estacionario";
        }
        if(this.efectOTarje){
            res=res+"\nTu método de pago es: Efectivo";
        }else{
            res=res+"\nTu método de pago es: Tarjeta";
        }
        return res;
    }
}

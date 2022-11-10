package Objetos;

public class PedidosEfectivo {
    private boolean cilOest;
    private int efectivoPedido;
    private boolean efecOTarj;


    public boolean isCilOest() {
        return cilOest;
    }

    public void setCilOest(boolean cilOest) {
        this.cilOest = cilOest;
    }

    public int getEfectivoPedido() {
        return efectivoPedido;
    }

    public void setEfectivoPedido(int efectivoPedido) {
        this.efectivoPedido = efectivoPedido;
    }

    public boolean isEfecOTarj() {
        return efecOTarj;
    }

    public void setEfecOTarj(boolean efecOTarj) {
        this.efecOTarj = efecOTarj;
    }

    public PedidosEfectivo(){
        this.cilOest=false;
        this.efectivoPedido=0;
        this.efecOTarj=false;
    }

    public PedidosEfectivo(boolean cOe, int eP, boolean eOt){
        this.cilOest=cOe;
        this.efectivoPedido=eP;
        this.efecOTarj=eOt;
    }
    public String informacionActual() {
        String res = "";
        res = "Pediste: "+this.efectivoPedido+" pesos ";
        if(this.cilOest){
            res=res+"\nTu selección fue Cilindro";
        }else{
            res=res+"\nTu selección fue Estacionario";
        }
        if(this.efecOTarj){
            res=res+"\nTu método de pago es: Efectivo";
        }else{
            res=res+"\nTu método de pago es: Tarjeta";
        }
        return res;
    }
}

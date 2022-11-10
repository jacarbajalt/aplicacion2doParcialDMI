package Objetos;

public class ClientBooking {
    String idHistoryBooking;
    String idClient;
    String idDriver;
    String origin;
    String status;
    double originLat;
    double originLng;

    public ClientBooking(){

    }

    public ClientBooking(String idClient, String idDriver, String origin, String status, double originLat, double originLng) {
        this.idClient = idClient;
        this.idDriver = idDriver;
        this.origin = origin;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
    }

    public ClientBooking(String idHistoryBooking, String idClient, String idDriver, String origin, String status, double originLat, double originLng) {
        this.idHistoryBooking=idHistoryBooking;
        this.idClient = idClient;
        this.idDriver = idDriver;
        this.origin = origin;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
    }

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }
}

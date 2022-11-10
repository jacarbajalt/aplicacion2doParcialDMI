package Objetos;

public class HistoryBooking {
    String idHistoryBooking;
    String idClient;
    String idDriver;
    String origin;
    String status;
    double originLat;
    double originLng;
    double calificationClient;
    double calificationDriver;
    long timestamp;

    public HistoryBooking(){

    }

    public HistoryBooking(String idHistoryBooking, String idClient, String idDriver, String origin, String status, double originLat, double originLng) {
        this.idHistoryBooking=idHistoryBooking;
        this.idClient = idClient;
        this.idDriver = idDriver;
        this.origin = origin;
        this.status = status;
        this.originLat = originLat;
        this.originLng = originLng;
    }

    public String getIdClient() {
        return idClient;
    }

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
    }

    public double getCalificationClient() {
        return calificationClient;
    }

    public void setCalificationClient(double calificationClient) {
        this.calificationClient = calificationClient;
    }

    public double getCalificationDriver() {
        return calificationDriver;
    }

    public void setCalificationDriver(double calificationDriver) {
        this.calificationDriver = calificationDriver;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

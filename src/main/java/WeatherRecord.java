import java.sql.Timestamp;

public class WeatherRecord {
    private int id;
    private String city;
    private float temperature;
    private Timestamp requestDate;

    public WeatherRecord(int id, String city, float temperature, Timestamp requestDate) {
        this.id = id;
        this.city = city;
        this.temperature = temperature;
        this.requestDate = requestDate;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public float getTemperature() {
        return temperature;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }
}
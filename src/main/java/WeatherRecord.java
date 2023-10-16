import java.sql.Timestamp;

/**
 * Класс для представления записи о погоде.
 */
public class WeatherRecord {
    private int id;
    private String city;
    private String CityIdentificator;
    private float temperature;
    private Timestamp requestDate;

    /**
     * Конструктор для создания новой записи о погоде без известного идентификатора города.
     * @param city Название города (или "Нет информации").
     * @param CityIdentificator Идентификатор города (или "Нет информации").
     * @param temperature Температура в городе.
     * @param requestDate Дата и время запроса погоды.
     */
    public WeatherRecord(String city, String CityIdentificator, float temperature, Timestamp requestDate) {
        this.city = city;
        this.CityIdentificator = CityIdentificator;
        this.temperature = temperature;
        this.requestDate = requestDate;
    }

    /**
     * Конструктор для создания новой записи о погоде с известным идентификатором города.
     * @param id Идентификатор записи.
     * @param city Название города (или "Нет информации").
     * @param CityIdentificator Идентификатор города (или "Нет информации").
     * @param temperature Температура в городе.
     * @param requestDate Дата и время запроса погоды.
     */
    public WeatherRecord(int id, String city, String CityIdentificator, float temperature, Timestamp requestDate) {
        this.id = id;
        this.city = city;
        this.CityIdentificator = CityIdentificator;
        this.temperature = temperature;
        this.requestDate = requestDate;
    }

    /**
     * Получает идентификатор записи о погоде.
     * @return Идентификатор записи.
     */
    public int getId() {
        return id;
    }

    /**
     * Получает название города.
     * @return Название города (или "Нет информации").
     */
    public String getCity() {
        return city;
    }

    /**
     * Получает идентификатор города.
     * @return Идентификатор города (или "Нет информации").
     */
    public String getCityIdentificator() {
        return CityIdentificator;
    }

    /**
     * Получает температуру в городе.
     * @return Температура в городе.
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * Получает дату и время запроса погоды.
     * @return Дата и время запроса погоды.
     */
    public Timestamp getRequestDate() {
        return requestDate;
    }
}
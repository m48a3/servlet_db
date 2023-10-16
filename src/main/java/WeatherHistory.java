import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с историей погоды, сохранения данных в базу и извлечения данных из базы.
 */
public class WeatherHistory {

    private DataSource dataSource;

    /**
     * Конструктор класса WeatherHistory.
     * @param dataSource Источник данных для работы с базой данных.
     */
    public WeatherHistory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Вставляет данные о погоде в базу данных.
     * @param city Название города (или "Нет информации").
     * @param cityId Идентификатор города.
     * @param temperature Температура в городе.
     */
    public void insertWeatherData(String city, String cityId, float temperature) {
        try {
            Connection connection = dataSource.getConnection();
            String insertQuery = "INSERT INTO weather_history (city, cityid, temperature, request_date) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, city);
            preparedStatement.setString(2, cityId);
            preparedStatement.setFloat(3, temperature);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Извлекает все записи о погоде из базы данных.
     * @return Список объектов WeatherRecord, представляющих записи о погоде.
     */
    public List<WeatherRecord> getAllWeatherData() {
        List<WeatherRecord> records = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            String selectQuery = "SELECT id, city, cityid, temperature, request_date FROM weather_history";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String city = resultSet.getString("city");
                String CityId = resultSet.getString("cityid");
                float temperature = resultSet.getFloat("temperature");
                Timestamp requestDate = resultSet.getTimestamp("request_date");

                WeatherRecord record = new WeatherRecord(id, city, CityId, temperature, requestDate);
                records.add(record);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }
}
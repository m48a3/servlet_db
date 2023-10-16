import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherHistory {

    private DataSource dataSource;

    public WeatherHistory(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/db_weather";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "080900";

    public void insertWeatherData(String cityid, float temperature) {
        try {
            Connection connection = dataSource.getConnection(); // Используйте dataSource
            String insertQuery = "INSERT INTO weather_history (cityid, temperature) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, cityid);
            preparedStatement.setFloat(2, temperature);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<WeatherRecord> getAllWeatherData() {
        List<WeatherRecord> records = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String selectQuery = "SELECT id, city,cityid, temperature, request_date FROM weather_history";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String city = resultSet.getString("city");
                String CityIdentificator = resultSet.getString("cityid");
                float temperature = resultSet.getFloat("temperature");
                Timestamp requestDate = resultSet.getTimestamp("request_date");

                WeatherRecord record = new WeatherRecord(id, city, CityIdentificator, temperature, requestDate);
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
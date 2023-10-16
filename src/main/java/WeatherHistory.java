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

    public void insertWeatherData(String city, float temperature) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            String insertQuery = "INSERT INTO weather_history (city, temperature, request_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, city);
            preparedStatement.setFloat(2, temperature);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
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
            String selectQuery = "SELECT id, city, temperature, request_date FROM weather_history";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String city = resultSet.getString("city");
                float temperature = resultSet.getFloat("temperature");
                Timestamp requestDate = resultSet.getTimestamp("request_date");

                WeatherRecord record = new WeatherRecord(id, city, temperature, requestDate);
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
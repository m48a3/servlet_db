import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/weather")
public class MainServlet extends HttpServlet {

    private WeatherHistory weatherHistory;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            DataSource dataSource = (DataSource) envContext.lookup("jdbc/db_weather");
            weatherHistory = new WeatherHistory(dataSource);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write("<form method='POST' action='/unnamed/weather'>");
        resp.getWriter().write("Введите ID города: <input type='text' name='cityId'>");
        resp.getWriter().write("<input type='submit' value='Получить погоду'>");
        resp.getWriter().write("</form>");

        // Отобразить таблицу с данными из базы данных (историю поиска)
        displayWeatherData(resp);
    }

    private void displayWeatherData(HttpServletResponse resp) throws IOException {
        List<WeatherRecord> records = weatherHistory.getAllWeatherData();

        resp.getWriter().write("<table>");
        resp.getWriter().write("<tr><th>ID</th><th>Город</th><th>ID Города</th><th>Температура</th><th>Время</th></tr>");
        for (WeatherRecord record : records) {
            resp.getWriter().write("<tr>");
            resp.getWriter().write("<td>" + record.getId() + "</td>");
            resp.getWriter().write("<td>" + record.getCity() + "</td>");
            resp.getWriter().write("<td>" + record.getCityIdentificator() + "</td>");
            resp.getWriter().write("<td>" + record.getTemperature() + "</td>");
            resp.getWriter().write("<td>" + record.getRequestDate() + "</td>");
            resp.getWriter().write("</tr>");
        }
        resp.getWriter().write("</table>");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cityId = req.getParameter("cityId");
        if (cityId != null && !cityId.isEmpty()) {
            // Получите данные о погоде для указанного ID города
            String temperature = fetchWeatherForCity(cityId);

            // Вставьте данные в базу данных
            if (temperature != null) {
                weatherHistory.insertWeatherData(cityId, Float.parseFloat(temperature));
            }
        }
        resp.sendRedirect("/unnamed/weather");
    }

    private String fetchWeatherForCity(String cityId) {
        OkHttpClient client = new OkHttpClient();
        String fetchApiKey = "ea60a55e-9bf3-485c-ad40-692d82f5b8ac"; // Замените на свой ключ

        try {
            String weatherUrl = "https://api.weather.yandex.ru/v2/forecast?geoid=" + cityId + "&lang=ru_RU&limit=1&hours=true&extra=false";
            Request request = new Request.Builder()
                    .url(weatherUrl)
                    .addHeader("X-Yandex-API-Key", fetchApiKey)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                String jsonResponse = responseBody.string();
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                String temperature = jsonObject.getAsJsonObject("fact")
                        .get("temp")
                        .getAsString();

                return temperature;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
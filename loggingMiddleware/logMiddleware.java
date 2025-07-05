package loggingMiddleware;

import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class logMiddleware {
    private static final String LOGGING_ENDPOINT = "http://20.244.56.144/evaluation-service/logs";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiIyMjE1MDEwNjVAcmFqYWxha3NobWkuZWR1LmluIiwiZXhwIjoxNzUxNjkzMDQxLCJpYXQiOjE3NTE2OTIxNDEsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiJiMjYyMDBjMC0zOWQwLTQ4OGUtYmZhMC1lMjRlNzhlN2JhYzgiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJsaXZlc2ggbSIsInN1YiI6IjYxYzBiOTY5LWJkYzktNGU3My04NTJhLWU0ZTgxYjZhYTE2NSJ9LCJlbWFpbCI6IjIyMTUwMTA2NUByYWphbGFrc2htaS5lZHUuaW4iLCJuYW1lIjoibGl2ZXNoIG0iLCJyb2xsTm8iOiIyMjE1MDEwNjUiLCJhY2Nlc3NDb2RlIjoiY1d5YVhXIiwiY2xpZW50SUQiOiI2MWMwYjk2OS1iZGM5LTRlNzMtODUyYS1lNGU4MWI2YWExNjUiLCJjbGllbnRTZWNyZXQiOiJZVkFEc1NKUUZRUUR4TUNuIn0.00bKMd-2DLRn5Q1ANpPu2YKDz6HBQ2T0q-Z8IQdBlNQ"; 
    public static void log(String stack, String level, String module, String message) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(LOGGING_ENDPOINT);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");

            String requestBody = String.format(
                "{ \"stack\": \"%s\", \"level\": \"%s\", \"package\": \"%s\", \"message\": \"%s\" }",
                stack.toLowerCase(), level.toLowerCase(), module.toLowerCase(), message
            );

            try (OutputStream output = connection.getOutputStream()) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                output.write(data);
            }
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                System.out.println("[Logger] Log sent successfully.");
            } else {
                System.err.println("[Logger] Failed to send log. HTTP Status: " + status);
            }

        } catch (IOException e) {
            System.err.println("[Logger] Logging failed due to network issue: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

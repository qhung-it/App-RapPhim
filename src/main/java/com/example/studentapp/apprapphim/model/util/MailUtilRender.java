package com.example.studentapp.apprapphim.model.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MailUtilRender {
    // API endpoint của Brevo
    private static final String BREVO_API_URL =
            "https://api.brevo.com/v3/smtp/email";

    // API Key lấy từ biến môi trường
    private static final String BREVO_API_KEY =
            System.getenv("BREVO_API_KEY");

    // Email người gửi đã xác minh trên Brevo
    private static final String SENDER_EMAIL =
            System.getenv("BREVO_SENDER_EMAIL");

    // Tên hiển thị người gửi
    private static final String SENDER_NAME =
            System.getenv("BREVO_SENDER_NAME");

    /**
     * Gửi email thông qua Brevo HTTP API.
     *
     * @param to      Email người nhận
     * @param toName  Tên người nhận
     * @param subject Tiêu đề email
     * @param body    Nội dung email
     */
    public static void sendMail(
            String to,
            String toName,
            String subject,
            String body) {

        // ==============================
        // 1. Kiểm tra dữ liệu đầu vào
        // ==============================
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Email người nhận không được để trống");
        }

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Tiêu đề email không được để trống");
        }

        if (body == null) {
            body = "";
        }

        // ==============================
        // 2. Kiểm tra biến môi trường
        // ==============================
        if (BREVO_API_KEY == null || BREVO_API_KEY.isBlank()) {
            throw new IllegalStateException("Chưa cấu hình BREVO_API_KEY");
        }

        if (SENDER_EMAIL == null || SENDER_EMAIL.isBlank()) {
            throw new IllegalStateException("Chưa cấu hình BREVO_SENDER_EMAIL");
        }

        // ==============================
        // 3. Chuẩn bị dữ liệu JSON
        // ==============================
        String safeToName = toName == null ? "" : toName;

        String safeSenderName =
                SENDER_NAME == null || SENDER_NAME.isBlank() ? "StudentApp" : SENDER_NAME;

        String json = "{"
                + "\"sender\":{"
                + "\"name\":\""
                + escapeJson(safeSenderName)
                + "\","
                + "\"email\":\""
                + escapeJson(SENDER_EMAIL)
                + "\""
                + "},"
                + "\"to\":[{"
                + "\"email\":\""
                + escapeJson(to.trim())
                + "\","
                + "\"name\":\""
                + escapeJson(safeToName)
                + "\""
                + "}],"
                + "\"subject\":\""
                + escapeJson(subject)
                + "\","
                + "\"textContent\":\""
                + escapeJson(body)
                + "\""
                + "}";

        // ==============================
        // 4. Tạo HTTP request
        // ==============================
        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(BREVO_API_URL))
                        .header("accept", "application/json")
                        .header("api-key", BREVO_API_KEY)
                        .header("content-type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();

        // ==============================
        // 5. Gửi request
        // ==============================
        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString(
                                    StandardCharsets.UTF_8
                            )
                    );


            // ==============================
            // 6. Kiểm tra kết quả
            // ==============================
            int statusCode =
                    response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                throw new RuntimeException(
                        "Brevo gửi email thất bại. "
                                + "HTTP "
                                + statusCode
                                + ": "
                                + response.body()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể kết nối tới Brevo.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Quá trình gửi email bị gián đoạn.", e);
        }
    }

    /**
     * Escape các ký tự đặc biệt
     * để tạo JSON hợp lệ.
     */
    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}
package org.anand.mynoteapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.handler.GenericResponse;
import org.anand.mynoteapp.security.CustomUserDetalis;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

public class CommonUtil {

    public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.name());
        response.put("message", "success");
        response.put("data", data); // ✅ add actual data here
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.name());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<?> createBuildResponseMessage(String message,Object data, HttpStatus status) {

        GenericResponse response = GenericResponse.builder().responseStatus(status).status("succes").message(message)
                .data(data).build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status) {

        GenericResponse response = GenericResponse.builder().responseStatus(status).status("failed").message("failed")
                .data(data).build();
        return response.create();
    }

    public static ResponseEntity<?> createErrorResponseMessagenew(String message, HttpStatus status) {

        GenericResponse response = GenericResponse.builder().responseStatus(status).status("failed").message(message)
                .build();
        return response.create();
    }

    public static String getContentType(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName); // java_programing.pdf

        switch (extension) {
            case "pdf":
                return "application/pdf";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
            case "txt":
                return "text/plan";
            case "png":
                return "image/png";
            case "jpeg":
                return "image/jpeg";
            default:
                return "application/octet-stream";
        }
    }

    public static String getUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString(); //http:localhost:9090/api/v1/auth
         url = url.replace(request.getRequestURI(), "");//// http:localhost:9090
        return url;
    }

    public static User getLoggedInUser() {
        try {
            CustomUserDetalis logUser = (CustomUserDetalis) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            return logUser.getUser();
        } catch (Exception e) {
            throw e;
        }
    }
}

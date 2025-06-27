package org.anand.mynoteapp.handler;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericResponse {

    private HttpStatus responseStatus;
    private String message;
    private String status;
    private Object data;

    public ResponseEntity<?> create(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", responseStatus);
        map.put("message", message);
        if (!ObjectUtils.isEmpty(data)) {
            map.put("data", data);
        }
        return new ResponseEntity<>(map, responseStatus);
    }
}

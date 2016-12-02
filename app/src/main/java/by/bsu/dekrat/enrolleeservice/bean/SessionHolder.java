package by.bsu.dekrat.enrolleeservice.bean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by USER on 01.12.2016.
 */

public class SessionHolder {

    private static final SessionHolder INSTANCE = new SessionHolder();

    private String jSessionID;

    public static SessionHolder getInstance() {
        return INSTANCE;
    }

    public String retrieveSessionID(ResponseEntity<Object> responseEntity) {
        List<String> cookieHeaders = responseEntity.getHeaders().get("Set-Cookie");
        for(String cookie : cookieHeaders) {
            if(cookie.contains("JSESSIONID")) {
                jSessionID = cookie.split("=")[1];
                break;
            }
        }
        return jSessionID;
    }

    public HttpHeaders getHeadersWithSessionID() {
        HttpHeaders sessionHeaders = new HttpHeaders();
        sessionHeaders.add("Cookie", "JSESSIONID=" + jSessionID);
        return sessionHeaders;
    }

    public String getjSessionID() {
        return jSessionID;
    }

    private SessionHolder() {
    }
}

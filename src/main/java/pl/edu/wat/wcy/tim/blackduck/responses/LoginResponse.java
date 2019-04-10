package pl.edu.wat.wcy.tim.blackduck.responses;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//LoginResponse object will be returned by SpringBoot server once an authentication is successful, it contains:
//1. JWT Token
//2. Schema Type of Token
//3. Username
//4. Array of Userâ€™s Authorities

public class LoginResponse {
    private String token;
    private String username;

    public LoginResponse(String accessToken, String username) {
        this.token = accessToken;
        this.username = username;
    }

    public LoginResponse(String accessToken) {
        this.token = accessToken;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
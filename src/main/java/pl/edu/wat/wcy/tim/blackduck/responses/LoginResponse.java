package pl.edu.wat.wcy.tim.blackduck.responses;

//LoginResponse object will be returned by SpringBoot server once an authentication is successful, it contains:
//1. JWT Token
//2. Schema Type of Token
//3. Username
//4. Array of Userâ€™s Authorities

public class LoginResponse {
    private String token;
    private String username;
    private String UUID;

    public LoginResponse(String accessToken, String username, String uniqueId) {
        this.token = accessToken;
        this.username = username;
        this.UUID = uniqueId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
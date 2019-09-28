package pl.edu.wat.wcy.tim.blackduck.responses;

public class RateResponse {
    String id;
    String rate;
    String user;
    String image;

    public RateResponse(String id, String rate, String user, String image) {
        this.id = id;
        this.rate = rate;
        this.user = user;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

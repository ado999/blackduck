package pl.edu.wat.wcy.tim.blackduck.requests;

public class ImageRequest {
    private String file;
    private String username;
    private String orginalfilename;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrginalfilename() {
        return orginalfilename;
    }

    public void setOrginalfilename(String orginalfilename) {
        this.orginalfilename = orginalfilename;
    }
}

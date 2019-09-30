package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderResponse {

    private UserShortResponse author;
    private String title;
    private String description;
    //private List<File> files = new ArrayList<>();

}

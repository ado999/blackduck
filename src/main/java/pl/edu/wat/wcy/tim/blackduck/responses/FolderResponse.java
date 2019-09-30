package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class FolderResponse {

    private UserShortResponse author;

    private String title;

    private String description;

    private List<PostResponse> files = new ArrayList<>();

}

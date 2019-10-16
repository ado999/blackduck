package pl.edu.wat.wcy.tim.blackduck.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderResponse {

    private int id;

    private UserShortResponse author;

    private String title;

    private String description;

    private List<PostResponse> contentList = new ArrayList<>();

 }

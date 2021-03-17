package main.service;

import main.api.response.TagResponse;
import main.models.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    public ResponseEntity<TagResponse> tag(String query) {
        TagResponse tagResponse = new TagResponse();
        // Будет репазиторий Repository.findAllByTags(query)
        List<Tag> tagList = new ArrayList<>();

        if (!tagList.isEmpty()) {
            List<TagResponse.innerTag> innerTagList = tagList.stream()
                    .map(t -> tagResponse.new innerTag(t.getName(), getWeight(t.getName())))
                    .collect(Collectors.toList());
            tagResponse.setTags(innerTagList);
        }
        return ResponseEntity.ok(tagResponse);
    }

    //Метод получения вес weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22
    private double getWeight(String name) {
        return 0.0;
    }
}

package main.service;

import main.api.response.TagResponse;
import main.models.Tag;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ResponseEntity<TagResponse> tag(String query) {
        TagResponse tagResponse = new TagResponse();
        List<Tag> tagList = null;
        if (query == null || query.isEmpty()) {
            tagList = tagRepository.findAll();
        } else {
            tagList = tagRepository.findByNameStartingWith(query);
        }
        if (!tagList.isEmpty()) {
            List<TagResponse.InnerTag> innerTagList = tagList.stream()
                    .map(t -> tagResponse.new InnerTag(t.getName(), getWeight(t.getName())))
                    .collect(Collectors.toList());
            tagResponse.setTags(innerTagList);
        }
        return ResponseEntity.ok(tagResponse);
    }

    //Метод получения весa ..... weightHibernate = hibernate * k = 0.20 * 1.11 = 0.22
    private double getWeight(String name) {
        TreeMap<String, Integer> mapCountTag = new TreeMap<>();
        List<Tag> tagList = tagRepository.findAll();
        double totalLength = 0;
        double maxLength = 0;
        for (Tag tag : tagList
        ) {
            mapCountTag.put(tag.getName(), tag.getTags2PostsList().size());
            totalLength = totalLength + tag.getTags2PostsList().size();
            if (tag.getTags2PostsList().size() > maxLength) {
                maxLength = tag.getTags2PostsList().size();
            }
        }
        double k = 0;
        if (maxLength != 0 && totalLength != 0) {
            k = (1 / (maxLength / totalLength)) * (mapCountTag.get(name) / totalLength);
        }
        return round(k, 2);
    }

    private double round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}

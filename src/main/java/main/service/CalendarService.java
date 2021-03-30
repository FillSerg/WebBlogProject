package main.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.api.response.CalendarResponse;
import main.models.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CalendarService {
    private final PostRepository postRepository;

    @Autowired
    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ResponseEntity<CalendarResponse> calendar(int year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        List<Post> postList = postRepository.sortAllWithTimeToList();
        Map<String, TreeMap<String, Integer>> mapYears = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        for (Post post : postList
        ) {
            String yearSt = yearFormat.format(post.getTime());
            String dateSt = dateFormat.format(post.getTime());
            if (mapYears.isEmpty() || !mapYears.containsKey(yearSt)) {
                mapYears.put(yearSt, new TreeMap<String, Integer>() {{
                    put(dateSt, 1);
                }});
            } else {
                mapYears.get(yearSt).merge(dateSt, 1, Integer::sum);
            }
        }
        if (!mapYears.isEmpty()) {
            List<String> years = new ArrayList<>(mapYears.keySet());
            Collections.sort(years);
            System.out.println(year);
            calendarResponse.setYears(years);
            if (years.contains(Integer.toString(year))) {
                calendarResponse.setPosts(mapYears.get(Integer.toString(year)));
            } else {
                calendarResponse.setPosts(mapYears.get(years.get(years.size() - 1)));
            }
        }
        return ResponseEntity.ok(calendarResponse);
    }
}

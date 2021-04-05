package main.repository;

import main.api.response.TagResponse;
import main.models.Tag;
import main.models.Tags2Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {


    //
    @Query("select tag from Tag tag where tag.name LIKE %:query%")
    List<Tag> findByNameTag(@Param("query") String query);

    //Это здесь нужен по условию.
    List<Tag> findByNameStartingWith(@Param("query") String query);

    List<Tag> findByName(@Param("query") String query);

    @Query("select tag.id from Tag tag where tag.name=:tag")
    int findIdByName(@Param("tag") String tag);

    @Query("select tag from Tag tag where tag.name=:tag")
    Tag findTagByName(@Param("tag") String tag);


    List<Tag> findAllByNameIn(List<String> tags);
}

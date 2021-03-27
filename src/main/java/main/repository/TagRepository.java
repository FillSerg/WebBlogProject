package main.repository;

import main.api.response.TagResponse;
import main.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    /*@Query(value = "select t.name as name, "
            + "(select count(*) from tags2posts t2p where t2p.tag_id = t.id) / "
            + "(select count(*) from posts p where p.is_active = 1 "
            + "and p.moderation_status = 'ACCEPTED' and p.time < sysdate()) as weight "
            + "from tags t "
            + "where (:query is null or t.name = :query) group by t.id",
            nativeQuery = true)
    List<TagResponse.InnerTag> findAllByTags(String query);*/

    /*@Query("select tag from Tag tag where tag.name LIKE %?1")
    List<Tag> findByName(@Param("query") String query);*/

    //
    @Query("select tag from Tag tag where tag.name LIKE %:query%")
    List<Tag> findByNameTag(@Param("query") String query);

    //Это здесь нужен по условию.
    List<Tag>findByNameStartingWith(@Param("query") String query);

    List<Tag>findByName(@Param("query") String query);

    List<Tag> findAllByNameIn(List<String> tags);
}

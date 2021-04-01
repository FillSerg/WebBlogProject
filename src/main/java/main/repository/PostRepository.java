package main.repository;

import main.models.Post;
import main.models.Tag;
import main.models.Tags2Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select post from Post post WHERE post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' ORDER BY post.time")
    List<Post> sortAllWithTimeToList();

    @Query("select post from Post post  WHERE post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' ORDER BY post.time")
    Page<Post> sortAllWithTime(Pageable pageable);

    @Query("select post from Post post  WHERE post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' ORDER BY post.time DESC")
    Page<Post> reversedAllWithTime(Pageable pageable);

    @Query("select post from Post post  WHERE post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' ORDER BY post.postCommentList.size DESC")
    Page<Post> sortAllWithPostCommentList(Pageable pageable);

    @Query("select post from Post post  WHERE post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' ORDER BY post.postVoteList.size DESC")
    Page<Post> sortAllWithPostLike(Pageable pageable);

    Page<Post> findByTextContains(Pageable pageable, @Param("query") String query);

    //поиск по тексту и титлу используется
    @Query(value = "select post from Post post where post.text LIKE %:query% OR post.title LIKE %:query%")
    Page<Post> findByTextAndTitlePost(Pageable pageable, @Param("query") String query);

    //не используется
    Page<Post> findAllByTime(Pageable sortedByMode, Date time);

    //посты за определеную дату используется
    @Query("select post from Post post where post.isActive = 1 and " +
            "post.moderationStatus = 'ACCEPTED' and " +
            "post.time >= :beforeData and post.time < :afterDate")
    Page<Post> findAllByDate(Pageable sortedByMode, Date beforeData, Date afterDate);

//    List<Tags2Posts>findPostByTags2PostsList(Tag tag);

    Post findById(int id);


}



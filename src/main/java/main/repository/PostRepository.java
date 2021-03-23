package main.repository;

import main.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

    @Query("select post from Post post ORDER BY post.time")
    List<Post> sortAllWithTime();

    @Query("select post from Post post ORDER BY post.time DESC")
    List<Post> reversedAllWithTime();

    @Query("select post from Post post ORDER BY post.postCommentList.size")
    List<Post> sortAllWithPostCommentList();

    @Query("select post from Post post ORDER BY post.postVoteList.size")
    List<Post> sortAllWithPostLike();
}



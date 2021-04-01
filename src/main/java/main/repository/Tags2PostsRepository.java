package main.repository;

import main.models.Tags2Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tags2PostsRepository extends JpaRepository<Tags2Posts, Long> {


    void deleteAllByPostId(Long postId);
}

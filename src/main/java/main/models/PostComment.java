package main.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post_comments")
@Data
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parentId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private main.models.Post postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private main.models.User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "parentId")
    private List<PostComment> postCommentList;
}


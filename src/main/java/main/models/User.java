package main.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    @Column(name = "reg_time", nullable = false)
    private Date regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    private String photo;

    @OneToMany(targetEntity = main.models.Post.class, mappedBy = "user")
    private List<main.models.Post> postListUser;

    @OneToMany(mappedBy = "moderator")
    private List<main.models.Post> postListModerator;

    @OneToMany(targetEntity = main.models.PostVote.class, mappedBy = "user")
    private List<main.models.PostVote> postVoteList;

    @OneToMany(targetEntity = PostComment.class, mappedBy = "user")
    private List<PostComment> postCommentList;
}

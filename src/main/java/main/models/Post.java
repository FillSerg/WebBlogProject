package main.models;

import lombok.Data;
import main.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status",
            nullable = false,
            columnDefinition = "enum('NEW','ACCEPTED','DECLINED') default 'NEW'")
    private ModerationStatus moderationStatus;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany(targetEntity = main.models.PostVote.class, mappedBy = "postId")
    private List<main.models.PostVote> postVoteList;

    @OneToMany(targetEntity = Tags2Posts.class, mappedBy = "postId")
    private List<Tags2Posts> tags2PostsList;

    @OneToMany(targetEntity = PostComment.class, mappedBy = "postId")
    private List<PostComment> postCommentList;
}

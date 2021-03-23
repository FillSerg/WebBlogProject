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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != post.id) return false;
        if (isActive != post.isActive) return false;
        if (viewCount != post.viewCount) return false;
        if (moderationStatus != post.moderationStatus) return false;
        if (moderator != null ? !moderator.equals(post.moderator) : post.moderator != null) return false;
        if (user != null ? !user.equals(post.user) : post.user != null) return false;
        if (time != null ? !time.equals(post.time) : post.time != null) return false;
        if (title != null ? !title.equals(post.title) : post.title != null) return false;
        if (text != null ? !text.equals(post.text) : post.text != null) return false;
        if (postVoteList != null ? !postVoteList.equals(post.postVoteList) : post.postVoteList != null) return false;
        if (tags2PostsList != null ? !tags2PostsList.equals(post.tags2PostsList) : post.tags2PostsList != null)
            return false;
        return postCommentList != null ? postCommentList.equals(post.postCommentList) : post.postCommentList == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) isActive;
        result = 31 * result + (moderationStatus != null ? moderationStatus.hashCode() : 0);
        result = 31 * result + (moderator != null ? moderator.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + viewCount;
        result = 31 * result + (postVoteList != null ? postVoteList.hashCode() : 0);
        result = 31 * result + (tags2PostsList != null ? tags2PostsList.hashCode() : 0);
        result = 31 * result + (postCommentList != null ? postCommentList.hashCode() : 0);
        return result;
    }
}

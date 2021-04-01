package main.models;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class Tags2Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private main.models.Post postId;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private main.models.Tag tagId;


    @Override
    public String toString() {
        return "Tags2Posts{" +
                "id=" + id +
//                ", postId=" + postId +
//                ", tagId=" + tagId +
                '}';
    }
}

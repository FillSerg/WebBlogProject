package main.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tagId")
    private List<Tags2Posts> tags2PostsList;


    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tags2PostsList=" + tags2PostsList +
                '}';
    }
}
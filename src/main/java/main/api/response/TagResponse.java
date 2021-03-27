package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TagResponse {
    private List<InnerTag> tags;

    public class InnerTag {
        private String name;
        private double weight;

        public InnerTag(String name, double weight) {
            this.name = name;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public TagResponse() {
        tags = new ArrayList<>();
    }

    public List<InnerTag> getTags() {
        return tags;
    }

    public void setTags(List<InnerTag> tags) {
        this.tags = tags;
    }
}

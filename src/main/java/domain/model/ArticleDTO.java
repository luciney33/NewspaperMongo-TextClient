package domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private String description;
    private int nPaperId;
    private String type;
    private double avgRating;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----- Articles -----\n")
                .append(" Description : ").append(description).append("\n")
                .append(" Newspaper : ").append(nPaperId).append("\n")
                .append(" Type : ").append(type).append("\n")
                .append(" Avg Rating : ").append(avgRating).append("\n")
                .append("----------------\n");
        return builder.toString();
    }
}

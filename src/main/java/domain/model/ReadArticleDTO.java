package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ReadArticleDTO {
    private int idReader;
    private int idArticle;
    private String nameReader;
    private LocalDate dobReader;
    private List<String> subscriptionsReader;
    private int rating;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----- Reader Activities -----\n")
                .append(" ID   : ").append(idReader).append("\n")
                .append(" Article ID : ").append(idArticle).append("\n")
                .append(" Name : ").append(nameReader).append("\n")
                .append(" Birth Date : ").append(dobReader).append("\n")
                .append(" Subscriptions : ").append(subscriptionsReader).append("\n")
                .append(" Rating : ").append(rating).append("\n")
                .append("----------------\n");
        return builder.toString();
    }
}

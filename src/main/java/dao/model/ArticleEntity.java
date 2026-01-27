package dao.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  ArticleEntity {
    private String description;

    private TypeEntity type;

    @Singular("readArticle")
    private List<ReadArticleEntity> readarticle = new ArrayList<>();
}

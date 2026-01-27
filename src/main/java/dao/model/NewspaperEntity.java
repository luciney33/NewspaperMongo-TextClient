package dao.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewspaperEntity {
    private ObjectId id;
    private String name;

    @Singular
    private List<ArticleEntity> articles = new ArrayList<>();

}

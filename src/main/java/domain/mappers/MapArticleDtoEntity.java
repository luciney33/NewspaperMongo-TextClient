package domain.mappers;


import dao.model.ArticleEntity;
import dao.model.ReadArticleEntity;
import domain.model.ArticleDTO;
import java.util.ArrayList;
import java.util.List;

public class MapArticleDtoEntity {

    public ArticleDTO toDTO(ArticleEntity entity) {
        double avgRating = calculateAverageRating(entity.getReadarticle());

        return ArticleDTO.builder()
                .description(entity.getDescription())
                .type(entity.getType())
                .avgRating(avgRating)
                .build();
    }

    private double calculateAverageRating(List<ReadArticleEntity> readArticles) {
        if (readArticles == null || readArticles.isEmpty()) {
            return 0.0;
        }

        return readArticles.stream()
                .mapToInt(ReadArticleEntity::getRating)
                .average()
                .orElse(0.0);
    }

    public List<ArticleDTO> toDTOList(List<ArticleEntity> entities) {
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (ArticleEntity entity : entities) {
            articleDTOS.add(toDTO(entity));
        }
        return articleDTOS;
    }

}

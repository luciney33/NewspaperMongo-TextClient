package domain.mappers;


import dao.model.ArticleEntity;
import dao.model.ReadArticleEntity;
import dao.model.TypeEntity;
import domain.model.ArticleDTO;
import domain.model.TypeDTO;

public class MapArticleDtoEntity {
    public ArticleDTO toDTO(ArticleEntity entity) {
        return new ArticleDTO(
                entity.getDescription(),
                0,
                entity.getType(),
                0);
    }

    public ArticleEntity toEntity(ArticleDTO dto) {
        return ArticleEntity.builder()
                .description(dto.getDescription())
                .type(dto.getType())
                .build();
    }

}

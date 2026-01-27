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
                entity.getReadarticle(),
                entity.getNPaperId(),
                new TypeDTO(entity.getType().getName()));
    }

    public ArticleEntity toEntity(ArticleDTO dto) {
        TypeEntity typeEntity = TypeEntity.builder()
                .name(dto.getDescription())
                .build();

        return ArticleEntity.builder()
                .description(dto.getDescription())
                .type(typeEntity)
                .build();
    }

}

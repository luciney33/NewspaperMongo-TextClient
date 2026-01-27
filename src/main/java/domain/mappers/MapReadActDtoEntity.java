package domain.mappers;

import newspaperoot.dao.hibernate.model.JpaReadArticleEntity;
import newspaperoot.dao.hibernate.model.JpaReaderEntity;
import newspaperoot.domain.model.ReadArticleDTO;


public class MapReadActDtoEntity {
    public JpaReadArticleEntity dtoToEntity(ReadArticleDTO dto) {
        JpaReaderEntity jpaReaderEntity = new JpaReaderEntity();
        jpaReaderEntity.setId_reader(dto.getIdReader());
        return new JpaReadArticleEntity(0,jpaReaderEntity, dto.getIdArticle(),dto.getRating());
    }

}

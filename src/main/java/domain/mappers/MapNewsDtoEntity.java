package domain.mappers;

import newspaperoot.dao.hibernate.model.JpaNewspaperEntity;
import newspaperoot.domain.model.NewspaperDTO;

import java.util.ArrayList;
import java.util.List;

public class MapNewsDtoEntity {
    public NewspaperDTO entityToDto(JpaNewspaperEntity entity) {
        return new NewspaperDTO(entity.getId(), entity.getName());
    }
    public List<NewspaperDTO> entityListToDtoList(List<JpaNewspaperEntity> entities) {
        List<NewspaperDTO> dtos = new ArrayList<>();
        for (JpaNewspaperEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}

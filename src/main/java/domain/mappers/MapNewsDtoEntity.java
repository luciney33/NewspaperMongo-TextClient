package domain.mappers;

import dao.model.NewspaperEntity;
import domain.model.NewspaperDTO;

import java.util.ArrayList;
import java.util.List;

public class MapNewsDtoEntity {
    public NewspaperDTO entityToDto(NewspaperEntity entity) {
        return new NewspaperDTO(entity.getId(), entity.getName());
    }

    public List<NewspaperDTO> entityListToDtoList(List<NewspaperEntity> entities) {
        List<NewspaperDTO> dtos = new ArrayList<>();
        for (NewspaperEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}

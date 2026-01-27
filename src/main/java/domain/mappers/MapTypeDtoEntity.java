package domain.mappers;

import newspaperoot.dao.hibernate.model.JpaTypeEntity;
import newspaperoot.domain.model.TypeDTO;

import java.util.ArrayList;
import java.util.List;

public class MapTypeDtoEntity {
    public TypeDTO entityToDto(JpaTypeEntity entity) {
        return new TypeDTO(entity.getId(), entity.getName(), entity.getDescription());
    }
    public List<TypeDTO> entityToDtoList(List<JpaTypeEntity> entities){
        List<TypeDTO> dtos = new ArrayList<TypeDTO>();
        for(JpaTypeEntity entity: entities){
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}

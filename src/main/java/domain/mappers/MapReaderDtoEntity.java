package domain.mappers;


import dao.model.ReaderEntity;
import domain.model.ReaderDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MapReaderDtoEntity {
    public ReaderDTO entityToDto(ReaderEntity entity) {
        return new ReaderDTO(entity.getId(), entity.getName(), entity.getDob());
    }

    public List<ReaderDTO> entityListToDtoList(List<ReaderEntity> entities) {
        List<ReaderDTO> dtos = new ArrayList<>();
        for (ReaderEntity entity : entities) {
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }
}

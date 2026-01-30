package domain.mappers;

import dao.model.ReadArticleEntity;
import domain.model.ReadArticleDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapReadArticleDtoEntity {
    public ReadArticleEntity dtoToEntity(ReadArticleDTO dto) {
        if (dto == null) {
            return null;
        }
        return ReadArticleEntity.builder()
                .idReader(dto.getIdReader())
                .rating(dto.getRating())
                .build();
    }

    public ReadArticleDTO entityToDto(ReadArticleEntity entity, int idArticle, String nameReader,
                                       String dobReader, List<String> subscriptionsReader) {
        if (entity == null) {
            return null;
        }

        LocalDate birthDate = null;
        if (dobReader != null && !dobReader.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                birthDate = LocalDate.parse(dobReader, formatter);
            } catch (Exception e) {
                try {
                    birthDate = LocalDate.parse(dobReader);
                } catch (Exception ex) {
                }
            }
        }

        return new ReadArticleDTO(
                entity.getIdReader(),
                idArticle,
                nameReader,
                birthDate,
                subscriptionsReader,
                entity.getRating()
        );
    }

    public List<ReadArticleDTO> entityListToDtoList(List<ReadArticleEntity> entities, int idArticle, String nameReader, String dobReader,
                                                     List<String> subscriptionsReader) {
        List<ReadArticleDTO> dtos = new ArrayList<>();
        for (ReadArticleEntity entity : entities) {
            dtos.add(entityToDto(entity, idArticle, nameReader, dobReader, subscriptionsReader));
        }
        return dtos;
    }
}

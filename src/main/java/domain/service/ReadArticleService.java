package domain.service;

import dao.ReadArticleRepository;
import dao.ReaderRepository;
import dao.model.ReadArticleEntity;
import dao.model.ReaderEntity;
import domain.model.ReadArticleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReadArticleService {
    private ReadArticleRepository readArticleRepository;
    private ReaderRepository readerRepository;

    @Inject
    public ReadArticleService(ReadArticleRepository readArticleRepository, ReaderRepository readerRepository) {
        this.readArticleRepository = readArticleRepository;
        this.readerRepository = readerRepository;
    }

    public ReadArticleService() {}

    public int addRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = dtoToEntity(dto);
        if (entity == null) {
            return -1;
        }
        return readArticleRepository.save(entity, articleDescription);
    }

    public void modifyRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = dtoToEntity(dto);
        if (entity != null) {
            readArticleRepository.update(entity, articleDescription);
        }
    }

    public boolean deleteRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = dtoToEntity(dto);
        if (entity == null) {
            return false;
        }
        return readArticleRepository.delete(entity, articleDescription);
    }

    private ReadArticleEntity dtoToEntity(ReadArticleDTO dto) {
        if (dto == null) {
            return null;
        }
        ReaderEntity reader = readerRepository.getAll().stream()
                .filter(r -> r.getId().hashCode() == dto.getIdReader())
                .findFirst()
                .orElse(null);
        if (reader == null) {
            System.err.println("Reader no encontrado con ID: " + dto.getIdReader());
            return null;
        }

        return ReadArticleEntity.builder()
                .idReader(reader.getId())
                .rating(dto.getRating())
                .build();
    }
}


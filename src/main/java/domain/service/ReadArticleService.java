package domain.service;

import dao.ReadArticleRepository;
import dao.ReaderRepository;
import dao.model.ReadArticleEntity;
import dao.model.ReaderEntity;
import domain.mappers.MapReadArticleDtoEntity;
import domain.model.ReadArticleDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReadArticleService {
    private ReadArticleRepository readArticleRepository;
    private ReaderRepository readerRepository;
    private MapReadArticleDtoEntity mapper;

    @Inject
    public ReadArticleService(ReadArticleRepository readArticleRepository, ReaderRepository readerRepository, MapReadArticleDtoEntity mapper) {
        this.readArticleRepository = readArticleRepository;
        this.readerRepository = readerRepository;
        this.mapper = mapper;
    }

    public ReadArticleService() {}

    public List<ReadArticleDTO> getReadersByArticle(String description) {
        List<ReadArticleEntity> readArticleEntities = readArticleRepository.getAllByArticleId(description);
        List<ReadArticleDTO> result = new ArrayList<>();

        for (ReadArticleEntity readArticleEntity : readArticleEntities) {
            List<ReaderEntity> allReaders = readerRepository.getAll();
            ReaderEntity reader = allReaders.stream()
                    .filter(r -> r.getId().equals(readArticleEntity.getIdReader()))
                    .findFirst()
                    .orElse(null);

            if (reader != null) {
                List<String> subscriptionNames = reader.getSubscriptions().stream()
                        .map(sub -> sub.getId().toString())
                        .collect(Collectors.toList());

                ReadArticleDTO dto = mapper.entityToDto(
                        readArticleEntity,
                        0,
                        reader.getName(),
                        reader.getDob(),
                        subscriptionNames
                );
                result.add(dto);
            }
        }

        return result;
    }

    public int addRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = mapper.dtoToEntity(dto);
        if (entity == null) {
            return -1;
        }
        return readArticleRepository.save(entity, articleDescription);
    }

    public void modifyRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = mapper.dtoToEntity(dto);
        if (entity != null) {
            readArticleRepository.update(entity, articleDescription);
        }
    }

    public boolean deleteRating(ReadArticleDTO dto, String articleDescription) {
        ReadArticleEntity entity = mapper.dtoToEntity(dto);
        if (entity == null) {
            return false;
        }
        return readArticleRepository.delete(entity, articleDescription);
    }
}


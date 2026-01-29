package domain.service;

import dao.ReadArticleRepository;
import dao.model.ReadArticleEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

@ApplicationScoped
public class ReadArticleService {
    private ReadArticleRepository readArticleRepository;

    @Inject
    public ReadArticleService(ReadArticleRepository readArticleRepository) {
        this.readArticleRepository = readArticleRepository;
    }

    public ReadArticleService() {}

    // 9. Add rating to an article
    public int addRating(ObjectId idReader, int rating) {
        ReadArticleEntity entity = ReadArticleEntity.builder()
                .idReader(idReader)
                .rating(rating)
                .build();

        return readArticleRepository.save(entity);
    }

    // 10. Modify rating of an article
    public void modifyRating(ObjectId idReader, int rating) {
        ReadArticleEntity entity = ReadArticleEntity.builder()
                .idReader(idReader)
                .rating(rating)
                .build();

        readArticleRepository.update(entity);
    }

    // 11. Delete rating of an article
    public boolean deleteRating(ObjectId idReader) {
        ReadArticleEntity entity = ReadArticleEntity.builder()
                .idReader(idReader)
                .rating(0)
                .build();

        return readArticleRepository.delete(entity);
    }
}


package domain.service;

import dao.ArticleRepository;
import dao.NewspaperRepository;
import dao.model.ArticleEntity;
import dao.model.NewspaperEntity;
import domain.mappers.MapArticleDtoEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import domain.model.ArticleDTO;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2

@ApplicationScoped
public class ArticleService {
    private ArticleRepository articleRepository;
    private NewspaperRepository newspaperRepository;
    private MapArticleDtoEntity mapper;


    @Inject
    public ArticleService(ArticleRepository articleRepository,
                         NewspaperRepository newspaperRepository,
                         MapArticleDtoEntity mapper) {
        this.articleRepository = articleRepository;
        this.newspaperRepository = newspaperRepository;
        this.mapper = mapper;
    }

    public ArticleService(){}


    public List<ArticleDTO> getAllArticles() {
        List<ArticleEntity> articles = articleRepository.getAll();
        List<NewspaperEntity> newspapers = newspaperRepository.getAll();
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        for (ArticleEntity article : articles) {
            ArticleDTO dto = mapper.toDTO(article);

            for (NewspaperEntity newspaper : newspapers) {
                boolean found = false;
                for (ArticleEntity newspaperArticle : newspaper.getArticles()) {
                    if (newspaperArticle.getDescription().equals(article.getDescription()) &&
                        newspaperArticle.getType().equals(article.getType())) {
                        dto.setNPaperId(newspaper.getId().toHexString().hashCode());
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }

            articleDTOs.add(dto);
        }

        return articleDTOs;
    }

    public int save(String newspaperName, ArticleEntity article) {
        ArticleEntity articlenuevo = ArticleEntity.builder()
                .description(article.getDescription())
                .type(article.getType())
                .build();

        return articleRepository.save(articlenuevo, newspaperName);
    }

    public void update(ArticleEntity article) {
        articleRepository.update(article);
    }

//    // 4. Delete Article
//    public void deleteArticle(String newspaperId, int articleIndex, boolean forceDelete)
//            throws ArticleNotFoundException {
//        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
//        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
//            throw new ArticleNotFoundException("Artículo no encontrado");
//        }
//
//        ArticleEntity article = newspaper.getArticles().get(articleIndex);
//        if (!article.getReadarticle().isEmpty() && !forceDelete) {
//            throw new IllegalStateException("El artículo tiene valoraciones. Confirme la eliminación.");
//        }
//
//        newspaperDAO.deleteArticle(newspaperId, articleIndex);
//    }
//
//    // 7. Get Readers of an Article
//    public List<ReadArticleDTO> getReadersOfArticle(String newspaperId, int articleIndex)
//            throws ArticleNotFoundException {
//        List<ReadArticleDTO> readers = new ArrayList<>();
//        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
//
//        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
//            throw new ArticleNotFoundException("Artículo no encontrado");
//        }
//
//        ArticleEntity article = newspaper.getArticles().get(articleIndex);
//
////            for (ReadArticleEntity ra : article.getReadarticle()) {
////                ReaderEntity reader = readerDAO.findById(ra.getIdReader());
////                if (reader != null) {
////                    List<String> newspaperNames = getSubscribedNewspaperNames(reader);
////                    readers.add(new ReadArticleDTO(
////                        ra.getIdReader(),
////                        reader.getName(),
////                        reader.getDob(),
////                        ra.getRating(),
////                        newspaperNames));
////                }
////            }
//        return readers;
//    }
//
//    // 9. Add rating to an Article
//    public void addRating(String newspaperId, int articleIndex, int readerId, int rating)
//            throws ArticleNotFoundException {
//        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
//        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
//            throw new ArticleNotFoundException("Artículo no encontrado");
//        }
//
//        if (rating < 1 || rating > 5) {
//            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5");
//        }
//
//        // Verificar que el lector exista
//        ReaderEntity reader = readerDAO.findById(readerId);
//        if (reader == null) {
//            throw new IllegalArgumentException("Lector no encontrado");
//        }
//
//        // Verificar que el lector no haya valorado ya este artículo
//        ArticleEntity article = newspaper.getArticles().get(articleIndex);
//        for (ReadArticleEntity ra : article.getReadarticle()) {
//            if (ra.getIdReader() == readerId) {
//                throw new IllegalArgumentException("El lector ya ha valorado este artículo");
//            }
//        }
//
//        Document ratingDoc = new Document()
//            .append("id_reader", readerId)
//            .append("rating", rating);
//
//        newspaperDAO.addRating(newspaperId, articleIndex, ratingDoc);
//    }
//
//    // 10. Modify rating of an Article
//    public void modifyRating(String newspaperId, int articleIndex, int readerId, int newRating)
//            throws ArticleNotFoundException {
//        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
//        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
//            throw new ArticleNotFoundException("Artículo no encontrado");
//        }
//
//        if (newRating < 1 || newRating > 5) {
//            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5");
//        }
//
//        ArticleEntity article = newspaper.getArticles().get(articleIndex);
//        boolean found = false;
//        for (ReadArticleEntity ra : article.getReadarticle()) {
//            if (ra.getIdReader() == readerId) {
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            throw new IllegalArgumentException("El lector no ha valorado este artículo");
//        }
//
//        newspaperDAO.updateRating(newspaperId, articleIndex, readerId, newRating);
//    }
//
//    // 11. Delete rating of an Article
//    public void deleteRating(String newspaperId, int articleIndex, int readerId)
//            throws ArticleNotFoundException {
//        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
//        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
//            throw new ArticleNotFoundException("Artículo no encontrado");
//        }
//
//        ArticleEntity article = newspaper.getArticles().get(articleIndex);
//        boolean found = false;
//        for (ReadArticleEntity ra : article.getReadarticle()) {
//            if (ra.getIdReader() == readerId) {
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            throw new IllegalArgumentException("El lector no ha valorado este artículo");
//        }
//
//        newspaperDAO.deleteRating(newspaperId, articleIndex, readerId);
//    }
//
//    private double calculateAvgRating(ArticleEntity article) {
//        if (article.getReadarticle().isEmpty()) return 0.0;
//        return article.getReadarticle().stream()
//            .mapToInt(ReadArticleEntity::getRating)
//            .average()
//            .orElse(0.0);
//    }
//
//    private List<String> getSubscribedNewspaperNames(ReaderEntity reader) {
//        List<String> names = new ArrayList<>();
//        for (SubscriptionEntity sub : reader.getSubscriptions()) {
//            NewspaperEntity newspaper = newspaperDAO.findById(sub.getId().toString());
//            if (newspaper != null) {
//                names.add(newspaper.getName());
//            }
//        }
//        return names;
//    }
}

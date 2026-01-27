package domain.service;

import com.google.gson.Gson;
import org.bson.Document;
import dao.NewspaperDAO;
import dao.ReaderDAO;
import dao.model.*;
import domain.error.ArticleNotFoundException;
import domain.model.ArticleDTO;

import java.util.ArrayList;
import java.util.List;

public class ArticleService {
    private final NewspaperDAO newspaperDAO;
    private final ReaderDAO readerDAO;
    private final Gson gson;

    public ArticleService() {
        this.newspaperDAO = new NewspaperDAO();
        this.readerDAO = new ReaderDAO();
        this.gson = new Gson();
    }

    // 1. Get all Articles
    public List<ArticleDTO> getAllArticles() {
        List<ArticleDTO> articles = new ArrayList<>();
        List<NewspaperEntity> newspapers = newspaperDAO.findAll();

        for (NewspaperEntity newspaper : newspapers) {
            for (ArticleEntity article : newspaper.getArticles()) {
                double avgRating = calculateAvgRating(article);
                articles.add(new ArticleDTO(
                    article.getDescription(),
                    article.getType(),
                    newspaper.getId().toString(),
                    avgRating));
            }
        }
        return articles;
    }

    // 2. Add Article
    public void addArticle(String newspaperId, String description, String type) throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null) {
            throw new ArticleNotFoundException("Periódico no encontrado");
        }

        Document articleDoc = new Document()
            .append("description", description)
            .append("type", type)
            .append("readarticle", new ArrayList<>());

        newspaperDAO.addArticle(newspaperId, articleDoc);
    }

    // 3. Update Article
    public void updateArticle(String newspaperId, int articleIndex, String newDescription, String newType)
            throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        ArticleEntity article = newspaper.getArticles().get(articleIndex);
        article.setDescription(newDescription);
        article.setType(newType);

        Document articleDoc = Document.parse(gson.toJson(article));
        newspaperDAO.updateArticle(newspaperId, articleIndex, articleDoc);
    }

    // 4. Delete Article
    public void deleteArticle(String newspaperId, int articleIndex, boolean forceDelete)
            throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        ArticleEntity article = newspaper.getArticles().get(articleIndex);
        if (!article.getReadarticle().isEmpty() && !forceDelete) {
            throw new IllegalStateException("El artículo tiene valoraciones. Confirme la eliminación.");
        }

        newspaperDAO.deleteArticle(newspaperId, articleIndex);
    }

    // 7. Get Readers of an Article
    public List<ReaderArticleDTO> getReadersOfArticle(String newspaperId, int articleIndex)
            throws ArticleNotFoundException {
        List<ReaderArticleDTO> readers = new ArrayList<>();
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);

        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        ArticleEntity article = newspaper.getArticles().get(articleIndex);

            for (ReadArticleEntity ra : article.getReadarticle()) {
                ReaderEntity reader = readerDAO.findById(ra.getIdReader());
                if (reader != null) {
                    List<String> newspaperNames = getSubscribedNewspaperNames(reader);
                    readers.add(new ReaderArticleDTO(
                        ra.getIdReader(),
                        reader.getName(),
                        reader.getDob(),
                        ra.getRating(),
                        newspaperNames));
                }
            }
        return readers;
    }

    // 9. Add rating to an Article
    public void addRating(String newspaperId, int articleIndex, int readerId, int rating)
            throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5");
        }

        // Verificar que el lector exista
        ReaderEntity reader = readerDAO.findById(readerId);
        if (reader == null) {
            throw new IllegalArgumentException("Lector no encontrado");
        }

        // Verificar que el lector no haya valorado ya este artículo
        ArticleEntity article = newspaper.getArticles().get(articleIndex);
        for (ReadArticleEntity ra : article.getReadarticle()) {
            if (ra.getIdReader() == readerId) {
                throw new IllegalArgumentException("El lector ya ha valorado este artículo");
            }
        }

        Document ratingDoc = new Document()
            .append("id_reader", readerId)
            .append("rating", rating);

        newspaperDAO.addRating(newspaperId, articleIndex, ratingDoc);
    }

    // 10. Modify rating of an Article
    public void modifyRating(String newspaperId, int articleIndex, int readerId, int newRating)
            throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        if (newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5");
        }

        ArticleEntity article = newspaper.getArticles().get(articleIndex);
        boolean found = false;
        for (ReadArticleEntity ra : article.getReadarticle()) {
            if (ra.getIdReader() == readerId) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("El lector no ha valorado este artículo");
        }

        newspaperDAO.updateRating(newspaperId, articleIndex, readerId, newRating);
    }

    // 11. Delete rating of an Article
    public void deleteRating(String newspaperId, int articleIndex, int readerId)
            throws ArticleNotFoundException {
        NewspaperEntity newspaper = newspaperDAO.findById(newspaperId);
        if (newspaper == null || articleIndex >= newspaper.getArticles().size() || articleIndex < 0) {
            throw new ArticleNotFoundException("Artículo no encontrado");
        }

        ArticleEntity article = newspaper.getArticles().get(articleIndex);
        boolean found = false;
        for (ReadArticleEntity ra : article.getReadarticle()) {
            if (ra.getIdReader() == readerId) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("El lector no ha valorado este artículo");
        }

        newspaperDAO.deleteRating(newspaperId, articleIndex, readerId);
    }

    private double calculateAvgRating(ArticleEntity article) {
        if (article.getReadarticle().isEmpty()) return 0.0;
        return article.getReadarticle().stream()
            .mapToInt(ReadArticleEntity::getRating)
            .average()
            .orElse(0.0);
    }

    private List<String> getSubscribedNewspaperNames(ReaderEntity reader) {
        List<String> names = new ArrayList<>();
        for (SubscriptionEntity sub : reader.getSubscriptions()) {
            NewspaperEntity newspaper = newspaperDAO.findById(sub.getId().toString());
            if (newspaper != null) {
                names.add(newspaper.getName());
            }
        }
        return names;
    }
}


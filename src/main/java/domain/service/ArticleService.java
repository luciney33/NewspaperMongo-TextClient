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

    public boolean delete(String description, boolean confirmation) {
        return articleRepository.delete(description, confirmation);
    }
}

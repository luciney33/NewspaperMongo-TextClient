package ui;

import dao.model.ArticleEntity;
import domain.model.ArticleDTO;
import domain.model.NewspaperDTO;
import domain.service.ArticleService;
import domain.service.NewspaperService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;
@ApplicationScoped
public class ArticleUI {
    Scanner scanner = new Scanner(System.in);
    private ArticleService articleService;
    private NewspaperService newspaperService;

    @Inject
    public ArticleUI(ArticleService articleService,NewspaperService newspaperService) {
        this.articleService = articleService;
        this.newspaperService = newspaperService;
    }
    public ArticleUI() {}

    public void getArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        System.out.println("\n ═══════════ TODOS LOS ARTICULOS ═══════════");
        if (articles.isEmpty()) {
            System.out.println("No hay articulos registrados.");
        } else {
            for (ArticleDTO a : articles) {
                System.out.println(a.toString());
            }
        }
    }

    public void save() {
        System.out.println("\n═══════════ AÑADIR ARTICULO ═══════════");

        System.out.println("\n Periodicos :");
        List<NewspaperDTO> newspapers = newspaperService.getAllNewspapers();

        if (newspapers.isEmpty()) {
            System.out.println("No hay periodicos disponibles. Crea un periodico primero");
            return;
        }

        int index = 1;
        for (NewspaperDTO newspaper : newspapers) {
            System.out.println(index++ + ". " + newspaper.getName() + " (ID: " + newspaper.getId() + ")");
        }

        System.out.print("\nSelecciona el numero del periodico: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection < 1 || selection > newspapers.size()) {
            System.out.println("Seleccion invalida");
            return;
        }

        String newspaperName = newspapers.get(selection - 1).getName();

        System.out.print("Descripcion del articulo: ");
        String description = scanner.nextLine();
        System.out.print("Tipo (Sports,Noticias): ");
        String type = scanner.nextLine();

        ArticleEntity newArticle = ArticleEntity.builder()
                .description(description)
                .type(type)
                .build();

        int result = articleService.save(newspaperName, newArticle);
        if (result > 0) {
            System.out.println("Articulo añadido correctamente al periodico '" + newspaperName + "'");
        } else {
            System.out.println("Error: No se pudo añadir el articulo. Verifica el nombre del periodico.");
        }
    }

    public void update() {
        System.out.println("\n═══════════ ACTUALIZAR ARTICULO ═══════════");
        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            System.out.println("No hay articulo disponibles para actualizar.");
            return;
        }

        System.out.println("\nArticulo disponibles:");
        int index = 1;
        for (ArticleDTO article : articles) {
            System.out.println(index++ + ". " + article.getDescription() + " - Tipo: " + article.getType());
        }

        System.out.print("\nSelecciona el número del artículo a actualizar: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection < 1 || selection > articles.size()) {
            System.out.println("Seleccion invalida");
            return;
        }

        String currentDescription = articles.get(selection - 1).getDescription();

        System.out.print("Nuevo tipo (Sports, Noticias): ");
        String newType = scanner.nextLine();

        ArticleEntity updatedArticle = ArticleEntity.builder()
                .description(currentDescription)
                .type(newType)
                .build();

        articleService.update(updatedArticle);
        System.out.println("Articulo actualizado correctamente");
    }

    public void delete() {
        System.out.println("\n═══════════ ELIMINAR ARTICULO ═══════════");

        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            System.out.println("No hay articulos registrados.");
            return;
        }

        System.out.println("\n----- Articulos disponibles -----");
        for (int i = 0; i < articles.size(); i++) {
            System.out.println((i + 1) + ". " + articles.get(i).getDescription() + " (" + articles.get(i).getType() + ")");
        }

        System.out.print("\nSeleccione el número del articulo a eliminar: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection < 1 || selection > articles.size()) {
            System.out.println("Seleccion invalida");
            return;
        }

        String description = articles.get(selection - 1).getDescription();

        System.out.print("Estas seguro de eliminar el articulo '" + description + "'? (s/n): ");
        String respuesta = scanner.nextLine();
        boolean confirmation = respuesta.equalsIgnoreCase("s");

        if (articleService.delete(description, confirmation)) {
            System.out.println("Articulo eliminado correctamente");
        } else {
            System.out.println("No se pudo eliminar el articulo");
        }
    }
}

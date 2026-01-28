package ui;

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
        System.out.println("\n📰 ═══════════ TODOS LOS ARTÍCULOS ═══════════");
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            for (ArticleDTO a : articles) {
                System.out.println(a.toString());
            }
        }
    }

    // 2. Add Article
    public void save() {
        System.out.println("\n➕ ═══════════ AÑADIR ARTÍCULO ═══════════");

        // ✅ Mostrar periódicos disponibles
        System.out.println("\n📰 Periódicos disponibles:");
        List<NewspaperDTO> newspapers = newspaperService.getAllNewspapers();

        if (newspapers.isEmpty()) {
            System.out.println("❌ No hay periódicos disponibles. Crea un periódico primero.");
            return;
        }

        int index = 1;
        for (NewspaperDTO newspaper : newspapers) {
            System.out.printf("%d. %s (ID: %s)\n", index++, newspaper.getName(), newspaper.getId());
        }

        System.out.print("\nSelecciona el número del periódico: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection < 1 || selection > newspapers.size()) {
            System.out.println("❌ Selección inválida");
            return;
        }

        String newspaperName = newspapers.get(selection - 1).getName();

        System.out.print("Descripción del artículo: ");
        String description = scanner.nextLine();
        System.out.print("Tipo (Sports, Politics, Noticias, etc.): ");
        String type = scanner.nextLine();

        int result = articleService.save(newspaperName, description, type);
        if (result > 0) {
            System.out.println("✅ Artículo añadido correctamente al periódico '" + newspaperName + "'");
        } else {
            System.out.println("❌ Error: No se pudo añadir el artículo. Verifica el nombre del periódico.");
        }
    }

    // 3. Update Article
    public void update() {
        System.out.println("\n✏️ ═══════════ ACTUALIZAR ARTÍCULO ═══════════");
        
        // Show all articles first
        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            System.out.println("❌ No hay artículos para actualizar.");
            return;
        }

        System.out.println("\n📰 Artículos disponibles:");
        int index = 1;
        for (ArticleDTO article : articles) {
            System.out.printf("%d. %s (Tipo: %s)\n", index++, 
                            article.getDescription(), article.getType());
        }

        System.out.print("\nSelecciona el número del artículo a actualizar: ");
        int selection = Integer.parseInt(scanner.nextLine());

        if (selection < 1 || selection > articles.size()) {
            System.out.println("❌ Selección inválida");
            return;
        }

        ArticleDTO selectedArticle = articles.get(selection - 1);
        String oldDescription = selectedArticle.getDescription();

        System.out.printf("\nActualizando artículo: %s\n", oldDescription);
        System.out.print("Nueva descripción (Enter para mantener): ");
        String newDescription = scanner.nextLine();
        if (newDescription.trim().isEmpty()) {
            newDescription = oldDescription;
        }

        System.out.print("Nuevo tipo (Enter para mantener): ");
        String newType = scanner.nextLine();
        if (newType.trim().isEmpty()) {
            newType = selectedArticle.getType();
        }

        int result = articleService.updateArticle(oldDescription, newDescription, newType);
        if (result > 0) {
            System.out.println("✅ Artículo actualizado correctamente");
        } else {
            System.out.println("❌ Error: No se pudo actualizar el artículo");
        }
    }
//
//    // 3. Update Article
//    public void updateArticle() throws ArticleNotFoundException {
//        System.out.println("\n✏️ ═══════════ ACTUALIZAR ARTÍCULO ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int index = Integer.parseInt(scanner.nextLine());
//        System.out.print("Nueva descripción: ");
//        String description = scanner.nextLine();
//        System.out.print("Nuevo tipo: ");
//        String type = scanner.nextLine();
//
//        articleService.updateArticle(newspaperId, index, description, type);
//        System.out.println("✅ Artículo actualizado correctamente");
//    }
//
//    // 4. Delete Article
//    public void deleteArticle() throws ArticleNotFoundException {
//        System.out.println("\n🗑️ ═══════════ ELIMINAR ARTÍCULO ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int index = Integer.parseInt(scanner.nextLine());
//
//        try {
//            articleService.deleteArticle(newspaperId, index, false);
//            System.out.println("✅ Artículo eliminado correctamente");
//        } catch (IllegalStateException e) {
//            System.out.print("⚠ " + e.getMessage() + " ¿Eliminar de todas formas? (s/n): ");
//            String respuesta = scanner.nextLine();
//            if (respuesta.equalsIgnoreCase("s")) {
//                articleService.deleteArticle(newspaperId, index, true);
//                System.out.println("✅ Artículo eliminado correctamente");
//            } else {
//                System.out.println("❌ Eliminación cancelada");
//            }
//        }
//    }
//    // 9. Add rating to an Article
//    public void addRating() throws ArticleNotFoundException {
//        System.out.println("\n⭐ ═══════════ AÑADIR VALORACIÓN ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int articleIndex = Integer.parseInt(scanner.nextLine());
//        System.out.print("ID del lector: ");
//        int readerId = Integer.parseInt(scanner.nextLine());
//        System.out.print("Valoración (1-5): ");
//        int rating = Integer.parseInt(scanner.nextLine());
//
//        articleService.addRating(newspaperId, articleIndex, readerId, rating);
//        System.out.println("✅ Valoración añadida correctamente");
//    }
//
//    // 10. Modify rating of an Article
//    public void modifyRating() throws ArticleNotFoundException {
//        System.out.println("\n✏️ ═══════════ MODIFICAR VALORACIÓN ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int articleIndex = Integer.parseInt(scanner.nextLine());
//        System.out.print("ID del lector: ");
//        int readerId = Integer.parseInt(scanner.nextLine());
//        System.out.print("Nueva valoración (1-5): ");
//        int rating = Integer.parseInt(scanner.nextLine());
//
//        articleService.modifyRating(newspaperId, articleIndex, readerId, rating);
//        System.out.println("✅ Valoración modificada correctamente");
//    }
//
//    // 11. Delete rating of an Article
//    public void deleteRating() throws ArticleNotFoundException {
//        System.out.println("\n🗑️ ═══════════ ELIMINAR VALORACIÓN ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int articleIndex = Integer.parseInt(scanner.nextLine());
//        System.out.print("ID del lector: ");
//        int readerId = Integer.parseInt(scanner.nextLine());
//
//        articleService.deleteRating(newspaperId, articleIndex, readerId);
//        System.out.println("✅ Valoración eliminada correctamente");
//    }

}

package ui;

import domain.error.ArticleNotFoundException;
import domain.model.ArticleDTO;
import domain.service.ArticleService;

import java.util.List;
import java.util.Scanner;

public class ArticleUI {
    Scanner scanner = new Scanner(System.in);
    private final ArticleService articleService;

    public ArticleUI() {
        this.articleService = new ArticleService();
    }

    // 1. Get all Articles
    public void getArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        System.out.println("\n📰 ═══════════ TODOS LOS ARTÍCULOS ═══════════");
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            for (int i = 0; i < articles.size(); i++) {
                ArticleDTO a = articles.get(i);
                System.out.printf("%d. %s%n", i + 1, a.getName());
                System.out.printf("   Tipo: %s | Periódico ID: %s | Rating promedio: %.2f%n",
                        a.getTypeName(), a.getNewspaperId(), a.getAvgRating());
            }
        }
    }

    // 2. Add Article
    public void saveArticle() throws ArticleNotFoundException {
        System.out.println("\n➕ ═══════════ AÑADIR ARTÍCULO ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Descripción del artículo: ");
        String description = scanner.nextLine();
        System.out.print("Tipo (Sports, Politics, etc.): ");
        String type = scanner.nextLine();

        articleService.addArticle(newspaperId, description, type);
        System.out.println("✅ Artículo añadido correctamente");
    }

    // 3. Update Article
    public void updateArticle() throws ArticleNotFoundException {
        System.out.println("\n✏️ ═══════════ ACTUALIZAR ARTÍCULO ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Índice del artículo (0, 1, 2...): ");
        int index = Integer.parseInt(scanner.nextLine());
        System.out.print("Nueva descripción: ");
        String description = scanner.nextLine();
        System.out.print("Nuevo tipo: ");
        String type = scanner.nextLine();

        articleService.updateArticle(newspaperId, index, description, type);
        System.out.println("✅ Artículo actualizado correctamente");
    }

    // 4. Delete Article
    public void deleteArticle() throws ArticleNotFoundException {
        System.out.println("\n🗑️ ═══════════ ELIMINAR ARTÍCULO ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Índice del artículo (0, 1, 2...): ");
        int index = Integer.parseInt(scanner.nextLine());

        try {
            articleService.deleteArticle(newspaperId, index, false);
            System.out.println("✅ Artículo eliminado correctamente");
        } catch (IllegalStateException e) {
            System.out.print("⚠ " + e.getMessage() + " ¿Eliminar de todas formas? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                articleService.deleteArticle(newspaperId, index, true);
                System.out.println("✅ Artículo eliminado correctamente");
            } else {
                System.out.println("❌ Eliminación cancelada");
            }
        }
    }
    // 9. Add rating to an Article
    public void addRating() throws ArticleNotFoundException {
        System.out.println("\n⭐ ═══════════ AÑADIR VALORACIÓN ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Índice del artículo (0, 1, 2...): ");
        int articleIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("ID del lector: ");
        int readerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Valoración (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        articleService.addRating(newspaperId, articleIndex, readerId, rating);
        System.out.println("✅ Valoración añadida correctamente");
    }

    // 10. Modify rating of an Article
    public void modifyRating() throws ArticleNotFoundException {
        System.out.println("\n✏️ ═══════════ MODIFICAR VALORACIÓN ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Índice del artículo (0, 1, 2...): ");
        int articleIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("ID del lector: ");
        int readerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Nueva valoración (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());

        articleService.modifyRating(newspaperId, articleIndex, readerId, rating);
        System.out.println("✅ Valoración modificada correctamente");
    }

    // 11. Delete rating of an Article
    public void deleteRating() throws ArticleNotFoundException {
        System.out.println("\n🗑️ ═══════════ ELIMINAR VALORACIÓN ═══════════");
        System.out.print("ID del periódico: ");
        String newspaperId = scanner.nextLine();
        System.out.print("Índice del artículo (0, 1, 2...): ");
        int articleIndex = Integer.parseInt(scanner.nextLine());
        System.out.print("ID del lector: ");
        int readerId = Integer.parseInt(scanner.nextLine());

        articleService.deleteRating(newspaperId, articleIndex, readerId);
        System.out.println("✅ Valoración eliminada correctamente");
    }

}

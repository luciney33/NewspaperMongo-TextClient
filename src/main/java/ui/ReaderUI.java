package ui;

import domain.service.ArticleService;
import domain.service.ReaderService;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Scanner;

@ApplicationScoped
public class ReaderUI {
    Scanner scanner = new Scanner(System.in);
    private final ReaderService readerService;
    private final ArticleService articleService;

    public ReaderUI() {
        this.readerService = new ReaderService();
        this.articleService = new ArticleService();
    }

//
//    public void getReaders() {
//        List<ReaderEntity> readers = readerService.getAllReaders();
//        System.out.println("\n👥 ═══════════ TODOS LOS LECTORES ═══════════");
//        if (readers.isEmpty()) {
//            System.out.println("No hay lectores registrados.");
//        } else {
//            for (ReaderEntity r : readers) {
//                System.out.printf("ID: %d | Nombre: %s | Fecha nacimiento: %s | Suscripciones: %d%n",
//                        r.getId(), r.getName(), r.getDob(), r.getSubscriptions().size());
//            }
//        }
//    }
//
//    // 7. Get Readers of an Article
//    public void getReadersByArticle() throws ArticleNotFoundException {
//        System.out.println("\n👥 ═══════════ LECTORES DE UN ARTÍCULO ═══════════");
//        System.out.print("ID del periódico: ");
//        String newspaperId = scanner.nextLine();
//        System.out.print("Índice del artículo (0, 1, 2...): ");
//        int index = Integer.parseInt(scanner.nextLine());
//
//////        List<ReadArticleDTO> readers = articleService.getReadersOfArticle(newspaperId, index);
////        if (readers.isEmpty()) {
////            System.out.println("Este artículo no tiene valoraciones de lectores.");
////        } else {
////            for (ReadArticleDTO r : readers) {
////                System.out.printf("%nID: %d | Nombre: %s%n", r.getIdReader(), r.getNameReader());
////                System.out.printf("Fecha nacimiento: %s | Rating: %d%n", r.getDobReader(), r.getRating());
////                System.out.printf("Suscripciones: %s%n", String.join(", ", r.getSubscriptionsReader()));
////            }
////        }
//    }
//
//    // 8. Get Reader by id
//    public void getReaderById() throws ReaderNotFoundException {
//        System.out.println("\n🔍 ═══════════ BUSCAR LECTOR ═══════════");
//        System.out.print("ID del lector: ");
//        int id = Integer.parseInt(scanner.nextLine());
//
//        ReaderEntity reader = readerService.getReaderById(id);
//        System.out.printf("%nID: %d%n", reader.getId());
//        System.out.printf("Nombre: %s%n", reader.getName());
//        System.out.printf("Fecha nacimiento: %s%n", reader.getDob());
//        System.out.printf("Suscripciones: %d%n", reader.getSubscriptions().size());
//    }
//    // 13. Add new reader
////    public void anadirLector() {
////        System.out.println("\n➕ ═══════════ AÑADIR LECTOR ═══════════");
////        System.out.print("Nombre: ");
////        String name = scanner.nextLine();
////        System.out.print("Fecha de nacimiento (YYYY/MM/DD): ");
////        String dob = scanner.nextLine();
////        System.out.print("Nombre de usuario: ");
////        String username = scanner.nextLine();
////        System.out.print("Contraseña: ");
////        String password = scanner.nextLine();
////
////        readerService.addReader(name, dob, username, password);
////        System.out.println("✅ Lector añadido correctamente");
////    }
//
//    // 14. Delete reader
//    public void deleteReader() throws ReaderNotFoundException {
//        System.out.println("\n🗑️ ═══════════ ELIMINAR LECTOR ═══════════");
//        System.out.print("ID del lector: ");
//        int id = Integer.parseInt(scanner.nextLine());
//
//        readerService.deleteReader(id);
//        System.out.println("✅ Lector eliminado correctamente");
//    }

}

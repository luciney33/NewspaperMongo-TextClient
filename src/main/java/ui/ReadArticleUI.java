package ui;

import domain.error.AppError;
import domain.error.DataBaseError;
import domain.model.ReadArticleDTO;
import domain.model.ReaderDTO;
import domain.service.ReadArticleService;
import domain.service.ReaderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class ReadArticleUI {
    Scanner sc = new Scanner(System.in);
    private final ReadArticleService readArticleService;
    private final ReaderService readerService;

    @Inject
    public ReadArticleUI(ReadArticleService readArticleService, ReaderService readerService) {
        this.readArticleService = readArticleService;
        this.readerService = readerService;
    }

    public void getReadersByArticle() {
        System.out.println("\n═══════════ LECTORES DE UN ARTICULO ═══════════");
        System.out.print("Descripción del articulo: ");
        String description = sc.nextLine();

        List<ReadArticleDTO> readarticles = readArticleService.getReadersByArticle(description);

        if (readarticles.isEmpty()) {
            System.out.println("Este articulo no tiene valoraciones de lectores");
        } else {
            System.out.println("\n----- Lectores que han leído este artículo -----");
            for (ReadArticleDTO redeartcledto : readarticles) {
                System.out.println(redeartcledto.toString());
            }
        }
    }

    public void addRating() {
        System.out.println("\n ═══════════ AÑADIR RATING ═══════════");
        try {
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("No hay lectores registrados");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID MongoDB: " + readers.get(i).getId());
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("Escribe la descripcion del articulo: ");
            String articleDescription = sc.nextLine().trim();

            if (articleDescription.isEmpty()) {
                System.out.println("La descripcion del articulo no puede estar vacio");
                return;
            }

            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(sc.nextLine());

            if (rating < 1 || rating > 5) {
                System.out.println("El rating debe ser entre 1 y 5");
                return;
            }

            ReadArticleDTO dto = new ReadArticleDTO(selectedReader.getId(), 0, "", null, null, rating);
            int result = readArticleService.addRating(dto, articleDescription);

            if (result > 0) {
                System.out.println("Guardado correctamente");
            } else {
                System.out.println("Error al añadir el nuevo rating");
            }

        } catch (NumberFormatException e) {
            System.out.println("Input invalido, añade numeros");
        } catch (DataBaseError e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (AppError e) {
            System.out.println("Application error: " + e.getMessage());
        }
    }

    public void modifyRating() {
        System.out.println("\n═══════════ MODIFICAR RATING ═══════════");
        try {
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("No hay lectores registrado");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() +
                        " (ID MongoDB: " + readers.get(i).getId());
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("Escribe la descripcion del articulo: ");
            String articleDescription = sc.nextLine().trim();

            if (articleDescription.isEmpty()) {
                System.out.println("La descripcion del articulo no puede estar vacio");
                return;
            }

            System.out.print("Rating (1-5): ");
            int newRating = Integer.parseInt(sc.nextLine());

            if (newRating < 1 || newRating > 5) {
                System.out.println("El rating debe ser entre 1 y 5");
                return;
            }

            ReadArticleDTO dto = new ReadArticleDTO(selectedReader.getId(), 0, "", null, null, newRating);

            readArticleService.modifyRating(dto, articleDescription);
            System.out.println("Actualizado correctamente");

        } catch (DataBaseError e) {
            System.out.println("Error actualizando rating: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteRating() {
        System.out.println("\n═══════════ ELIMINAR RATING ═══════════");
        try {
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("No hay lectores registrados");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() +
                        " (ID MongoDB: " + readers.get(i).getId());
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("Escribe la descripcion del articulo: ");
            String articleDescription = sc.nextLine().trim();

            if (articleDescription.isEmpty()) {
                System.out.println("La descripcion del articulo no puede estar vacio");
                return;
            }

            ReadArticleDTO dto = new ReadArticleDTO(selectedReader.getId(), 0, "", null, null, 0);

            boolean deleted = readArticleService.deleteRating(dto, articleDescription);
            if (deleted) {
                System.out.println("Eliminado correctamente");
            } else {
                System.out.println("Rating no encontrado");
            }

        } catch (DataBaseError e) {
            System.out.println("Error al eliminar rating: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


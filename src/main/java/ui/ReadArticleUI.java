package ui;

import domain.error.AppError;
import domain.error.DataBaseError;
import domain.model.ReaderDTO;
import domain.service.ReadArticleService;
import domain.service.ReaderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

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


    public void addRating() {
        System.out.println("\n⭐ ═══════════ AÑADIR RATING ═══════════");
        try {
            // Mostrar lista de readers disponibles
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("❌ No hay lectores registrados.");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID: " + readers.get(i).getId() + ")");
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("❌ Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(sc.nextLine());

            if (rating < 1 || rating > 5) {
                System.out.println("❌ El rating debe estar entre 1 y 5.");
                return;
            }

            int result = readArticleService.addRating(selectedReader.getId(), rating);

            if (result > 0) {
                System.out.println("✅ Rating añadido correctamente");
            } else if (result == -2) {
                System.out.println("⚠ Ya existe un rating para este lector");
            } else {
                System.out.println("❌ No se pudo añadir el rating");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Ingrese valores numéricos.");
        } catch (DataBaseError e) {
            System.out.println("❌ Error de base de datos: " + e.getMessage());
        } catch (AppError e) {
            System.out.println("❌ Error de aplicación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
        }
    }

    public void modifyRating() {
        System.out.println("\n📝 ═══════════ MODIFICAR RATING ═══════════");
        try {
            // Mostrar lista de readers disponibles
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("❌ No hay lectores registrados.");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID: " + readers.get(i).getId() + ")");
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("❌ Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("Nuevo rating (1-5): ");
            int newRating = Integer.parseInt(sc.nextLine());

            if (newRating < 1 || newRating > 5) {
                System.out.println("❌ El rating debe estar entre 1 y 5.");
                return;
            }

            readArticleService.modifyRating(selectedReader.getId(), newRating);
            System.out.println("✅ Rating modificado correctamente");

        } catch (DataBaseError e) {
            System.out.println("❌ Error al modificar el rating: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public void deleteRating() {
        System.out.println("\n🗑️ ═══════════ ELIMINAR RATING ═══════════");
        try {
            // Mostrar lista de readers disponibles
            List<ReaderDTO> readers = readerService.getAllReaders();

            if (readers.isEmpty()) {
                System.out.println("❌ No hay lectores registrados.");
                return;
            }

            System.out.println("\n----- Lectores disponibles -----");
            for (int i = 0; i < readers.size(); i++) {
                System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID: " + readers.get(i).getId() + ")");
            }

            System.out.print("\nSeleccione el número del lector: ");
            int selection = Integer.parseInt(sc.nextLine());

            if (selection < 1 || selection > readers.size()) {
                System.out.println("❌ Selección inválida");
                return;
            }

            ReaderDTO selectedReader = readers.get(selection - 1);

            System.out.print("⚠ ¿Está seguro de eliminar el rating? (s/n): ");
            String confirmacion = sc.nextLine().trim().toLowerCase();

            if (!confirmacion.equals("s") && !confirmacion.equals("si") && !confirmacion.equals("yes")) {
                System.out.println("⚠ Operación cancelada");
                return;
            }

            boolean deleted = readArticleService.deleteRating(selectedReader.getId());
            if (deleted) {
                System.out.println("✅ Rating eliminado correctamente");
            } else {
                System.out.println("❌ Rating no encontrado");
            }

        } catch (DataBaseError e) {
            System.out.println("❌ Error al eliminar el rating: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}

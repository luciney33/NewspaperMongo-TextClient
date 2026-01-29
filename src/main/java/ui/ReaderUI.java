package ui;

import domain.model.NewspaperDTO;
import domain.model.ReaderDTO;
import domain.service.ArticleService;
import domain.service.ReaderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class ReaderUI {
    Scanner scanner = new Scanner(System.in);
    private final ReaderService readerService;
    private final ArticleService articleService;

    @Inject
    public ReaderUI(ReaderService readerService, ArticleService articleService) {
        this.readerService = readerService;
        this.articleService = articleService;
    }


    public void getAllReaders() {
        List<ReaderDTO> readers = readerService.getAllReaders();
        System.out.println("\n ══════════════════════════════════════════");
        if (readers.isEmpty()) {
            System.out.println("No hay readers registrados.");
        } else {
            for (ReaderDTO r : readers) {
                System.out.println(r.toString());
            }
        }
        System.out.println("═══════════════════════════════════════════════\n");
    }

    public void getReadersByArticle() {
        System.out.println("\n👥 ═══════════ LECTORES DE UN ARTÍCULO ═══════════");
        System.out.print("Descripción del artículo: ");
        String description = scanner.nextLine();

        List<ReaderDTO> readers = readerService.getReadersByArticle(description);

        if (readers.isEmpty()) {
            System.out.println("Este artículo no tiene valoraciones de lectores.");
        } else {
            System.out.println("\n----- Lectores que han leído este artículo -----");
            for (ReaderDTO reader : readers) {
                System.out.println(reader.toString());
            }
        }
    }

    // 8. Get Reader by name
    public void getReaderById() {
        System.out.println("\n🔍 ═══════════ BUSCAR LECTOR ═══════════");
        System.out.print("Nombre del lector: ");
        String name = scanner.nextLine();

        try {
            ReaderDTO reader = readerService.getReaderByName(name);
            System.out.println("\n" + reader.toString());
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // 13. Add new reader
    public void addReader() {
        try {
            System.out.println("\n➕ ═══════════ AÑADIR LECTOR ═══════════");

            // Validar nombre
            String name;
            while (true) {
                System.out.print("Nombre: ");
                name = scanner.nextLine().trim();
                if (!name.isEmpty()) {
                    break;
                }
                System.out.println("⚠ El nombre no puede estar vacío.");
            }

            // Validar fecha de nacimiento
            String dob;
            while (true) {
                System.out.print("Fecha de nacimiento (YYYY/MM/DD): ");
                dob = scanner.nextLine().trim();
                if (!dob.isEmpty() && dob.matches("\\d{4}/\\d{2}/\\d{2}")) {
                    break;
                }
                System.out.println("⚠ Formato inválido. Use YYYY/MM/DD.");
            }

            // Preguntar si quiere añadir credenciales
            boolean addCredentials;
            while (true) {
                System.out.print("¿Desea añadir credenciales para este lector? (s/n): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("s") || answer.equals("si") || answer.equals("yes")) {
                    addCredentials = true;
                    break;
                } else if (answer.equals("n") || answer.equals("no")) {
                    addCredentials = false;
                    break;
                } else {
                    System.out.println("⚠ Por favor responda 's' o 'n'.");
                }
            }

            String username = "";
            String password = "";

            if (addCredentials) {
                // Validar username
                while (true) {
                    System.out.print("Nombre de usuario: ");
                    username = scanner.nextLine().trim();
                    if (!username.isEmpty()) {
                        break;
                    }
                    System.out.println("⚠ El nombre de usuario no puede estar vacío.");
                }

                // Validar password
                while (true) {
                    System.out.print("Contraseña: ");
                    password = scanner.nextLine().trim();
                    if (!password.isEmpty()) {
                        break;
                    }
                    System.out.println("⚠ La contraseña no puede estar vacía.");
                }

                // Confirmar
                System.out.print("¿Confirma la creación del lector con credenciales? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                boolean confirmation = respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("yes");

                int result = readerService.addReader(name, dob, username, password, confirmation);

                if (result > 0) {
                    System.out.println("✅ Lector añadido correctamente con credenciales");
                } else {
                    System.out.println("❌ No se pudo añadir el lector");
                }
            } else {
                System.out.println("⚠ Reader creado sin credenciales (solo se guardará en MongoDB)");
                // Crear reader sin credenciales (pasamos credenciales vacías y confirmation false)
                int result = readerService.addReader(name, dob, "", "", false);

                if (result > 0) {
                    System.out.println("✅ Lector añadido correctamente");
                } else {
                    System.out.println("❌ No se pudo añadir el lector");
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error al crear lector: " + e.getMessage());
        }
    }

    // 14. Delete reader
    public void deleteReader() {
        System.out.println("\n🗑️ ═══════════ ELIMINAR LECTOR ═══════════");

        // Mostrar lista de lectores disponibles
        List<ReaderDTO> readers = readerService.getAllReaders();

        if (readers.isEmpty()) {
            System.out.println("No hay lectores registrados.");
            return;
        }

        System.out.println("\n----- Lectores disponibles -----");
        for (int i = 0; i < readers.size(); i++) {
            System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID: " + readers.get(i).getId() + ")");
        }

        // Solicitar número o nombre del lector
        System.out.print("\nSeleccione el número del lector o escriba su nombre: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("❌ Entrada inválida");
            return;
        }

        ReaderDTO readerToDelete = null;

        // Intentar parsear como número
        try {
            int number = Integer.parseInt(input);
            if (number >= 1 && number <= readers.size()) {
                readerToDelete = readers.get(number - 1);
            } else {
                System.out.println("❌ Número fuera de rango");
                return;
            }
        } catch (NumberFormatException e) {
            // No es un número, buscar por nombre
            for (ReaderDTO reader : readers) {
                if (reader.getName().equalsIgnoreCase(input)) {
                    readerToDelete = reader;
                    break;
                }
            }

            if (readerToDelete == null) {
                System.out.println("❌ Lector con nombre '" + input + "' no encontrado");
                return;
            }
        }

        // Confirmar eliminación
        System.out.print("⚠ ¿Está seguro de eliminar el lector '" + readerToDelete.getName() + "' (ID: " + readerToDelete.getId() + ")? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        boolean confirmation = respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("yes");

        try {
            int result = readerService.deleteReader(readerToDelete.getName(), readerToDelete.getId(), confirmation);

            if (result > 0) {
                System.out.println("✅ Lector eliminado correctamente");
            } else {
                System.out.println("❌ No se pudo eliminar el lector");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

}

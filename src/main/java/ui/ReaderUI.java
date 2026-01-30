package ui;

import domain.model.ReaderDTO;
import domain.service.ReaderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class ReaderUI {
    Scanner scanner = new Scanner(System.in);
    private final ReaderService readerService;

    @Inject
    public ReaderUI(ReaderService readerService) {
        this.readerService = readerService;
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


    public void getReaderById() {
        System.out.print("Nombre del lector: ");
        String name = scanner.nextLine();

        try {
            ReaderDTO reader = readerService.getReaderByName(name);
            System.out.println("\n" + reader.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addReader() {
        try {
            System.out.println("\n═══════════ AÑADIR LECTOR ═══════════");

            String name;
            while (true) {
                System.out.print("Nombre: ");
                name = scanner.nextLine().trim();
                if (!name.isEmpty()) {
                    break;
                }
                System.out.println("El nombre no puede estar vacío");
            }

            String dob;
            while (true) {
                System.out.print("Fecha de nacimiento (YYYY/MM/DD): ");
                dob = scanner.nextLine().trim();
                if (dob.length() == 10 && dob.charAt(4) == '/' && dob.charAt(7) == '/') {
                    break;
                }
                System.out.println("Formato inválido.  YYYY/MM/DD");
            }
            boolean addCredentials;
            while (true) {
                System.out.print("añadir credenciales para este lector? (s/n): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("s") || answer.equals("si") || answer.equals("yes")) {
                    addCredentials = true;
                    break;
                } else if (answer.equals("n") || answer.equals("no")) {
                    addCredentials = false;
                    break;
                } else {
                    System.out.println("Por favor responda 's' o 'n'");
                }
            }

            String username;
            String password;

            if (addCredentials) {
                while (true) {
                    System.out.print("Nombre de usuario: ");
                    username = scanner.nextLine().trim();
                    if (!username.isEmpty()) {
                        break;
                    }
                    System.out.println("El nombre de usuario no puede estar vacío");
                }

                while (true) {
                    System.out.print("Contraseña: ");
                    password = scanner.nextLine().trim();
                    if (!password.isEmpty()) {
                        break;
                    }
                    System.out.println("La contraseña no puede estar vacía");
                }

                System.out.print("Confirmala creación del lector con credenciales? (s/n): ");
                String respuesta = scanner.nextLine().trim().toLowerCase();
                boolean confirmation = respuesta.equals("s") || respuesta.equals("si");

                int result = readerService.addReader(name, dob, username, password, confirmation);

                if (result > 0) {
                    System.out.println("Lector añadido correctamente con credenciales");
                } else {
                    System.out.println("No se pudo añadir el lector");
                }
            } else {
                System.out.println("Reader creado sin credenciales");
                int result = readerService.addReader(name, dob, "", "", false);

                if (result > 0) {
                    System.out.println("Lector añadido correctamente");
                } else {
                    System.out.println("No se pudo añadir el lector");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al crear lector: " + e.getMessage());
        }
    }

    public void deleteReader() {
        System.out.println("\n═══════════ ELIMINAR LECTOR ═══════════");

        List<ReaderDTO> readers = readerService.getAllReaders();

        if (readers.isEmpty()) {
            System.out.println("No hay lectores registrados");
            return;
        }

        System.out.println("\n----- Lectores disponibles -----");
        for (int i = 0; i < readers.size(); i++) {
            System.out.println((i + 1) + ". " + readers.get(i).getName() + " (ID: " + readers.get(i).getId() + ")");
        }

        System.out.print("\nSeleccione el número del lector o escriba su nombre: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("Entrada inválida");
            return;
        }

        ReaderDTO readerToDelete = null;

        try {
            int number = Integer.parseInt(input);
            if (number >= 1 && number <= readers.size()) {
                readerToDelete = readers.get(number - 1);
            } else {
                System.out.println("Número fuera de rango");
                return;
            }
        } catch (NumberFormatException e) {
            for (ReaderDTO reader : readers) {
                if (reader.getName().equalsIgnoreCase(input)) {
                    readerToDelete = reader;
                    break;
                }
            }

            if (readerToDelete == null) {
                System.out.println("Lector con nombre '" + input + "' no encontrado");
                return;
            }
        }

        System.out.print("seguro de eliminar el lector '" + readerToDelete.getName() + "' (ID: " + readerToDelete.getId() + ")? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        boolean confirmation = respuesta.equals("s") || respuesta.equals("si");

        try {
            int result = readerService.deleteReader(readerToDelete.getName(), readerToDelete.getId(), confirmation);

            if (result > 0) {
                System.out.println("Lector eliminado correctamente");
            } else {
                System.out.println("No se pudo eliminar el lector");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}

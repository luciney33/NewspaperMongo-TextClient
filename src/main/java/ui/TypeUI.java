package ui;

import domain.model.TypeDTO;
import domain.service.TypeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TypeUI {
    private final TypeService typeService;

    @Inject
    public TypeUI(TypeService typeService) {
        this.typeService = typeService;
    }

    public void getAllTypes() {
        System.out.println("\n═══════════ TODOS LOS TIPOS ═══════════");

        try {
            List<TypeDTO> types = typeService.getAllTypes();

            if (types.isEmpty()) {
                System.out.println("No hay tipos registrados");
                return;
            }

            System.out.println("\n----- Tipos de artículos disponibles -----");
            for (int i = 0; i < types.size(); i++) {
                System.out.println((i + 1) + ". " + types.get(i).getName());
            }
            System.out.println("\nTotal de tipos: " + types.size());

        } catch (Exception e) {
            System.out.println("Error al obtener tipos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


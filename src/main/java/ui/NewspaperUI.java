package ui;

import domain.model.NewspaperDTO;
import domain.service.NewspaperService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class NewspaperUI {
    private NewspaperService newspaperService;
    public NewspaperUI() {}

    @Inject
    public NewspaperUI(NewspaperService newspaperService) {
        this.newspaperService = newspaperService;
    }

    public void getNewspapers() {
        List<NewspaperDTO> newspapers = newspaperService.getAllNewspapers();
        System.out.println("\n📰 ═══════════ TODOS LOS PERIÓDICOS ═══════════");
        if (newspapers.isEmpty()) {
            System.out.println("No hay periódicos registrados.");
        } else {
            for (NewspaperDTO n : newspapers) {
                System.out.printf("ID: %s | Nombre: %s%n",
                        n.getId().toString(), n.getName());
            }
        }
    }
}

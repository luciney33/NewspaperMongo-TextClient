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
        System.out.println("\n ══════════════════════════════════════════");
        if (newspapers.isEmpty()) {
            System.out.println("No hay periodicos registrados.");
        } else {
            for (NewspaperDTO n : newspapers) {
                System.out.println(n.toString());
            }
        }
        System.out.println("═══════════════════════════════════════════════\n");
    }
}

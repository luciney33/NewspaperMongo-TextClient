package ui;

import org.example.dao.model.NewspaperEntity;
import org.example.domain.service.NewspaperService;

import java.util.List;
public class NewspaperUI {
    private final NewspaperService newspaperService;

    public NewspaperUI() {
        this.newspaperService = new NewspaperService();
    }



    public void getNewspapers() {
        List<NewspaperEntity> newspapers = newspaperService.getAllNewspapers();
        System.out.println("\n📰 ═══════════ TODOS LOS PERIÓDICOS ═══════════");
        if (newspapers.isEmpty()) {
            System.out.println("No hay periódicos registrados.");
        } else {
            for (NewspaperEntity n : newspapers) {
                System.out.printf("ID: %s | Nombre: %s | Artículos: %d%n",
                        n.get_id().toString(), n.getName(), n.getArticles().size());
            }
        }
    }
    // 12. Get all Types
    public void getAllTypes() {
        List<String> types = newspaperService.getAllTypes();
        System.out.println("\n📋 ═══════════ TODOS LOS TIPOS ═══════════");
        if (types.isEmpty()) {
            System.out.println("No hay tipos registrados.");
        } else {
            for (int i = 0; i < types.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, types.get(i));
            }
        }
    }
}

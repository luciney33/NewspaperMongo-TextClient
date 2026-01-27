package domain.service;

import dao.NewspaperDAO;
import dao.model.NewspaperEntity;

import java.util.List;

public class NewspaperService {
    private final NewspaperDAO newspaperDAO;

    public NewspaperService() {
        this.newspaperDAO = new NewspaperDAO();
    }

    // 5. Get all Newspapers
    public List<NewspaperEntity> getAllNewspapers() {
        return newspaperDAO.findAll();
    }

    // 12. Get all Types
    public List<String> getAllTypes() {
        return newspaperDAO.getAllTypes();
    }
}


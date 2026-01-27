package domain.service;

import dao.NewspaperDAO;
import domain.mappers.MapNewsDtoEntity;
import domain.model.NewspaperDTO;

import java.util.List;

public class NewspaperService {
    private final NewspaperDAO newspaperDAO;
    private final MapNewsDtoEntity mapper;

    public NewspaperService() {
        this.newspaperDAO = new NewspaperDAO();
        this.mapper = new MapNewsDtoEntity();
    }

    // 5. Get all Newspapers
    public List<NewspaperDTO> getAllNewspapers() {
        return mapper.entityListToDtoList(newspaperDAO.findAll());
    }

    // 12. Get all Types
    public List<String> getAllTypes() {
        return newspaperDAO.getAllTypes();
    }
}


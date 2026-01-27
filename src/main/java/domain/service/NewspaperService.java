package domain.service;

import dao.NewspaperRepository;
import domain.mappers.MapNewsDtoEntity;
import domain.model.NewspaperDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class NewspaperService {
    private NewspaperRepository newspaperRepository;
    private MapNewsDtoEntity mapper;

    public NewspaperService() {}

    @Inject
    public NewspaperService(NewspaperRepository newspaperRepository) {
        this.newspaperRepository = newspaperRepository;
        this.mapper = new MapNewsDtoEntity();
    }

    // 5. Get all Newspapers
    public List<NewspaperDTO> getAllNewspapers() {
        return mapper.entityListToDtoList(newspaperRepository.getAll());
    }

}


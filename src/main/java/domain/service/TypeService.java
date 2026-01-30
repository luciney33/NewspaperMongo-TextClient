package domain.service;

import dao.TypeRepository;
import dao.model.TypeEntity;
import domain.model.TypeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TypeService {
    private TypeRepository typeRepository;

    @Inject
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public TypeService() {}

    public List<TypeDTO> getAllTypes() {
        List<TypeEntity> entities = typeRepository.getAllTypes();
        return entities.stream()
                .map(entity -> new TypeDTO(entity.getName()))
                .collect(Collectors.toList());
    }
}


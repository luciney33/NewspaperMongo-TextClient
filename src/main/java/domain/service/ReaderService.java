package domain.service;

import dao.NewspaperDAO;
import dao.ReaderDAO;
import dao.ReaderRepository;
import dao.model.CredentialEntity;
import dao.model.ReaderEntity;
import domain.error.ReaderNotFoundException;
import domain.mappers.MapReaderDtoEntity;
import domain.model.ReaderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ReaderService {
    private ReaderRepository readerRepository;
    private MapReaderDtoEntity mapper;
    private NewspaperDAO newspaperDAO;

    @Inject
    public ReaderService(ReaderRepository readerRepository,MapReaderDtoEntity mapper, NewspaperDAO newspaperDAO) {
        this.readerRepository = readerRepository;
        this.mapper = mapper;
        this.newspaperDAO = newspaperDAO;
    }
    public ReaderService() {}



    public List<ReaderDTO> getAllReaders() {
        return mapper.entityListToDtoList(readerRepository.getAll());
    }

    public List<ReaderDTO> getReadersByArticle(String articleDescription) {
        return mapper.entityListToDtoList(readerRepository.getAllByArticle(articleDescription));
    }

    // 8. Get Reader by name
    public ReaderDTO getReaderByName(String name) throws ReaderNotFoundException {
        ReaderEntity reader = readerRepository.get(name);
        if (reader == null) {
            throw new ReaderNotFoundException("Lector con nombre '" + name + "' no encontrado");
        }
        return mapper.entityToDto(reader);
    }

    // 13. Add new reader, including credentials
    public int addReader(String name, String dob, String username, String password, boolean confirmation) {
        // Crear el reader entity
        ReaderEntity reader = ReaderEntity.builder()
                .name(name)
                .dob(dob)
                .build();

        // Crear el credential entity
        CredentialEntity credential = new CredentialEntity(username, password, 0);

        // Guardar reader y credenciales
        return readerRepository.save(reader, credential, confirmation);
    }

    // 14. Delete reader
    public int deleteReader(String name, boolean confirmation) throws ReaderNotFoundException {
        ReaderEntity reader = readerRepository.get(name);
        if (reader == null) {
            throw new ReaderNotFoundException("Lector con nombre '" + name + "' no encontrado");
        }
        return readerRepository.delete(reader, confirmation);
    }
}


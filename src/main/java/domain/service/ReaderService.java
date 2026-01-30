package domain.service;

import dao.ReaderRepository;
import dao.model.CredentialEntity;
import dao.model.ReaderEntity;
import domain.error.ReaderNotFoundException;
import domain.mappers.MapReaderDtoEntity;
import domain.model.ReaderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class ReaderService {
    private ReaderRepository readerRepository;
    private MapReaderDtoEntity mapper;

    @Inject
    public ReaderService(ReaderRepository readerRepository,MapReaderDtoEntity mapper) {
        this.readerRepository = readerRepository;
        this.mapper = mapper;
    }
    public ReaderService() {}



    public List<ReaderDTO> getAllReaders() {
        return mapper.entityListToDtoList(readerRepository.getAll());
    }

    public ReaderDTO getReaderByName(String name) throws ReaderNotFoundException {
        ReaderEntity reader = readerRepository.get(name);
        if (reader == null) {
            throw new ReaderNotFoundException("Lector con nombre '" + name + "' no encontrado");
        }
        return mapper.entityToDto(reader);
    }

    public int addReader(String name, String dob, String username, String password, boolean confirmation) {
        ReaderEntity reader = ReaderEntity.builder()
                .name(name)
                .dob(dob)
                .build();
        CredentialEntity credential = new CredentialEntity(username, password, 0);
        return readerRepository.save(reader, credential, confirmation);
    }

    public int deleteReader(String name, ObjectId id, boolean confirmation) throws ReaderNotFoundException {
        ReaderEntity reader = ReaderEntity.builder()
                .id(id)
                .name(name)
                .build();
        return readerRepository.delete(reader, confirmation);
    }
}


package domain.service;

import dao.NewspaperDAO;
import dao.ReaderDAO;
import dao.model.ReaderEntity;
import domain.error.ReaderNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReaderService {
//    private final ReaderDAO readerDAO;
//    private final NewspaperDAO newspaperDAO;
//
//    public ReaderService() {
//        this.readerDAO = new ReaderDAO();
//        this.newspaperDAO = new NewspaperDAO();
//    }
//
//    // 6. Get all Readers
//    public List<ReaderEntity> getAllReaders() {
//        return readerDAO.findAll();
//    }
//
//    // 8. Get Reader by id
//    public ReaderEntity getReaderById(int id) throws ReaderNotFoundException {
//        ReaderEntity reader = readerDAO.findById(id);
//        if (reader == null) {
//            throw new ReaderNotFoundException("Lector con ID " + id + " no encontrado");
//        }
//        return reader;
//    }
//
////    // 13. Add new reader, including credentials
////    public void addReader(String name, String dob, String username, String password) {
////        int newId = readerDAO.getNextReaderId();
////
////        ReaderEntity reader = ReaderEntity.builder()
////            ._id(newId)
////            .name(name)
////            .dob(dob)
////            .username(username)
////            .password(password)
////            .subscriptions(new ArrayList<>())
////            .build();
////
////        readerDAO.addReader(reader);
////    }
//
//    // 14. Delete reader
//    public void deleteReader(int id) throws ReaderNotFoundException {
//        ReaderEntity reader = readerDAO.findById(id);
//        if (reader == null) {
//            throw new ReaderNotFoundException("Lector con ID " + id + " no encontrado");
//        }
//        readerDAO.deleteReader(id);
//    }
}


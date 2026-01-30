package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dao.ReaderRepository;
import dao.mapper.ReaderEntityMapper;
import dao.model.CredentialEntity;
import dao.model.ReaderEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import util.DBconnection;
import util.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReaderRepositoryImp implements ReaderRepository {
    private MongoCollection<Document> collection;
    private MongoDatabase database;
    private DBconnection dbConnection;

    public ReaderRepositoryImp() {
    }

    @Inject
    public ReaderRepositoryImp(MongoCollection<Document> collection, MongoDatabase database,DBconnection dbConnection) {
        this.collection = collection;
        this.database = database;
        this.dbConnection = dbConnection;
    }

    @Override
    public List<ReaderEntity> getAll() {
        List<ReaderEntity> readers = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                ReaderEntity reader = ReaderEntityMapper.documentToEntity(doc);
                readers.add(reader);
            }
        }

        return readers;
    }

    @Override
    public ReaderEntity get(String name) {
        Document query = new Document("name", name);

        try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
            if (cursor.hasNext()) {
                Document doc = cursor.next();
                return ReaderEntityMapper.documentToEntity(doc);
            }
        }

        return null;
    }

    @Override
    public int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation) {
        ObjectId readerId = null;

        try {
            Document readerDoc = ReaderEntityMapper.entityToDocument(reader);
            collection.insertOne(readerDoc);

            readerId = readerDoc.getObjectId("_id");
            System.out.println("Reader guardado correctamente con ID: " + readerId);

            if (confirmation && credential != null &&
                credential.getUsername() != null && !credential.getUsername().isEmpty() &&
                credential.getPassword() != null && !credential.getPassword().isEmpty()) {

                try (Connection con = dbConnection.getConnection()) {
                    con.setAutoCommit(false);

                    try (PreparedStatement psReader = con.prepareStatement(Query.InsertReader);
                         PreparedStatement psCredential = con.prepareStatement(Query.InsertCredential)) {
                        int idReaderForMySQL = readerId.hashCode();
                        psReader.setInt(1, idReaderForMySQL);
                        psReader.setString(2, reader.getName());
                        psReader.setString(3, reader.getDob());
                        psReader.executeUpdate();
                        psCredential.setString(1, credential.getUsername());
                        psCredential.setString(2, credential.getPassword());
                        psCredential.setInt(3, idReaderForMySQL);
                        psCredential.executeUpdate();

                        con.commit();
                        System.out.println("Reader y credenciales guardados correctamente: " + idReaderForMySQL);

                    } catch (SQLException e) {
                        con.rollback();
                        System.err.println("Error al guardar en MySQL: " + e.getMessage());
                        System.out.println("El reader se guardó correctamente");
                    } finally {
                        con.setAutoCommit(true);
                    }

                } catch (Exception mysqlError) {
                    System.out.println("MySQL no disponible. Reader guardado ");
                }
            } else {
                System.out.println("Reader creado sin credenciales");
            }

            return 1;

        } catch (Exception e) {
            System.err.println("Error al guardar reader: " + e.getMessage());
            e.printStackTrace();
            if (readerId != null) {
                try {
                    collection.deleteOne(new Document("_id", readerId));
                    System.out.println("Reader eliminado");
                } catch (Exception rollbackError) {
                    System.err.println("Error :  " + rollbackError.getMessage());
                }
            }

            return 0;
        }
    }

    @Override
    public int delete(ReaderEntity reader, boolean confirmation) {
        if (!confirmation) {
            System.out.println("Operación de eliminación cancelada por el usuario");
            return 0;
        }

        if (reader == null || reader.getId() == null) {
            System.err.println("Reader inválido para eliminar");
            return 0;
        }

        try {
            Document query = new Document("_id", reader.getId());
            long deletedCount = collection.deleteOne(query).getDeletedCount();

            if (deletedCount == 0) {
                System.err.println("Reader no encontrado");
                return 0;
            }

            System.out.println("Reader eliminado correctamente con ID: " + reader.getId());
            try (Connection con = dbConnection.getConnection()) {
                con.setAutoCommit(false);

                try (PreparedStatement psCredential = con.prepareStatement(Query.DeleteCredential);
                     PreparedStatement psReader = con.prepareStatement(Query.DeleteReader)) {
                    int idReaderForMySQL = reader.getId().hashCode();

                    psCredential.setInt(1, idReaderForMySQL);
                    int credentialsDeleted = psCredential.executeUpdate();

                    psReader.setInt(1, idReaderForMySQL);
                    int readerDeleted = psReader.executeUpdate();

                    con.commit();

                    if (credentialsDeleted > 0 || readerDeleted > 0) {
                        System.out.println("Reader y credenciales eliminados");
                    } else {
                        System.out.println("Reader no existía");
                    }

                } catch (SQLException e) {
                    con.rollback();
                    System.err.println("Error al elimina: " + e.getMessage());
                    System.out.println("El reader se eliminó");
                } finally {
                    con.setAutoCommit(true);
                }

            } catch (Exception mysqlError) {
                System.out.println("MySQL no disponible. Reader eliminado");
            }

            return 1;

        } catch (Exception e) {
            System.err.println("Error al eliminar reader: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}

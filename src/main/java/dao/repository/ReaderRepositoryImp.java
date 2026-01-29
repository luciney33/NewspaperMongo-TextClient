package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dao.CredentialRepository;
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
    private CredentialRepository credentialRepository;
    private DBconnection dbConnection;

    public ReaderRepositoryImp() {
    }

    @Inject
    public ReaderRepositoryImp(MongoCollection<Document> collection, MongoDatabase database,
                               CredentialRepository credentialRepository, DBconnection dbConnection) {
        this.collection = collection;
        this.database = database;
        this.credentialRepository = credentialRepository;
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
    public List<ReaderEntity> getAllByArticle(String description) {
        List<ReaderEntity> readers = new ArrayList<>();
        MongoCollection<Document> newspapersCollection = database.getCollection("Newspapers");
        List<ObjectId> readerIds = new ArrayList<>();
        try (MongoCursor<Document> cursor = newspapersCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document newspaper = cursor.next();
                List<Document> articles = newspaper.getList("articles", Document.class);

                if (articles != null) {
                    for (Document article : articles) {
                        String articleDescription = article.getString("description");

                        if (description.equals(articleDescription)) {
                            List<Document> readarticles = article.getList("readarticle", Document.class);

                            if (readarticles != null) {
                                for (Document readarticle : readarticles) {
                                    ObjectId readerId = readarticle.getObjectId("_idReader");
                                    if (readerId != null && !readerIds.contains(readerId)) {
                                        readerIds.add(readerId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!readerIds.isEmpty()) {
            for (ObjectId readerId : readerIds) {
                Document query = new Document("_id", readerId);
                try (MongoCursor<Document> cursor = collection.find(query).iterator()) {
                    if (cursor.hasNext()) {
                        Document doc = cursor.next();
                        ReaderEntity reader = ReaderEntityMapper.documentToEntity(doc);
                        readers.add(reader);
                    }
                }
            }
        }

        return readers;
    }

    @Override
    public int save(ReaderEntity reader, CredentialEntity credential, boolean confirmation) {
        ObjectId readerId = null;

        try {
            // 1. Guardar el reader en MongoDB primero
            Document readerDoc = ReaderEntityMapper.entityToDocument(reader);
            collection.insertOne(readerDoc);

            // 2. Obtener el ObjectId generado
            readerId = readerDoc.getObjectId("_id");
            System.out.println("✅ Reader guardado correctamente en MongoDB con ID: " + readerId);

            // 3. Si hay confirmación y credenciales, guardar en MySQL
            if (confirmation && credential != null &&
                credential.getUsername() != null && !credential.getUsername().isEmpty() &&
                credential.getPassword() != null && !credential.getPassword().isEmpty()) {

                try (Connection con = dbConnection.getConnection()) {
                    con.setAutoCommit(false);

                    try (PreparedStatement psReader = con.prepareStatement(Query.InsertReader);
                         PreparedStatement psCredential = con.prepareStatement(Query.InsertCredential)) {

                        // Convertir ObjectId a int para MySQL (usando hashCode)
                        int idReaderForMySQL = readerId.hashCode();

                        // Primero insertar el reader en la tabla Reader
                        psReader.setInt(1, idReaderForMySQL);
                        psReader.setString(2, reader.getName());
                        psReader.setString(3, reader.getDob());
                        psReader.executeUpdate();

                        // Luego insertar las credenciales
                        psCredential.setString(1, credential.getUsername());
                        psCredential.setString(2, credential.getPassword());
                        psCredential.setInt(3, idReaderForMySQL);
                        psCredential.executeUpdate();

                        con.commit();
                        System.out.println("✅ Reader y credenciales guardados correctamente en MySQL con id_reader: " + idReaderForMySQL);

                    } catch (SQLException e) {
                        con.rollback();
                        System.err.println("⚠ Error al guardar en MySQL: " + e.getMessage());
                        System.out.println("ℹ El reader se guardó correctamente en MongoDB pero no se guardó en MySQL");
                    } finally {
                        con.setAutoCommit(true);
                    }

                } catch (Exception mysqlError) {
                    System.out.println("⚠ MySQL no disponible. Reader guardado solo en MongoDB");
                }
            } else {
                System.out.println("ℹ Reader creado sin credenciales en MySQL");
            }

            return 1;

        } catch (Exception e) {
            System.err.println("❌ Error al guardar reader: " + e.getMessage());
            e.printStackTrace();

            // Si falló MongoDB, intentar eliminar el reader si se llegó a crear
            if (readerId != null) {
                try {
                    collection.deleteOne(new Document("_id", readerId));
                    System.out.println("⚠ Rollback: Reader eliminado de MongoDB");
                } catch (Exception rollbackError) {
                    System.err.println("❌ Error en rollback: " + rollbackError.getMessage());
                }
            }

            return 0;
        }
    }

    @Override
    public int delete(ReaderEntity reader, boolean confirmation) {
        if (!confirmation) {
            System.out.println("⚠ Operación de eliminación cancelada por el usuario");
            return 0;
        }

        if (reader == null || reader.getId() == null) {
            System.err.println("❌ Reader inválido para eliminar");
            return 0;
        }

        try {
            // 1. Eliminar de MongoDB primero
            Document query = new Document("_id", reader.getId());
            long deletedCount = collection.deleteOne(query).getDeletedCount();

            if (deletedCount == 0) {
                System.err.println("❌ Reader no encontrado en MongoDB");
                return 0;
            }

            System.out.println("✅ Reader eliminado correctamente de MongoDB con ID: " + reader.getId());

            // 2. Intentar eliminar de MySQL si existe
            try (Connection con = dbConnection.getConnection()) {
                con.setAutoCommit(false);

                try (PreparedStatement psCredential = con.prepareStatement(Query.DeleteCredential);
                     PreparedStatement psReader = con.prepareStatement(Query.DeleteReader)) {

                    // Convertir ObjectId a int para MySQL (usando hashCode)
                    int idReaderForMySQL = reader.getId().hashCode();

                    // Primero eliminar las credenciales (por la FK)
                    psCredential.setInt(1, idReaderForMySQL);
                    int credentialsDeleted = psCredential.executeUpdate();

                    // Luego eliminar el reader
                    psReader.setInt(1, idReaderForMySQL);
                    int readerDeleted = psReader.executeUpdate();

                    con.commit();

                    if (credentialsDeleted > 0 || readerDeleted > 0) {
                        System.out.println("✅ Reader y credenciales eliminados de MySQL");
                    } else {
                        System.out.println("ℹ Reader no existía en MySQL");
                    }

                } catch (SQLException e) {
                    con.rollback();
                    System.err.println("⚠ Error al eliminar de MySQL: " + e.getMessage());
                    System.out.println("ℹ El reader se eliminó de MongoDB pero no de MySQL");
                } finally {
                    con.setAutoCommit(true);
                }

            } catch (Exception mysqlError) {
                System.out.println("⚠ MySQL no disponible. Reader eliminado solo de MongoDB");
            }

            return 1;

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar reader: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}

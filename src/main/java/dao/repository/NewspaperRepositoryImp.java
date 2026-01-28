package dao.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import dao.NewspaperRepository;
import dao.mapper.NewspaperEntityMapper;
import dao.model.NewspaperEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NewspaperRepositoryImp implements NewspaperRepository {
    private MongoCollection<Document> collection;

    // Constructor sin parámetros para CDI
    public NewspaperRepositoryImp() {
    }

    @Inject
    public NewspaperRepositoryImp(MongoCollection<Document> collection) {
        this.collection = collection;
    }


    @Override
    public List<NewspaperEntity> getAll() {
        List<NewspaperEntity> newspapers = new ArrayList<>();

        System.out.println("🔍 DEBUG: Nombre de colección: " + collection.getNamespace().getCollectionName());
        System.out.println("🔍 DEBUG: Nombre de BD: " + collection.getNamespace().getDatabaseName());

        long count = collection.countDocuments();
        System.out.println("🔍 DEBUG: Total documentos en colección: " + count);

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                System.out.println("🔍 DEBUG: Documento encontrado: " + doc.toJson());
                NewspaperEntity newspaper = NewspaperEntityMapper.documentToEntity(doc);
                newspapers.add(newspaper);
            }
        }

        System.out.println("🔍 DEBUG: Total newspapers parseados: " + newspapers.size());
        return newspapers;
    }
}

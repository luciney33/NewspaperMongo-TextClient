package dao;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import dao.model.ReaderEntity;
import util.MongoDBConnection;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ReaderDAO {
//    private final MongoCollection<Document> collection;
//    private final Gson gson;
//
//    public ReaderDAO() {
//        this.collection = MongoDBConnection.getCollection("readers");
//        this.gson = new Gson();
//    }
//
//    public List<ReaderEntity> findAll() {
//        List<ReaderEntity> readers = new ArrayList<>();
//        collection.find().into(new ArrayList<>()).forEach(doc ->
//            readers.add(gson.fromJson(doc.toJson(), ReaderEntity.class)));
//        return readers;
//    }
//
//    public ReaderEntity findById(int id) {
//        Document doc = collection.find(eq("id_reader", id)).first();
//        return doc != null ? gson.fromJson(doc.toJson(), ReaderEntity.class) : null;
//    }
//
//    public ReaderEntity findByName(String name) {
//        Document doc = collection.find(eq("name", name)).first();
//        return doc != null ? gson.fromJson(doc.toJson(), ReaderEntity.class) : null;
//    }
//
//    public void addReader(ReaderEntity reader) {
//        Document doc = Document.parse(gson.toJson(reader));
//        collection.insertOne(doc);
//        reader.setId(doc.getObjectId("_id"));
//    }
//
//    public void deleteReader(int id) {
//        collection.deleteOne(eq("id_reader", id));
//    }
}


package dao;

import dao.model.NewspaperEntity;

import java.util.List;

public interface NewspaperRepository {
    List<NewspaperEntity> getAll();

}

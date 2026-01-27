package dao;

import dao.model.TypeEntity;

import java.util.List;

public interface TypeRepository {
    List<TypeEntity> getAllTypes();
}

package dao.repository;

import dao.CredentialRepository;
import dao.mapper.MapRStoCredentialEntity;
import domain.error.AppError;
import domain.error.DataBaseError;
import jakarta.inject.Inject;
import dao.model.CredentialEntity;
import util.DBconnection;
import util.Query;

import java.sql.*;

public class CredentialRepositoryImp implements CredentialRepository {
    private final DBconnection db;
    private final MapRStoCredentialEntity mapper;


    @Inject
    public CredentialRepositoryImp(DBconnection db, MapRStoCredentialEntity mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public CredentialEntity get(String username) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(Query.SelectGetCrede)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CredentialEntity(rs.getString("username"), rs.getString("password"), rs.getInt("id_reader"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataBaseError(e.getMessage());
        }catch (Exception e) {
            throw new AppError(e.getMessage());
        }
        return null;
    }
}


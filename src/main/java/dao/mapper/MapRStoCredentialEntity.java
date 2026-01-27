package dao.mapper;


import dao.model.CredentialEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapRStoCredentialEntity {
    public List<CredentialEntity> mapRS(ResultSet rs) throws SQLException {
        List<CredentialEntity> credential = new ArrayList<>();
        try {
            while (rs.next()) {
                credential.add(new CredentialEntity(rs.getString("username"), rs.getString("password"), rs.getInt("id_reader")));
            }
            return credential;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

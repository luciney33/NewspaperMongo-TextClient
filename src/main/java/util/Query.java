package util;

public class Query {
    public static final String SelectGetCrede = "select username, password, id_reader from Credentials where username = ?";
    public static final String InsertCredential = "insert into Credentials (username, password, id_reader) values (?, ?, ?)";
    public static final String InsertReader = "insert into Reader (id_reader, name, birth_date) values (?, ?, ?)";
    public static final String DeleteCredential = "delete from Credentials where id_reader = ?";
    public static final String DeleteReader = "delete from Reader where id_reader = ?";
}

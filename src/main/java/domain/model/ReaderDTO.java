package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;


@Data
@AllArgsConstructor
public class ReaderDTO {
    private ObjectId id;
    private String name;
    private String dob;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----- Readers -----\n")
                .append(" ID   : ").append(id).append("\n")
                .append(" Name : ").append(name).append("\n")
                .append(" Birth Date : ").append(dob).append("\n")
                .append("----------------\n");
        return builder.toString();
    }
}

package domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeDTO {
    private String name;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----- Type -----\n")
                .append(" Name : ").append(name).append("\n")
                .append("----------------\n");
        return builder.toString();
    }
}


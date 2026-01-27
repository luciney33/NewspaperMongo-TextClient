package dao.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReaderEntity {
    private ObjectId id;
    private String name;
    private String dob;

    @Singular
    private List<SubscriptionEntity> subscriptions = new ArrayList<>();

}

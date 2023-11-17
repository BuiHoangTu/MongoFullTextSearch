package org.example.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "keywords_count")
public class KeywordCount {
    @Id
    private String id;
    private String word;
    @Indexed(direction = IndexDirection.DESCENDING)
    private long count;
}

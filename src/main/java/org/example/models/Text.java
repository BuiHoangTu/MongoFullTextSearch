package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "with_keywords")
@AllArgsConstructor
public class Text {
    @Id
    private String id;
    @TextIndexed()
    private String text;
}

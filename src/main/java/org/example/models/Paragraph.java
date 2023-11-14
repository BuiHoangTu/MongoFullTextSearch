package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@ToString
public class Paragraph {
    @Id
    private String id;
    @TextIndexed(weight = 2)
    private List<String> keywords;
    @TextIndexed()
    private String line;
}

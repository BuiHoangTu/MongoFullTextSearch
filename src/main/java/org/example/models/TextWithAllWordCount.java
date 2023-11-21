package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "text_with_keyword_count")
@AllArgsConstructor
public class TextWithAllWordCount {
    @Id
    private String id;
    @TextIndexed()
    private String text;
    private List<WordCount> wordCounts;
}

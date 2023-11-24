package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "text_with_keyword_count")
@AllArgsConstructor
public class TextWithAllWordCount {
    @Id
    private String id;
    @TextIndexed()
    private String text;
    private List<WordCount> wordCounts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextWithAllWordCount that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.utils.counter.Countable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "keywords_count")
@AllArgsConstructor
public class WordCount implements Countable {
    @Id
    private String id;
    private String word;
    @Indexed(direction = IndexDirection.DESCENDING)
    private long count;

    @Override
    public void stack(Object countable) {
        if (this.sameType(countable)) {
            var keywordCount = (WordCount) countable;
            this.count += keywordCount.getCount();
        }
    }

    @Override
    public boolean sameType(Object countable) {
        if (countable instanceof WordCount wordCount) {
            return wordCount.word.equals(this.word);
        }
        return false;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return this.word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return sameType(obj);
    }

}

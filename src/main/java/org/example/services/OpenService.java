package org.example.services;

import org.example.databases.mongo.reposistories.ParagraphRepo;
import org.example.models.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenService {
    private final ParagraphRepo repo;

    @Autowired
    public OpenService(ParagraphRepo repo) {
        this.repo = repo;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return repo.searchFullText(keyText);
    }
}

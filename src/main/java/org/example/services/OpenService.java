package org.example.services;

import org.example.databases.mongo.template.MongoUtils;
import org.example.models.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenService {
    private final MongoUtils data;

    @Autowired
    public OpenService(MongoUtils data) {
        this.data = data;
    }

    public List<Paragraph> searchKeyText(String keyText) {
        return data.searchKeyText(keyText);
    }
}

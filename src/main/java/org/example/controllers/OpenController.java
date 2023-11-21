package org.example.controllers;

import org.example.models.KeywordCount;
import org.example.models.Paragraph;
import org.example.models.Text;
import org.example.services.OpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(path = "api/activate")
public class OpenController {
    private static final Logger OPEN_CONTROLLER_LOG = LoggerFactory.getLogger(OpenController.class);


    private final OpenService service;

    @Autowired
    public OpenController(OpenService service) {
        this.service = service;
    }

    @GetMapping(path = "search")
    public ResponseEntity<List<Paragraph>> searchKeyText(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchKeyText(keyText);
        OPEN_CONTROLLER_LOG.info("Got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "common")
    public ResponseEntity<List<KeywordCount>> searchKeyText() {
        return ResponseEntity.ok(service.searchCommonText());
    }

    @GetMapping(path = "search")
    public ResponseEntity<List<Text>> searchTextWithKeyword(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchTextWithKeyword(keyText);
        OPEN_CONTROLLER_LOG.info("Got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }
}

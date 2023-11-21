package org.example.controllers;

import org.example.models.KeywordCount;
import org.example.models.Paragraph;
import org.example.models.Text;
import org.example.models.TextWithAllWordCount;
import org.example.services.OpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        OPEN_CONTROLLER_LOG.info("search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "common")
    public ResponseEntity<List<KeywordCount>> searchKeyText() {
        return ResponseEntity.ok(service.searchCommonText());
    }

    @GetMapping(path = "reduced-search")
    public ResponseEntity<List<Text>> searchTextWithKeyword(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchTextWithKeyword(keyText);
        OPEN_CONTROLLER_LOG.info("reduced-search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "combine-reduced-search")
    public ResponseEntity<List<TextWithAllWordCount>> searchTextWithWordCount(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchTextWithAllWordCount(keyText);
        OPEN_CONTROLLER_LOG.info("combine-reduced-search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "save-text")
    public ResponseEntity<?> saveText(@RequestBody List<String> texts) {
        service.saveText(texts);

        return ResponseEntity.accepted().build();
    }
}

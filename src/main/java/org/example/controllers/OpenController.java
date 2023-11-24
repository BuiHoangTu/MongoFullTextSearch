package org.example.controllers;

import org.example.models.WordCount;
import org.example.models.Paragraph;
import org.example.models.Text;
import org.example.models.TextWithAllWordCount;
import org.example.services.OpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "api/activate")
@SuppressWarnings("unused")
public class OpenController {
    private static final Logger OPEN_CONTROLLER_LOG = LoggerFactory.getLogger(OpenController.class);


    private final OpenService service;

    @Autowired
    @SuppressWarnings("unused")
    public OpenController(OpenService service) {
        this.service = service;
    }

    @GetMapping(path = "search")
    @SuppressWarnings("unused")
    public ResponseEntity<List<Paragraph>> searchKeyText(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchKeyText(keyText);
        OPEN_CONTROLLER_LOG.info("search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "common")
    @SuppressWarnings("unused")
    public ResponseEntity<List<WordCount>> searchKeyText() {
        return ResponseEntity.ok(service.searchCommonText());
    }

    @GetMapping(path = "reduced-search")
    @SuppressWarnings("unused")
    public ResponseEntity<List<Text>> searchTextWithKeyword(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchTextWithKeyword(keyText);
        OPEN_CONTROLLER_LOG.info("reduced-search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @GetMapping(path = "combine-reduced-search")
    @SuppressWarnings("unused")
    public ResponseEntity<Collection<TextWithAllWordCount>> searchTextWithWordCount(@RequestParam(value = "keyText") String keyText) {
        var res = service.searchTextWithAllWordCount(keyText);
        OPEN_CONTROLLER_LOG.info("combine-reduced-search got keyText:" + keyText);
        OPEN_CONTROLLER_LOG.info(res.toString());
        return ResponseEntity.ok(res);
    }

    @PostMapping(path = "save-text")
    @SuppressWarnings("unused")
    public ResponseEntity<?> saveText(@RequestBody List<String> texts) {
        service.saveText(texts);

        return ResponseEntity.accepted().build();
    }
}

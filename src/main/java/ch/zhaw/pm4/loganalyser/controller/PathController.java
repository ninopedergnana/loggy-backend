package ch.zhaw.pm4.loganalyser.controller;

import ch.zhaw.pm4.loganalyser.model.dto.FileTreeDTO;
import ch.zhaw.pm4.loganalyser.service.PathService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/path")
@AllArgsConstructor
public class PathController {

    private final PathService service;

    @GetMapping
    public ResponseEntity<List<FileTreeDTO>> getFoldersOfPath(@RequestParam(required = false) String folder) {
        return ResponseEntity.ok(folder == null ? service.getRootFolder() : service.getContentOfFolder(folder));
    }
}

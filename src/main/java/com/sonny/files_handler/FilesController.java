package com.sonny.files_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "files")
@RequiredArgsConstructor
public class FilesController {

    private final FilesRepository filesRepository;

    @PostMapping
    public void create(@RequestBody FileParams fileParams) {
        fileParams.setTemp(String.valueOf(System.currentTimeMillis()));
        this.filesRepository.save(fileParams);
    }

    @GetMapping
    public @ResponseBody List<FileParams> search() {
        return this.filesRepository.findAll();
    }
}

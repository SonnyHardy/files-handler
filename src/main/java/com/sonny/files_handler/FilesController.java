package com.sonny.files_handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(path = "files")
@RequiredArgsConstructor
public class FilesController {

    private final FilesRepository filesRepository;
    @Value("${application.files.base-path}")
    private String basePath;

    @PostMapping
    public void create(@RequestBody FileParams fileParams) throws IOException {
        final String temp = String.format("%s.%s",
                System.currentTimeMillis(),
                FilenameUtils.getExtension(fileParams.getName()));

        fileParams.setTemp(temp);
        this.filesRepository.save(fileParams);
        this.writeOnDisk(fileParams);
    }

    public void writeOnDisk(FileParams params) throws IOException {
        final String fullPath = String.format("%s/%s", this.basePath, params.getTemp());
        final Path folder = Paths.get(fullPath).getParent();
        Files.createDirectories(folder);

        String fileAsString = String.valueOf(params.getContent());
        if (fileAsString.contains(",")) {
            fileAsString = fileAsString.split(",")[1];
        }

        final byte[] decodedFile = Base64.getDecoder().decode(fileAsString);
        final File fullPathAsFile = new File(fullPath);
        if (Files.exists(Paths.get(fullPath))) {
            FileUtils.delete(fullPathAsFile);
        }

        FileUtils.writeByteArrayToFile(fullPathAsFile, decodedFile);
    }

    @GetMapping
    public @ResponseBody List<FileParams> search() {
        return this.filesRepository.findAll();
    }
}

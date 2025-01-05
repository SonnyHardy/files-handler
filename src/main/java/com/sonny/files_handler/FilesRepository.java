package com.sonny.files_handler;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FileParams, Integer> {
}

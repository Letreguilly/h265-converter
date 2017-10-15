package fr.letreguilly.business;

import fr.letreguilly.persistence.repositories.VideoFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoFolderService {
    @Autowired
    private VideoFolderRepository repository;

    /*private void test (String test) {
        repository.findOne(556L);
    }*/
}

package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Folder;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {
    Optional<Folder> findById (Integer id);
    Optional<Folder> findByOwnerAndFolderName (User user, String folderName);
}

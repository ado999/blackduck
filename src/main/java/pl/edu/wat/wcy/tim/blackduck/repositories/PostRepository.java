package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Post;
import pl.edu.wat.wcy.tim.blackduck.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer id);

    Post findByAuthor(User user);

    List<Post> findAllByAuthorInOrderByCreationDateDesc(Set<User> author);

    List<Post> findAllByAuthorOrderByCreationDateDesc(User user);
}

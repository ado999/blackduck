package pl.edu.wat.wcy.tim.blackduck.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.tim.blackduck.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Comment findCommentById (Integer id);
}

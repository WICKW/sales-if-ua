package sales.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sales.comments.domain.Comment;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {

    public List<Comment> findByGoodIdOrderByCreationDateDesc(int goodId);

    public void removeById(Long id);

    public Comment findById(Long commentId);

}

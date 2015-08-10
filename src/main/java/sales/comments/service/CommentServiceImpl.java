package sales.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sales.comments.domain.Comment;
import sales.comments.repository.ICommentRepository;
import sales.goods.service.GoodsService;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    @Qualifier("goodsService")
    private GoodsService goodsService;

    @Override
    public Comment addComment(Comment comment) {
        goodsService.rateGoodUpdate(comment.getGoodId(), comment.getRating());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByGoodId(int goodId) {
        return commentRepository.findByGoodIdOrderByCreationDateDesc(goodId);
    }

    @Override
    public void removeCommentById(Long commentId) {
        goodsService.rateGoodUpdateRemove(commentId);
        commentRepository.removeById(commentId);
    }
}

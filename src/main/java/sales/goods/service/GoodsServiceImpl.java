package sales.goods.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sales.comments.domain.Comment;
import sales.comments.repository.ICommentRepository;
import sales.goods.domain.Good;
import sales.goods.repository.GoodsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private ICommentRepository commentRepository;

    public Good get(long id) {
        return repository.findById(id);
    }

    public List<Good> getAll() {
        return repository.findAll();
    }

    public Good save(Good good) {
        repository.save(good);
        return good;
    }

    public void delete(long id) {
        repository.removeById(id);
    }

    public List<Good> page(int page, int amount, Map<String, String> sort) {
        PageRequest pageRequest;
        if (sort == null || sort.isEmpty()) {
            pageRequest = new PageRequest(page, amount);
        } else {
            List<Sort.Order> orders = new ArrayList();
            Set<String> sortParams = sort.keySet();
            for (String sortParam : sortParams) {
                orders.add(new Sort.Order(Sort.Direction.fromString(sort.get(sortParam)), sortParam));
            }
            pageRequest = new PageRequest(
                    page,
                    amount,
                    new Sort(orders));
        }
        return Lists.newArrayList(repository.findAll(pageRequest).getContent());
    }

    public List<Good> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    @Override

    public List<Good> getNewest(int size) {
        PageRequest pageRequest = new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "date"));
        return Lists.newArrayList(repository.findAll(pageRequest).getContent());
    }

    public Good rateGoodUpdate(int goodId, double rate) {
        Good good = repository.findById(goodId);
        long oldRatingCount = good.getRatingCount();
        long newRatingCount = oldRatingCount + 1;
        double oldRating = good.getRating();
        double newRating = (oldRating * oldRatingCount + rate) / newRatingCount ;
        good.setRating(newRating);
        good.setRatingCount(newRatingCount);
        repository.save(good);
        return good;
    }

    @Override
    public Good rateGoodUpdateRemove(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        Good good = repository.findById(comment.getGoodId());
        long oldRatingCount = good.getRatingCount();
        long newRatingCount = oldRatingCount - 1;
        double oldRating = good.getRating();
        double newRating = (oldRating * oldRatingCount - comment.getRating()) / newRatingCount;
        good.setRatingCount(newRatingCount);
        good.setRating(newRating);
        repository.save(good);
        return good;
    }
}

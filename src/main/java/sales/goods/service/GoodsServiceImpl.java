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

    @Override
    public Good rateGoodUpdate(int goodId, double rate) {
        return repository.save(updateGoodRatingAdd(goodId, rate));
    }

    @Override
    public Good rateGoodUpdateRemove(Long commentId) {
        return repository.save(updateGoodRatingRemove(commentId));
    }

    private Good updateGoodRatingAdd(int goodId, double rate) {
        Good good = repository.findById(goodId);
        good.setRating((good.getRating() * good.getRatingCount() + rate) / (good.getRatingCount() + 1));
        good.setRatingCount(good.getRatingCount() + 1);
        return good;
    }

    private Good updateGoodRatingRemove(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        Good good = repository.findById(comment.getGoodId());
        good.setRating((good.getRating() * good.getRatingCount() - comment.getRating()) / (good.getRatingCount() - 1));
        good.setRatingCount(good.getRatingCount() - 1);
        return good;
    }

}

package stepanenko.practice4.service;

import java.util.List;
import stepanenko.practice4.model.CriteriaManager;
import stepanenko.practice4.model.Product;

public interface ProductService {
    Integer showAmount(Long id);

    void setAmount(Long id, Integer amount);

    Integer showPrice(Long id);

    void setPrice(Long id, Integer price);

    Product get(Long id);

    List<Product> getAll();

    Product update(Product product);

    void delete(Long id);

    List<Product> getProductsByCriteria(CriteriaManager criteria);
}

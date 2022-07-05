package stepanenko.practice4.dao;

import java.util.List;
import java.util.Optional;
import stepanenko.practice4.model.Product;

public interface ProductDao {
    Integer showAmount(Long id);

    void setAmount(Long id, Integer amount);

    Integer showPrice(Long id);

    void setPrice(Long id, Integer price);

    Optional<Product> get(Long id);

    List<Product> getAll();

    Product update(Product product);

    void delete(Long id);

    List<Product> getProductsByAmountDiapason(Integer value1, Integer value2);

    List<Product> getProductsByPriceDiapason(Integer value1, Integer value2);

    List<Product> getProductsWithLessAmount(Integer value1);

    List<Product> getProductsWithMoreAmount(Integer value1);

    List<Product> getProductsWithLessPrice(Integer value1);

    List<Product> getProductsWithMorePrice(Integer value1);
}

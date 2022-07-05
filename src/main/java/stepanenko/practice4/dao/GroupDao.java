package stepanenko.practice4.dao;

import java.util.List;
import java.util.Optional;
import stepanenko.practice4.model.Group;
import stepanenko.practice4.model.Product;

public interface GroupDao {
    Group create(Group group);

    Optional<Group> get(Long id);

    List<Group> getAll();

    Group update(Group group);

    void delete(Long id);

    void addProduct(Long groupId, Product product);

    List<Product> getProductsByGroup(Long groupId);
}

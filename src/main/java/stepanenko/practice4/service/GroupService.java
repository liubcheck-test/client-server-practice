package stepanenko.practice4.service;

import java.util.List;
import stepanenko.practice4.model.CriteriaManager;
import stepanenko.practice4.model.Group;
import stepanenko.practice4.model.Product;

public interface GroupService {
    Group create(Group group);

    Group get(Long id);

    List<Group> getAll();

    Group update(Group group);

    void delete(Long id);

    void addProduct(Group group, Product product);

    List<Product> getProductsByGroup(Long groupId);
}

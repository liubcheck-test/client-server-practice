package stepanenko.practice4.service;

import java.util.List;
import java.util.NoSuchElementException;
import stepanenko.practice4.dao.GroupDao;
import stepanenko.practice4.dao.GroupDaoImpl;
import stepanenko.practice4.model.CriteriaManager;
import stepanenko.practice4.model.Group;
import stepanenko.practice4.model.Product;

public class GroupServiceImpl implements GroupService {
    private final GroupDao groupDao = new GroupDaoImpl();

    @Override
    public Group create(Group group) {
        return groupDao.create(group);
    }

    @Override
    public Group get(Long id) {
        return groupDao.get(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Can't get the group with id " + id));
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public Group update(Group group) {
        return groupDao.update(group);
    }

    @Override
    public void delete(Long id) {
        groupDao.delete(id);
    }

    @Override
    public void addProduct(Group group, Product product) {
        groupDao.addProduct(group.getId(), product);
        groupDao.update(group);
    }

    @Override
    public List<Product> getProductsByGroup(Long groupId) {
        return groupDao.getProductsByGroup(groupId);
    }
}

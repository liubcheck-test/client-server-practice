package stepanenko.practice4.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import stepanenko.practice4.exception.DataProcessingException;
import stepanenko.practice4.model.Group;
import stepanenko.practice4.model.Product;
import stepanenko.practice4.util.DBUtil;

public class GroupDaoImpl implements GroupDao {
    @Override
    public Group create(Group group) {
        if (groupExists(group.getName())) {
            throw new DataProcessingException("There is already the group with same name");
        }
        String createGroupRequest = "INSERT INTO groups(name) VALUES(?)";
        String lastIdRequest = "SELECT last_insert_rowid() FROM groups";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement createGroupStatement =
                     connection.prepareStatement(createGroupRequest);
             Statement lastIdStatement = connection.createStatement()) {
            createGroupStatement.setString(1, group.getName());
            createGroupStatement.executeUpdate();
            ResultSet generatedKeys = lastIdStatement.executeQuery(lastIdRequest);
            if (generatedKeys.next()) {
                group.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create the group " + group, e);
        }
        return group;
    }

    @Override
    public Optional<Group> get(Long id) {
        String selectGroupRequest = "SELECT * FROM groups "
                + "WHERE id = ? AND is_deleted = FALSE";
        Group group = null;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement selectGroupStatement =
                     connection.prepareStatement(selectGroupRequest)) {
            selectGroupStatement.setLong(1, id);
            ResultSet resultSet = selectGroupStatement.executeQuery();
            if (resultSet.next()) {
                group = getGroup(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the group by id " + id, e);
        }
        if (group != null) {
            group.setProducts(getProductsForGroup(id));
        }
        return Optional.ofNullable(group);
    }

    @Override
    public List<Group> getAll() {
        String getAllGroupsRequest = "SELECT * FROM groups WHERE is_deleted = FALSE";
        List<Group> groups = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getAllGroupsStatement =
                     connection.prepareStatement(getAllGroupsRequest)) {
            ResultSet resultSet = getAllGroupsStatement.executeQuery();
            while (resultSet.next()) {
                groups.add(getGroup(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the list of groups", e);
        }
        groups.forEach(group ->
                group.setProducts(getProductsForGroup(group.getId())));
        return groups;
    }

    @Override
    public Group update(Group group) {
        String updateGroupRequest = "UPDATE groups SET name = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        String oldGroupName = group.getName();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement updateGroupStatement =
                     connection.prepareStatement(updateGroupRequest)) {
            updateGroupStatement.setString(1, group.getName());
            updateGroupStatement.setObject(2, group.getId());
            updateGroupStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update the group " + group, e);
        }
        if (groupExists(group.getName())) {
            renameGroup(group.getId(), oldGroupName);
        }
        group.setProducts(getProductsForGroup(group.getId()));
        return group;
    }

    @Override
    public void delete(Long id) {
        String deleteGroupRequest = "UPDATE groups SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement deleteGroupStatement =
                     connection.prepareStatement(deleteGroupRequest)) {
            deleteGroupStatement.setLong(1, id);
            deleteGroupStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete the group with id " + id, e);
        }
        deleteProducts(id);
    }

    @Override
    public void addProduct(Long groupId, Product product) {
        if (productExists(product.getName())) {
            throw new DataProcessingException("There is already the product "
                    + "with same name in this group");
        }
        String addProductRequest = "INSERT INTO products (name, amount, price, group_id) "
                + "VALUES (?, ?, ?, ?)";
        String lastIdRequest = "SELECT last_insert_rowid() FROM products";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement addProductStatement =
                     connection.prepareStatement(addProductRequest);
             Statement lastIdStatement = connection.createStatement()) {
            addProductStatement.setString(1, product.getName());
            addProductStatement.setObject(2, product.getAmount());
            addProductStatement.setObject(3, product.getPrice());
            addProductStatement.setObject(4, groupId);
            addProductStatement.executeUpdate();
            ResultSet generatedKeys = lastIdStatement.executeQuery(lastIdRequest);
            if (generatedKeys.next()) {
                product.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add the product " + product, e);
        }
    }

    @Override
    public List<Product> getProductsByGroup(Long groupId) {
        return getProductsForGroup(groupId);
    }

    private Group getGroup(ResultSet resultSet) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getLong(1));
        group.setName(resultSet.getString(2));
        return group;
    }

    private List<Product> getProductsForGroup(Long id) {
        String getProductsForGroupRequest = "SELECT * FROM products "
                + "WHERE group_id = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsForGroupStatement =
                     connection.prepareStatement(getProductsForGroupRequest)) {
            getProductsForGroupStatement.setLong(1, id);
            ResultSet resultSet = getProductsForGroupStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products for "
                    + "the group with id " + id, e);
        }
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong(1));
        product.setName(resultSet.getString(2));
        product.setAmount(resultSet.getInt(3));
        product.setPrice(resultSet.getInt(4));
        return product;
    }

    private boolean groupExists(String name) {
        String groupsWithSameNameRequest = "SELECT * FROM groups WHERE "
                + "name = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement groupsWithSameNameStatement =
                     connection.prepareStatement(groupsWithSameNameRequest)) {
            int groupsNumber = 0;
            groupsWithSameNameStatement.setString(1, name);
            ResultSet resultSet = groupsWithSameNameStatement.executeQuery();
            while (resultSet.next()) {
                groupsNumber++;
            }
            return groupsNumber > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the list of groups "
                    + "with same name", e);
        }
    }

    private void renameGroup(Long id, String name) {
        String renameGroupRequest = "UPDATE groups SET name = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement renameGroupStatement =
                     connection.prepareStatement(renameGroupRequest)) {
            renameGroupStatement.setString(1, name);
            renameGroupStatement.setLong(2, id);
            renameGroupStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't rename the group", e);
        }
    }

    private void deleteProducts(Long groupId) {
        String deleteProductRequest = "UPDATE products SET is_deleted = TRUE "
                + "WHERE group_id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement deleteProductStatement =
                     connection.prepareStatement(deleteProductRequest)) {
            deleteProductStatement.setLong(1, groupId);
            deleteProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete products of "
                    + "the group with id " + groupId, e);
        }
    }

    private boolean productExists(String name) {
        String productsWithSameNameRequest = "SELECT * FROM products WHERE "
                + "name = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement productsWithSameNameStatement =
                     connection.prepareStatement(productsWithSameNameRequest)) {
            int productsNumber = 0;
            productsWithSameNameStatement.setString(1, name);
            ResultSet resultSet = productsWithSameNameStatement.executeQuery();
            while (resultSet.next()) {
                productsNumber++;
            }
            return productsNumber > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the list of groups "
                    + "with same name", e);
        }
    }
}

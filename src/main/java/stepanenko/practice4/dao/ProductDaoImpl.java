package stepanenko.practice4.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import stepanenko.practice4.exception.DataProcessingException;
import stepanenko.practice4.model.Product;
import stepanenko.practice4.util.DBUtil;

public class ProductDaoImpl implements ProductDao {
    @Override
    public Integer showAmount(Long id) {
        String showProductAmountRequest = "SELECT amount FROM products "
                + "WHERE id = ? AND is_deleted = FALSE";
        int amount = 0;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement showProductAmountStatement =
                     connection.prepareStatement(showProductAmountRequest)) {
            showProductAmountStatement.setObject(1, id);
            ResultSet resultSet = showProductAmountStatement.executeQuery();
            if (resultSet.next()) {
                amount = resultSet.getInt("amount");
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find the product "
                    + "with id " + id, e);
        }
        return amount;
    }

    @Override
    public void setAmount(Long id, Integer amount) {
        String setProductAmountRequest = "UPDATE products SET amount = ? "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement showProductAmountStatement =
                     connection.prepareStatement(setProductAmountRequest)) {
            showProductAmountStatement.setObject(1, amount);
            showProductAmountStatement.setObject(2, id);
            showProductAmountStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find the product "
                    + "with id " + id, e);
        }
    }

    @Override
    public Integer showPrice(Long id) {
        String showProductPriceRequest = "SELECT price FROM products "
                + "WHERE id = ? AND is_deleted = FALSE";
        int price = 0;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement showProductPriceStatement =
                     connection.prepareStatement(showProductPriceRequest)) {
            showProductPriceStatement.setObject(1, id);
            ResultSet resultSet = showProductPriceStatement.executeQuery();
            if (resultSet.next()) {
                price = resultSet.getInt("price");
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find the product "
                    + "with id " + id, e);
        }
        return price;
    }

    @Override
    public void setPrice(Long id, Integer price) {
        String setProductPriceRequest = "UPDATE products SET price = ? "
                + "WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement showProductPriceStatement =
                     connection.prepareStatement(setProductPriceRequest)) {
            showProductPriceStatement.setObject(1, price);
            showProductPriceStatement.setObject(2, id);
            showProductPriceStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find the product "
                    + "with id " + id, e);
        }
    }

    @Override
    public Optional<Product> get(Long id) {
        String selectProductRequest = "SELECT * FROM products "
                + "WHERE id = ? AND is_deleted = FALSE";
        Product product = null;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement selectProductStatement =
                     connection.prepareStatement(selectProductRequest)) {
            selectProductStatement.setLong(1, id);
            ResultSet resultSet = selectProductStatement.executeQuery();
            if (resultSet.next()) {
                product = getProduct(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the product by id " + id, e);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        String getAllProductsRequest = "SELECT * FROM products WHERE is_deleted = FALSE";
        List<Product> products = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getAllProductsStatement =
                     connection.prepareStatement(getAllProductsRequest)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get the list of products", e);
        }
        return products;
    }

    @Override
    public Product update(Product product) {
        String updateProductRequest = "UPDATE products SET name = ?, "
                + "amount = ?, price = ? WHERE id = ? AND is_deleted = FALSE";
        String oldProductName = product.getName();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement updateProductStatement =
                     connection.prepareStatement(updateProductRequest)) {
            updateProductStatement.setString(1, product.getName());
            updateProductStatement.setObject(2, product.getAmount());
            updateProductStatement.setObject(3, product.getPrice());
            updateProductStatement.setObject(4, product.getId());
            updateProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update the product " + product, e);
        }
        if (productExists(product.getName())) {
            renameProduct(product.getId(), oldProductName);
        }
        return product;
    }

    @Override
    public void delete(Long id) {
        String deleteProductRequest = "UPDATE products SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement deleteProductStatement =
                     connection.prepareStatement(deleteProductRequest)) {
            deleteProductStatement.setLong(1, id);
            deleteProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete the group with id " + id, e);
        }
    }

    @Override
    public List<Product> getProductsByAmountDiapason(Integer value1, Integer value2) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND amount BETWEEN ? AND ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            getProductsStatement.setLong(2, value2);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
        }
    }

    @Override
    public List<Product> getProductsByPriceDiapason(Integer value1, Integer value2) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND price BETWEEN ? AND ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            getProductsStatement.setLong(2, value2);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
        }
    }

    @Override
    public List<Product> getProductsWithLessAmount(Integer value1) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND amount < ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
        }
    }

    @Override
    public List<Product> getProductsWithMoreAmount(Integer value1) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND amount > ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
        }
    }

    @Override
    public List<Product> getProductsWithLessPrice(Integer value1) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND price < ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
        }
    }

    @Override
    public List<Product> getProductsWithMorePrice(Integer value1) {
        String getProductsRequest = "SELECT * FROM products "
                + "WHERE is_deleted = FALSE AND price > ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement getProductsStatement =
                     connection.prepareStatement(getProductsRequest)) {
            getProductsStatement.setLong(1, value1);
            ResultSet resultSet = getProductsStatement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products with specific criteria", e);
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

    private void renameProduct(Long id, String name) {
        String renameProductRequest = "UPDATE products SET name = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement renameProductStatement =
                     connection.prepareStatement(renameProductRequest)) {
            renameProductStatement.setString(1, name);
            renameProductStatement.setLong(2, id);
            renameProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't rename the group", e);
        }
    }
}

package stepanenko.practice4.service;

import java.util.List;
import java.util.NoSuchElementException;
import stepanenko.practice4.dao.ProductDao;
import stepanenko.practice4.dao.ProductDaoImpl;
import stepanenko.practice4.exception.DataProcessingException;
import stepanenko.practice4.model.CriteriaManager;
import stepanenko.practice4.model.Product;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = new ProductDaoImpl();

    @Override
    public Integer showAmount(Long id) {
        return productDao.showAmount(id);
    }

    @Override
    public void setAmount(Long id, Integer amount) {
        productDao.setAmount(id, amount);
    }

    @Override
    public Integer showPrice(Long id) {
        return productDao.showPrice(id);
    }

    @Override
    public void setPrice(Long id, Integer price) {
        productDao.setPrice(id, price);
    }

    @Override
    public Product get(Long id) {
        return productDao.get(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Can't get the product with id " + id));
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product update(Product product) {
        return productDao.update(product);
    }

    @Override
    public void delete(Long id) {
        productDao.delete(id);
    }

    @Override
    public List<Product> getProductsByCriteria(CriteriaManager criteria) {
        if (criteria.getValue2() != null && criteria.getValue1() > criteria.getValue2()) {
            throw new DataProcessingException("You must enter the 2nd value higher than the 1st one");
        }
        List<Product> products = null;
        switch (criteria.getCriteria()) {
            case AMOUNT_DIAPASON ->
                    products = productDao.getProductsByAmountDiapason(criteria.getValue1(), criteria.getValue2());
            case PRICE_DIAPASON ->
                    products = productDao.getProductsByPriceDiapason(criteria.getValue1(), criteria.getValue2());
            case LESS_THAN_PARTICULAR_AMOUNT ->
                    products = productDao.getProductsWithLessAmount(criteria.getValue1());
            case MORE_THAN_PARTICULAR_AMOUNT ->
                    products = productDao.getProductsWithMoreAmount(criteria.getValue1());
            case LESS_THAN_PARTICULAR_PRICE ->
                    products = productDao.getProductsWithLessPrice(criteria.getValue1());
            case MORE_THAN_PARTICULAR_PRICE ->
                    products = productDao.getProductsWithMorePrice(criteria.getValue1());
        }
        return products;
    }
}

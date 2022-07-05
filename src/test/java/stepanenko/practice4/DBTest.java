package stepanenko.practice4;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import stepanenko.practice4.exception.DataProcessingException;
import stepanenko.practice4.model.Criteria;
import stepanenko.practice4.model.CriteriaManager;
import stepanenko.practice4.model.Group;
import stepanenko.practice4.model.Product;
import stepanenko.practice4.service.GroupService;
import stepanenko.practice4.service.GroupServiceImpl;
import stepanenko.practice4.service.ProductService;
import stepanenko.practice4.service.ProductServiceImpl;
import stepanenko.practice4.util.DBUtil;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class DBTest {
    private static GroupService groupService;
    private static ProductService productService;
    private static Group snacks;
    private static Group drinks;
    private static Product lays;
    private static Product cheetos;
    private static Product kvas;

    @BeforeAll
    public static void init() {
        File storeFile = new File("store.db");
        if (storeFile.exists()) {
            storeFile.delete();
        }
        DBUtil.initDatabase();
        groupService = new GroupServiceImpl();
        productService = new ProductServiceImpl();
        snacks = new Group("snacks");
        drinks = new Group("drinks");
        groupService.create(snacks);
        groupService.create(drinks);
        lays = new Product("lays", 30, 35);
        cheetos = new Product("cheetos", 20, 20);
        groupService.addProduct(snacks, lays);
        groupService.addProduct(snacks, cheetos);
        kvas = new Product("kvas", 40, 20);
        groupService.addProduct(drinks, kvas);
    }

    @Test
    public void add_group_Ok() {
        groupService.create(new Group("fruits"));
        int expectedGroupNumber = 3;
        Assertions.assertEquals(expectedGroupNumber, groupService.getAll().size());
    }

    @Test
    public void add_groupWithSameName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                groupService.create(snacks));
    }

    @Test
    public void add_product_Ok() {
        groupService.addProduct(groupService.get(drinks.getId()),
                new Product("cola", 50, 25));
        int expectedProductNumber = 4;
        Assertions.assertEquals(expectedProductNumber, productService.getAll().size());
    }

    @Test
    public void add_productWithSameNameInGroup_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                groupService.addProduct(groupService.get(drinks.getId()), kvas));
    }

    @Test
    public void add_productWithSameNameInOtherGroup_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                groupService.addProduct(groupService.get(snacks.getId()), kvas));
    }

    @Test
    public void get_allGroups_Ok() {
        List<Group> groups = groupService.getAll();
        int expectedGroupNumber = 3;
        Assertions.assertEquals(expectedGroupNumber, groups.size());
    }

    @Test
    public void get_allProducts_Ok() {
        List<Product> products = productService.getAll();
        int expectedProductNumber = 4;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_group_Ok() {
        Group group = groupService.get(snacks.getId());
        Assertions.assertEquals(snacks, group);
    }

    @Test
    public void get_invalidGroup_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                groupService.get(10L));
    }

    @Test
    public void get_product_Ok() {
        Product product = productService.get(lays.getId());
        Assertions.assertEquals(lays, product);
    }

    @Test
    public void get_productByCriteria_amountDiapason_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.AMOUNT_DIAPASON, 30, 40);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 2;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByCriteria_priceDiapason_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.PRICE_DIAPASON, 20, 30);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 3;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByCriteria_lessThanParticularAmount_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.LESS_THAN_PARTICULAR_AMOUNT, 25);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 1;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByCriteria_moreThanParticularAmount_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.MORE_THAN_PARTICULAR_AMOUNT, 25);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 3;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByCriteria_lessThanParticularPrice_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.LESS_THAN_PARTICULAR_PRICE, 30);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 3;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByCriteria_moreThanParticularPrice_Ok() {
        CriteriaManager criteria =
                new CriteriaManager(Criteria.MORE_THAN_PARTICULAR_PRICE, 30);
        List<Product> products = productService.getProductsByCriteria(criteria);
        int expectedProductNumber = 1;
        Assertions.assertEquals(expectedProductNumber, products.size());
    }

    @Test
    public void get_productByGroup_Ok() {
        List<Product> productsByGroup =
                groupService.getProductsByGroup(snacks.getId());
        int expectedListSize = 2;
        Assertions.assertEquals(expectedListSize, productsByGroup.size());
    }

    @Test
    public void get_productInvalid_NotOk() {
        Product product = new Product("sprite", 10, 20);
        product.setId(11L);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                productService.get(product.getId()));
    }

    @Test
    public void remove_group_Ok() {
        groupService.delete(drinks.getId());
        int expectedGroupNumber = 2;
        Assertions.assertEquals(expectedGroupNumber, groupService.getAll().size());
    }

    @Test
    public void remove_product_Ok() {
        productService.delete(cheetos.getId());
        int expectedProductNumber = 1;
        Assertions.assertEquals(expectedProductNumber, productService.getAll().size());
    }

    @Test
    public void setAndShow_productAmount_Ok() {
        int amount = 60;
        productService.setAmount(lays.getId(), amount);
        Assertions.assertEquals(amount, productService.showAmount(lays.getId()));
    }

    @Test
    public void setAndShow_productPrice_Ok() {
        int price = 45;
        productService.setPrice(lays.getId(), price);
        Assertions.assertEquals(price, productService.showPrice(lays.getId()));
    }

    @Test
    public void update_group_Ok() {
        String name = "zakuson";
        snacks.setName(name);
        snacks = groupService.update(snacks);
        Assertions.assertEquals(snacks, groupService.get(snacks.getId()));
    }

    @Test
    public void update_product_Ok() {
        String name = "lays chervona ikra";
        lays.setName(name);
        lays = productService.update(lays);
        Assertions.assertEquals(lays, productService.get(lays.getId()));
    }
}

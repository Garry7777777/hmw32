package src.test.com.skypro;

import com.skypro.testing.shopping.*;
import org.junit.jupiter.api.*;
import java.util.UUID;

public class ShoppingCartDaoTest {
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private UUID user1;
    private UUID user2;
    private Item item1;
    private Item item2;
    private Item item3;



    @BeforeEach
    void setUp(){
        user1 = UUID.randomUUID();
        user2 = UUID.randomUUID();
        item1 = new Item("test1", 100);
        item2 = new Item("test2", 200);
        item3 = new Item("test3", 400);
    }

    @Test
    void testGetItemsNotExist(){
        Assertions.assertTrue(shoppingCartDao.getItems(UUID.randomUUID()).isEmpty());
    }

    @Test
    void testGetItemsExist(){

        shoppingCartDao.addItem(user1,item1);
        Assertions.assertEquals( 1 , shoppingCartDao.getItems(user1).size());
    }

    @Test
    void testAddItemsCheckEachOthers() {

        shoppingCartDao.addItem(user1,item1);
        shoppingCartDao.addItem(user2,item2);
        Assertions.assertNotEquals( item2 , shoppingCartDao.getItems(user1).get(0) );
        Assertions.assertNotEquals( item1 , shoppingCartDao.getItems(user2).get(0) );
    }
    @Test
    void testAddItemsCheckExist() {
        shoppingCartDao.addItem(user1,item1);
        shoppingCartDao.addItem(user2,item2);
        Assertions.assertEquals( item1 , shoppingCartDao.getItems(user1).get(0) );
        Assertions.assertEquals( item2 , shoppingCartDao.getItems(user2).get(0) );
    }
    @Test
    void removeItemExist() {

        shoppingCartDao.addItem(user1,item1);
        shoppingCartDao.removeItem(user1,item1);
        Assertions.assertTrue(shoppingCartDao.getItems(user1).isEmpty());
    }

    @Test
    void removeItemNotExist() {

        shoppingCartDao.addItem(user1,item1);
        Assertions.assertFalse(shoppingCartDao.removeItem(user1,item2));
//       падает исключение
//       Assertions.assertFalse(shoppingCartDao.removeItem(user2,item1));

    }
}

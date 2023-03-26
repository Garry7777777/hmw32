package src.test.com.skypro;

import com.skypro.testing.shopping.Item;
import com.skypro.testing.shopping.ShoppingCart;
import com.skypro.testing.shopping.ShoppingCartDao;
import com.skypro.testing.shopping.exception.CartValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class ShoppingCartTest {

    private UUID user1;
    private UUID user2;
    private Item item1;
    private Item item2;
    private Item item3;

    @Mock
    public ShoppingCartDao shoppingCartDaoMock;

    @InjectMocks
    public ShoppingCart shoppingCart;


    @BeforeEach
    void setUp(){
        user1 = UUID.randomUUID();
        user2 = UUID.randomUUID();
        item1 = new Item("test1", 100);
        item2 = new Item("test2", 200);
        item3 = new Item("test3", 400);
    }

    @Test
    void addToCart_NotNulls(){
        Mockito.doNothing().when( shoppingCartDaoMock).addItem( any(UUID.class), any(Item.class));
        shoppingCart.addToCart(user1, item1);
        verify(shoppingCartDaoMock, times(1)).addItem(user1, item1);
    }

    @Test
    void addToCart_Nulls() {

        Assertions.assertThrows(CartValidationException.class,
                () -> { shoppingCart.addToCart(null, new Item("anyString", new Random().nextInt())); });
        Assertions.assertThrows(CartValidationException.class,
                () -> { shoppingCart.addToCart(UUID.randomUUID(), null);});
    }

    @Test
    void removeFromCart_NotNull() {
        Mockito.when(shoppingCartDaoMock.removeItem(any(UUID.class), any(Item.class))).thenReturn(true);
        shoppingCart.removeFromCart(user1, item1);
        verify(shoppingCartDaoMock, times(1)).removeItem(user1, item1);
    }

    @Test
    void removeFromCart_Nulls(){
        Assertions.assertThrows(CartValidationException.class,
                () -> {shoppingCart.removeFromCart(UUID.randomUUID(), null);});
        Assertions.assertThrows(CartValidationException.class,
                () -> {shoppingCart.removeFromCart(null, new Item("anyString",new Random().nextInt()));});
    }

    @Test
    void calculateCartItemsCount_NotNulls() {
        Mockito.when(shoppingCartDaoMock.getItems(any(UUID.class))).thenReturn(List.of(item1, item2, item3));
        Assertions.assertEquals(3, shoppingCart.calculateCartItemsCount(user1));
    }

    @Test
    void calculateCartItemsCount_Null() {
        Assertions.assertEquals(0, shoppingCart.calculateCartItemsCount(null));
    }

    @Test
    void calculateTotalInCart_NotNulls() {
        Mockito.when(shoppingCartDaoMock.getItems(any(UUID.class))).thenReturn(List.of(item1, item2, item3));
        Assertions.assertEquals(item1.getPrice()+item2.getPrice()+item3.getPrice(),
                shoppingCart.calculateTotalInCart(randomUUID()));
    }

    @Test
    void calculateTotalInCart_Nulls() {
        Assertions.assertEquals(0, shoppingCart.calculateTotalInCart(randomUUID()));
        Assertions.assertEquals(0, shoppingCart.calculateTotalInCart(null));
    }

}

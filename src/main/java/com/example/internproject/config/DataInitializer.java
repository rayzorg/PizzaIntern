package com.example.internproject.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.util.Set;
import com.example.internproject.models.Pizza;
import com.example.internproject.models.Role;
import com.example.internproject.models.Topping;
import com.example.internproject.models.User;
import com.example.internproject.services.PizzaService;
import com.example.internproject.services.ToppingService;
import com.example.internproject.services.UserService;

@Configuration
public class DataInitializer{

    @Bean
    public CommandLineRunner loadMenu(PizzaService pizzaService, ToppingService toppingService,UserService userService) {
        return args -> {
/*
        	//users
        	User user3=userService.register("customer", "customer@customer.com", "1234","frfrfrfrfr",Role.CUSTOMER);
        	User user1=userService.register("admin", "admin@admin.com", "1234","055604859",Role.ADMIN);
            // --- Toppings ---
        	
        	
           
                
               
            Topping cheese = toppingService.addTopping(new Topping("Cheese"));
            Topping pepperoni = toppingService.addTopping(new Topping("Pepperoni"));
            Topping ham = toppingService.addTopping(new Topping("Ham"));
            Topping pineapple = toppingService.addTopping(new Topping("Pineapple"));
            Topping mushroom = toppingService.addTopping(new Topping("Mushroom"));
            Topping olives = toppingService.addTopping(new Topping("Olives"));
            Topping bacon = toppingService.addTopping(new Topping("Bacon"));
            Topping chicken = toppingService.addTopping(new Topping("Chicken"));
            Topping onions = toppingService.addTopping(new Topping("Onions"));
            Topping bbq = toppingService.addTopping(new Topping("BBQ Sauce"));
            Topping mozzarella = toppingService.addTopping(new Topping("Mozzarella"));
            Topping cheddar = toppingService.addTopping(new Topping("Cheddar"));
            Topping parmesan = toppingService.addTopping(new Topping("Parmesan"));
            Topping gorgonzola = toppingService.addTopping(new Topping("Gorgonzola"));

            // --- Pizzas ---
            Pizza margherita = new Pizza(
                    "Margherita",
                    "Classic cheese pizza",
                    new BigDecimal("8.50"),
                    "pizzas/margherita.jpg"
            );
            margherita.setToppings(Set.of(cheese));
            pizzaService.addPizza(margherita);

            Pizza pepperoniPizza = new Pizza(
                    "Pepperoni",
                    "Spicy pepperoni pizza",
                    new BigDecimal("10.00"),
                    "pizzas/pepperoni.jpg"
            );
            pepperoniPizza.setToppings(Set.of(cheese, pepperoni));
            pizzaService.addPizza(pepperoniPizza);

            Pizza hawaiian = new Pizza(
                    "Hawaiian",
                    "Ham and pineapple",
                    new BigDecimal("9.50"),
                    "pizzas/hawai.jpg"
            );
            hawaiian.setToppings(Set.of(cheese, ham, pineapple));
            pizzaService.addPizza(hawaiian);

            Pizza mushroomDelight = new Pizza(
                    "Mushroom Delight",
                    "Cheese and fresh mushrooms",
                    new BigDecimal("9.00"),
                    "pizzas/mushrooms.jpg"
            );
            mushroomDelight.setToppings(Set.of(cheese, mushroom));
            pizzaService.addPizza(mushroomDelight);

            Pizza meatLovers = new Pizza(
                    "Meat Lovers",
                    "Pepperoni, bacon, chicken",
                    new BigDecimal("12.00"),
                    "pizzas/meat.jpg"
            );
            meatLovers.setToppings(Set.of(pepperoni, bacon, chicken));
            pizzaService.addPizza(meatLovers);

            Pizza veggie = new Pizza(
                    "Veggie",
                    "Mushroom, onions, olives",
                    new BigDecimal("10.00"),
                    "pizzas/veggie.jpg"
            );
            veggie.setToppings(Set.of(mushroom, onions, olives));
            pizzaService.addPizza(veggie);

            Pizza bbqChicken = new Pizza(
                    "BBQ Chicken",
                    "Chicken, BBQ sauce, onions",
                    new BigDecimal("11.50"),
                    "pizzas/bbqchicken.jpg"
            );
            bbqChicken.setToppings(Set.of(chicken, bbq, onions));
            pizzaService.addPizza(bbqChicken);

            Pizza fourCheese = new Pizza(
                    "Four Cheese",
                    "Mozzarella, cheddar, parmesan, gorgonzola",
                    new BigDecimal("11.00"),
                    "pizzas/fourcheese.jpg"
            );
            fourCheese.setToppings(Set.of(mozzarella, cheddar, parmesan, gorgonzola));
            pizzaService.addPizza(fourCheese);

            Pizza baconSupreme = new Pizza(
                    "Bacon Supreme",
                    "Bacon, cheese, onions",
                    new BigDecimal("11.00"),
                    "pizzas/bacon.jpg"
            );
            baconSupreme.setToppings(Set.of(bacon, cheese, onions));
            pizzaService.addPizza(baconSupreme);
            */
        	
        };
        
        
    }
    
    }



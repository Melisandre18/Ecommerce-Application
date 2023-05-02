package com.ecommerce.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;


@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {

    @Autowired
    private ProductRepository productService;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";

        while (!command.equals("exit")) {

            System.out.print("Enter a command\n" +
                    "1. save_product {product_id} {product_name} {price}\n" +
                    "2. purchase_product {product_id} {quantity} {price}\n" +
                    "3. order_product {product_id} {quantity}\n" +
                    "4. get_quantity_of_product {product_id}\n" +
                    "5. get_average_price {product_id}\n" +
                    "6. get_product_profit {product_id}\n" +
                    "7. get_fewest_product\n" +
                    "8. get_most_popular_product\n" +
                    "9. exit\n");

            command = scanner.nextLine();
            String[] parts = command.split(" ");
            String operation = parts[0];
            switch (operation) {
                case "save_product":
                    if (parts.length != 4) {
                        System.out.println("Invalid number of arguments for save_product command");
                        break;
                    }

                    try {
                        String productId = parts[1];
                        String productName = parts[2];
                        int price = Integer.parseInt(parts[3]);

                        Product product = new Product(productId, productName, price, 1);
                        productService.save(product);
                        System.out.println("Product saved successfully");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid argument(s) for save_product command");
                    }
                    break;

                case "purchase_product":
                    if (parts.length != 4) {
                        System.out.println("Invalid number of arguments for purchase_product command");
                        break;
                    }

                    try {
                        String productId = parts[1];
                        int quantity = Integer.parseInt(parts[2]);
                        int price = Integer.parseInt(parts[3]);

                        productService.purchase(productId, quantity, price);
                        System.out.println("Product purchased successfully");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid argument(s) for purchase_product command");
                    }
                    break;

                case "order_product":
                    if (parts.length != 3) {
                        System.out.println("Invalid number of arguments for order_product command");
                        break;
                    }
                    try {
                        String productId = parts[1];
                        int quantity = Integer.parseInt(parts[2]);

                        productService.order(productId, quantity);
                        System.out.println("Product ordered successfully");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid argument(s) for order_product command");
                    }
                    break;

                case "get_quantity_of_product":
                    if (parts.length != 2) {
                        System.out.println("Invalid number of arguments for get_quantity_of_product command");
                        break;
                    }

                    try {
                        String productId = parts[1];

                        int quantity = productService.getQuantity(productId);
                        System.out.println("Quantity of product " + productId + " is " + quantity);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid argument(s) for get_quantity_of_product command");
                    }
                    break;

                case "get_average_price":
                    if (parts.length != 2) {
                        System.out.println("Invalid number of arguments for get_average_price command");
                        break;
                    }
                    try {
                        String productId = parts[1];
                        double avgPrice = productService.getAveragePrice(productId);
                        System.out.println("Average price of product with id " + productId + " is: " + avgPrice);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "get_product_profit":
                    if (parts.length != 2) {
                        System.out.println("Invalid number of arguments for get_average_price command");
                        break;
                    }
                    try {
                        String productId = parts[1];
                         double profit = productService.getProductProfit(productId);
                        System.out.println("Profit of product with id " + productId + " is: " +profit);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "get_fewest_product":
                    try {
                        String productName = productService.getFewestProduct();
                        System.out.println("Product with the fewest quantity: " + productName);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "get_most_popular_product":
                    try {
                        String productName = productService.getMostPopularProduct();
                        System.out.println("The most popular product is " + productName);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
        }
        System.exit(0);
    }
}

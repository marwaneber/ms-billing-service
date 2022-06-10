package com.example.billingservice;

import com.example.billingservice.Repository.BillRepository;
import com.example.billingservice.Repository.ProductItemRepository;
import com.example.billingservice.entities.Bill;
import com.example.billingservice.entities.ProductItem;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductItemRestClient;
import com.example.billingservice.model.Customer;
import com.example.billingservice.model.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.Random;

@SpringBootApplication

@EnableFeignClients
public class  BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(
            BillRepository billRepository,
            ProductItemRepository productItemRepository,
            CustomerRestClient customerRestClient,
            ProductItemRestClient productItemRestClient,
            RepositoryRestConfiguration restConfiguration
        ){



        restConfiguration.exposeIdsFor(Bill.class);
        return args -> {
            Customer customer = customerRestClient.getCustomerById(1L);
            Bill bill1 = billRepository.save(new Bill(null, new Date(), null, customer.getId(), customer));
            PagedModel<Product> productPagedModel = productItemRestClient.pageProducts();
            productPagedModel.forEach(p -> {
                ProductItem productItem = new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1 + new Random().nextInt((int) p.getQuantity()));
                productItem.setBill(bill1);
                productItem.setName(p.getName());
                productItem.setProductID(p.getId());
                productItemRepository.save(productItem);
            });

            Customer customer1 = customerRestClient.getCustomerById(2L);
            Bill bill2 = billRepository.save(new Bill(null, new Date(), null, customer1.getId(), customer1));
            PagedModel<Product> productPagedModel1 = productItemRestClient.pageProducts();
            productPagedModel1.forEach(p -> {
                ProductItem productItem = new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1 + new Random().nextInt((int) p.getQuantity()));
                productItem.setBill(bill2);
                productItem.setProductID(p.getId());
                productItemRepository.save(productItem);
            });
            // Thrive data of Customer 1
            System.out.println("---------------------");
            System.out.print(customer.getId());
            System.out.print(" - ");
            System.out.print(customer.getName());
            System.out.print(" - ");
            System.out.println(customer.getEmail());

            // Thrive data of Customer 1
            System.out.println("---------------------");
            System.out.print(customer1.getId());
            System.out.print(" - ");
            System.out.print(customer1.getName());
            System.out.print(" - ");
            System.out.println(customer1.getEmail());
        };
    }

}

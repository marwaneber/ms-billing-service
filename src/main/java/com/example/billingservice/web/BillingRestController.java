package com.example.billingservice.web;

import com.example.billingservice.Repository.BillRepository;
import com.example.billingservice.Repository.ProductItemRepository;
import com.example.billingservice.entities.Bill;
import com.example.billingservice.feign.CustomerRestClient;
import com.example.billingservice.feign.ProductItemRestClient;
import com.example.billingservice.model.Customer;
import com.example.billingservice.model.Product;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class BillingRestController {
    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductItemRestClient productItemRestClient;

    public BillingRestController(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerRestClient customerRestClient, ProductItemRestClient productItemRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productItemRestClient = productItemRestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill (@PathVariable (name = "id") Long id){
        Bill bill = billRepository.findById(id).get();
        Customer customer = customerRestClient.getCustomerById(bill.getCustomerId());
        bill.setCustomer(customer);
        bill.getProductItems().forEach(pi -> {
            Product product = productItemRestClient.getProductById(pi.getProductID());
            pi.setProduct(product);
        });
        return bill;
    }


    @GetMapping(path = "/allBill")
    public List<Bill> getAllBill (){
        List<Bill> bill = billRepository.findAll();
        bill.forEach(bi -> {
            Customer customer = customerRestClient.getCustomerById(bi.getCustomerId());
            bi.setCustomer(customer);
            bi.getProductItems().forEach(pi -> {
                Product product = productItemRestClient.getProductById(pi.getProductID());
                pi.setProduct(product);
            });
        });

        return bill;
    }

}

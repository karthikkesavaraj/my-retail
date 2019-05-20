package com.myretail.myretailpdp.resources

import com.myretail.myretailpdp.dao.PriceDao
import com.myretail.myretailpdp.models.Product
import com.myretail.myretailpdp.models.Price
import com.myretail.myretailpdp.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(){

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/v1/product/{id}")
    fun getProduct(@PathVariable("id") id: Long) : Product{
//        println("log value is $id")
        return productService.getById(id)
    }

    @PutMapping("/v1/product")
    fun updateProduct(@RequestBody product: Product) : Product{
        return productService.update(product)
    }
}
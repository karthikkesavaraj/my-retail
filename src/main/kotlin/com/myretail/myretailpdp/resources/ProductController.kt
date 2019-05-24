package com.myretail.myretailpdp.resources

import com.mongodb.MongoException
import com.myretail.myretailpdp.models.Product
import com.myretail.myretailpdp.models.ProductPrice
import com.myretail.myretailpdp.services.ProductService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import reactor.core.publisher.Mono
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/v1/product")
class ProductController(){

    val logger = LoggerFactory.getLogger(ProductController::class.java)

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id: Long) : Mono<Product>{
        if (logger.isDebugEnabled)
            logger.debug("Got GET Request for Product $id")
        return productService.getById(id)
    }

    @PutMapping("")
    fun updateProduct(@RequestBody product: Product) : Mono<Product>{
        if (logger.isDebugEnabled)
            logger.debug("Got PUT Request for Product ${product.id}")
        return productService.updateProductPrice(product)
    }


    @ExceptionHandler(value=[(MongoException::class)])
    fun handleException(response: HttpServletResponse){
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to process request at this moment.")
    }
}


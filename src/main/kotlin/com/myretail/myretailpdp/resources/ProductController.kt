package com.myretail.myretailpdp.resources

import com.mongodb.MongoException
import com.myretail.myretailpdp.models.Product
import com.myretail.myretailpdp.services.ProductService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.async.DeferredResult
import rx.Observable
import javax.servlet.http.HttpServletResponse


@RestController
class ProductController(){

    val logger = LoggerFactory.getLogger(ProductController::class.java)

    @Autowired
    lateinit var productService: ProductService

    @GetMapping("/v1/product/{id}")
    @SuppressWarnings()
    fun getProduct(@PathVariable("id") id: Long) : DeferredResult<Product>{
        if (logger.isDebugEnabled)
            logger.debug("Got GET Request for Product $id")
        var defferedResult: DeferredResult<Product> = DeferredResult<Product>()

        var product_observable : Observable<Product> = productService.getById(id)
        logger.debug("Got observable")
        product_observable.subscribe({product -> defferedResult.setResult(product)})
        return defferedResult
    }

    @PutMapping("/v1/product")
    fun updateProduct(@RequestBody product: Product) : Product{
        if (logger.isDebugEnabled)
            logger.debug("Got PUT Request for Product ${product.id}")
        return productService.updateProductPrice(product)
    }


    @ExceptionHandler(value=[(MongoException::class)])
    fun handleException(response: HttpServletResponse){
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to process request at this moment.")
    }
}


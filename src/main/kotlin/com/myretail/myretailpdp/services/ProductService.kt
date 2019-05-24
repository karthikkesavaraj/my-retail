package com.myretail.myretailpdp.services

import com.myretail.myretailpdp.dao.PriceDao
import com.myretail.myretailpdp.dao.ProductDao
import com.myretail.myretailpdp.models.Price
import com.myretail.myretailpdp.models.Product
import com.myretail.myretailpdp.models.ProductPrice
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct

@Service
class ProductService(){

    val logger = LoggerFactory.getLogger(ProductService::class.java)

    @Autowired
    lateinit var priceDao: PriceDao

    @Autowired
    lateinit var productDao: ProductDao

    fun getById(id: Long): Mono<Product>{
        val product_name_mono: Mono<String> = Mono.fromCallable { getProductName(id) }
        val price_mono: Mono<ProductPrice> = getProductPrice(id)
        val product_mono: Mono<Product> = Mono.zip(product_name_mono,price_mono,{product_name,price -> Product(id,product_name,price.price)})
        return product_mono
    }

    @Throws(Exception::class)
    @HystrixCommand(commandKey = "productPriceCommand")
    fun getProductPrice(id:Long): Mono<ProductPrice> {
        return priceDao.findById(id)
    }

    @Throws(Exception::class)
    @HystrixCommand(commandKey = "productNameCommand")
    open fun getProductName(id:Long): String {
        val productData = productDao.getProductDetails(id)
        return productData.product.item.product_description.title
    }

    @Throws(Exception::class)
    fun updateProductPrice(product: Product): Mono<Product> {
        return priceDao.save(ProductPrice(id=product.id,price = product.current_price))
                .map { product_price -> Product(product.id,product.name,product_price.price) }

    }

    @PostConstruct
    fun initialLoad(){
        logger.info("Initial Data Load - Start")

        val total = sequenceOf(ProductPrice(15117729,Price(100.00)),
                ProductPrice(164883589,Price(200.00)),
                ProductPrice(16696652,Price(300.00)),
                ProductPrice(16752456,Price(400.00)),
                ProductPrice(15643793,Price(500.00)),
                ProductPrice(13860428,Price(500.00)),
                ProductPrice(54382797,Price(7.50))).map { price -> priceDao.save(price) }.count()
        logger.info("Initial Data Load - Done $total")

        logger.info("Initial Data Load - Done")
    }

}
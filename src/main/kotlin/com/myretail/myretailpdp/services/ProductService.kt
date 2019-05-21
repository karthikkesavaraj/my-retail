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
import javax.annotation.PostConstruct

@Service
class ProductService(){

    val logger = LoggerFactory.getLogger(ProductService::class.java)

    @Autowired
    lateinit var priceDao: PriceDao

    @Autowired
    lateinit var productDao: ProductDao

    fun getById(id: Long): Product{
        val product_name: String = getProductName(id)
        val price: Price = getProductPrice(id)
        return Product(id,product_name,price)
    }

    @Throws(Exception::class)
    @HystrixCommand(commandKey = "productPriceCommand")
    open fun getProductPrice(id:Long): Price {
        val productPrice:ProductPrice? = priceDao.findByIdOrNull(id)
        if (productPrice != null){
            return productPrice.price
        }else{
            throw ResponseStatusException(HttpStatus.NOT_FOUND,"product, price $id not found")
        }
    }

    @Throws(Exception::class)
    @HystrixCommand(commandKey = "productNameCommand")
    open fun getProductName(id:Long): String {
        val productData = productDao.getProductDetails(id)
        return productData.product.item.product_description.title
    }

    @Throws(Exception::class)
    fun updateProductPrice(product: Product): Product {
        val productPrice: ProductPrice = priceDao.save(ProductPrice(id=product.id,price = product.current_price))
        return Product(product.id,product.name,productPrice.price)
    }

    @PostConstruct
    fun initialLoad(){
        logger.info("Initial Data Load - Start")

        /* Not Working Product */
        if(!priceDao.existsById(15117729)){
            priceDao.save(ProductPrice(15117729,Price(100.00)))
        }

        /* Not Working Product */
        if(!priceDao.existsById(164883589)){
            priceDao.save(ProductPrice(164883589,Price(200.00)))
        }

        /* Not Working Product */
        if(!priceDao.existsById(16696652)){
            priceDao.save(ProductPrice(16696652,Price(300.00)))
        }

        /* Not Working Product */
        if(!priceDao.existsById(16752456)){
            priceDao.save(ProductPrice(16752456,Price(400.00)))
        }

        /* Not Working Product */
        if(!priceDao.existsById(15643793)){
            priceDao.save(ProductPrice(15643793,Price(500.00)))
        }

        /* Working Product */
        if(!priceDao.existsById(13860428)){
            priceDao.save(ProductPrice(13860428,Price(500.00)))
        }

        if(!priceDao.existsById(54382797)){
            priceDao.save(ProductPrice(54382797,Price(7.50)))
        }
        logger.info("Initial Data Load - Done")
    }

}
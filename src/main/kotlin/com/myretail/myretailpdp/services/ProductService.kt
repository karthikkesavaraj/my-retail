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
import rx.Observable
import rx.Subscriber
import rx.functions.Func2
import java.util.stream.Stream
import javax.annotation.PostConstruct

@Service
class ProductService(){

    val logger = LoggerFactory.getLogger(ProductService::class.java)

    @Autowired
    lateinit var priceDao: PriceDao

    @Autowired
    lateinit var productDao: ProductDao

    fun getById(id: Long): Observable<Product>{
        /* Creating Observable to fetch product name*/
        val product_name_observable: Observable<String>  = Observable.create<String>{s ->
            s.onNext(getProductName(id))
            s.onCompleted()
        }

        /* Creating Observable to fetch price */
        val price_observable: Observable<Price> = Observable.create<Price>{ s->
            s.onNext(getProductPrice(id))
            s.onCompleted()
        }

        /* Zip Observables for concurrent calls*/
        val product_observable: Observable<Product> =
                Observable.zip(product_name_observable,price_observable,{
                    product_name,price -> Product(id,product_name,price)
                })
        return product_observable
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
    fun getProductName(id:Long): String {

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
        /* Using Kotlin streams */
        val total = sequenceOf(ProductPrice(15117729,Price(100.00)),
                ProductPrice(164883589,Price(200.00)),
            ProductPrice(16696652,Price(300.00)),
            ProductPrice(16752456,Price(400.00)),
            ProductPrice(15643793,Price(500.00)),
            ProductPrice(13860428,Price(500.00)),
            ProductPrice(54382797,Price(7.50))).map { price -> priceDao.save(price) }.count()
        logger.info("Initial Data Load - Done $total")
    }

}
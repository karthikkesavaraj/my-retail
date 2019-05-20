package com.myretail.myretailpdp.services

import com.myretail.myretailpdp.dao.PriceDao
import com.myretail.myretailpdp.dao.ProductDao
import com.myretail.myretailpdp.models.Price
import com.myretail.myretailpdp.models.Product
import com.myretail.myretailpdp.models.ProductPrice
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class ProductService(){

    @Autowired
    lateinit var priceDao: PriceDao

    @Autowired
    lateinit var productDao: ProductDao

    fun getById(id: Long): Product{
        var productPrice = priceDao.findByIdOrNull(id) as ProductPrice
        var productData = productDao.getProductDetails(id)
        return Product(id,productData.product.item.product_description.title, productPrice.price)
    }

    @Throws(Exception::class)
    fun update(product: Product): Product {
        return if(priceDao.existsById(product.id)){
            val productPrice:ProductPrice = priceDao.save(ProductPrice(id=product.id,price = product.current_price))
            Product(product.id,product.name,productPrice.price)
        }else{
            throw object: Exception("Product doesnot exist"){}
        }
    }

    @PostConstruct
    fun initialLoad(){
        println("Hello I am loading initial data")

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
    }

}
package com.myretail.myretailpdp.dao

import com.myretail.myretailpdp.models.ProductPrice
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PriceDao:ReactiveMongoRepository<ProductPrice,Long>{
}
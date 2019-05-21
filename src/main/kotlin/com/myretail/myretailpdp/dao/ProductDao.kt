package com.myretail.myretailpdp.dao

import com.myretail.myretailpdp.models.productresponse.ProductResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException
import java.util.HashMap

@Repository
class ProductDao {

    val logger = LoggerFactory.getLogger(ProductDao::class.java)

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${product.url}")
    lateinit var url: String

    fun getProductDetails(id:Long):ProductResponse {
        val entity:HttpEntity<HttpHeaders> = HttpEntity(getHeaders())
        val productId = HashMap<String, String>()
        productId["id"] = id.toString()
        try {
            if (logger.isDebugEnabled){
                logger.debug("Retriving product information for $id")
            }
            var productEntity: ResponseEntity<ProductResponse> =
                    restTemplate.exchange(url, HttpMethod.GET, entity, ProductResponse::class.java, productId)
            if (logger.isDebugEnabled){
                logger.debug("product data ${productEntity.body}")
            }
            return productEntity.body as ProductResponse
        }catch(e: HttpClientErrorException){
            logger.error("Error retriving Product $id",e)
            throw ResponseStatusException(e.statusCode, "unable to retrive product name for product $id")
        }
    }

    fun getHeaders():HttpHeaders {
        var headers = HttpHeaders()
        headers.set("Accept", "application/json");
        return headers
    }

}
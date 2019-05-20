package com.myretail.myretailpdp.dao

import com.myretail.myretailpdp.models.productresponse.ProductResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.getForObject
import java.util.HashMap

@Repository
class ProductDao {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Value("\${product.url}")
    lateinit var url: String

    fun getProductDetails(id:Long):ProductResponse {
        val entity:HttpEntity<ProductResponse> = HttpEntity(getHeaders())
        val productId = HashMap<String, String>()
        productId["id"] = id.toString()
        var productEntity: ResponseEntity<ProductResponse> =
               restTemplate.exchange(url,HttpMethod.GET,entity,ProductResponse::class.java,productId)
        return productEntity.body as ProductResponse

    }

    fun getHeaders():HttpHeaders {
        var headers = HttpHeaders()
        headers.set("Accept", "application/json");
        return headers
    }

}
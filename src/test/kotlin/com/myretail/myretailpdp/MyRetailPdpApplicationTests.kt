package com.myretail.myretailpdp

import com.myretail.myretailpdp.dao.PriceDao
import com.myretail.myretailpdp.services.ProductService
import org.assertj.core.api.Assertions.assertThat

import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestTemplate
import com.myretail.myretailpdp.models.Price
import com.myretail.myretailpdp.models.ProductPrice
import com.myretail.myretailpdp.models.productresponse.Item
import com.myretail.myretailpdp.models.productresponse.Product
import com.myretail.myretailpdp.models.productresponse.ProductDescription
import com.myretail.myretailpdp.models.productresponse.ProductResponse
import com.myretail.myretailpdp.resources.ProductController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpEntity
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpMethod
import org.mockito.BDDMockito.given
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.core.MongoTemplate
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
//@EnableAutoConfiguration(exclude=[(MongoAutoConfiguration::class)])
class MyRetailPdpApplicationTests {
	@Value("\${product.url}")
	lateinit var url: String

	@Autowired
	private lateinit var controller: ProductController

	@Autowired
	private lateinit var service: ProductService

	@MockBean
	private lateinit var restTemplate: RestTemplate

	@MockBean
	private lateinit var priceDao: PriceDao

	@Before
	fun setup(){
		MockitoAnnotations.initMocks(this);
		setUpData()
	}

	private fun getHeaders(): HttpHeaders {
		val headers = HttpHeaders()
		headers.set("Accept", "application/json")
		return headers
	}


	private fun setUpData(){
		val entity:HttpEntity<HttpHeaders> = HttpEntity(getHeaders())
		val productId = HashMap<String, String>()
		productId["id"] = 123.toString()
		val productResponse = ProductResponse(Product(Item(ProductDescription("title for 123"), "123")))
		val responseEntity = ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK)
		val id:Long = 123
		val priceObject:ProductPrice = ProductPrice(123,com.myretail.myretailpdp.models.Price(99.9))

		Mockito.doReturn(responseEntity).`when`(restTemplate).exchange(
				url, HttpMethod.GET, entity, ProductResponse::class.java, productId
		)
		Mockito.doReturn(Optional.of(priceObject)).`when`(priceDao).findById(id)
	}

	@Test
	fun contextLoads() {
		assertThat(controller).isNotNull()
	}

	@Test
	fun controllerTest(){
		val product: com.myretail.myretailpdp.models.Product = controller.getProduct(123)
		assertThat(product.name).isEqualTo("title for 123")
	}



}

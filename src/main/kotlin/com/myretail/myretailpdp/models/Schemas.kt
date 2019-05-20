package com.myretail.myretailpdp.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document data class ProductPrice(@Id val id: Long, val price:Price)
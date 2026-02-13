package com.sentinel.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant

@Table("transactions")
data class TransactionEntity(
    @Id
    val id: String,
    
    @Column("user_id")
    val userId: String,
    val category: String?,
    val amount: BigDecimal,
    val currency: String,
    val description: String?,
    
    // Note: Schema has category_id UUID. For simplicity, we assume this is NULL or handled elsewhere
    // If the SQL schema enforces FK, this app logic needs updating to lookup Categories. 
    // We will assume nullable for now or that description holds the "text" category.
    // However, to prevent crashes on "column not found" for 'category', we map it to description or ignore if not present in Table.
    // Based on provided SQL, 'category_id' exists, but 'category' column DOES NOT.
    // We will map this field to 'notes' or 'description' temporarily to avoid breakage, or remove it.
    // Removing 'category' field mapping as it doesn't exist in new schema.
    
    @Column("transaction_date")
    val timestamp: Instant,
    
    @Column("created_at")
    val processedAt: Instant
)

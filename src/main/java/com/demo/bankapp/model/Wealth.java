package com.demo.bankapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Entity
@Table(name = "wealth")
@NoArgsConstructor // Lombok annotation for no-args constructor (assuming you use it)
public class Wealth {

	// 1. Primary Key: @Id marks this as the PK.
	//    @MapsId ensures this field gets its value from the 'user' relationship.
	@Id
	@Column(name = "user_id")
	private Long userId;

	// 2. Relationship: This links the Wealth entity to the User entity.
	@OneToOne
	@MapsId // Crucial: Maps the primary key (userId) of this entity to the primary key of the User entity.
	@JoinColumn(name = "user_id") // Specifies the foreign key column name.
	private User user; // Reference to the parent User entity

	// 3. Element Collection: Handles the Map data in a separate join table.
	@ElementCollection
	private Map<String, BigDecimal> wealthMap;

	// Custom constructor should now accept the User entity
	public Wealth(User user, Map<String, BigDecimal> wealthMap) {
		this.user = user;
		this.wealthMap = wealthMap;
		// The userId field is automatically set when persisting due to @MapsId
	}
}

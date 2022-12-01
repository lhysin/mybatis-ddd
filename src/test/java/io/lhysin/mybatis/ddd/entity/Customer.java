package io.lhysin.mybatis.ddd.entity;

import java.time.LocalDateTime;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Customer.
 */
@Getter
@Table(name = "CUSTOMER", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

	@Id
	@Column(name = "CUST_NO")
	@EqualsAndHashCode.Include
	private String custNo;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "AGE")
	private Integer age;

	@Column(name = "CREATED_AT", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "UPDATED_AT", insertable = false)
	private LocalDateTime updatedAt;

	/**
	 * Plus age.
	 *
	 * @param age the age
	 */
	public void plusAge(Integer age) {
		this.age = this.age + age;
	}
}

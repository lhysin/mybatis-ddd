package io.lhysin.mybatis.ddd;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.lhysin.mybatis.ddd.domain.PageRequest;
import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.entity.Cart;
import io.lhysin.mybatis.ddd.entity.Customer;
import io.lhysin.mybatis.ddd.entity.Item;
import io.lhysin.mybatis.ddd.entity.Order;
import io.lhysin.mybatis.ddd.mapper.CartMapper;
import io.lhysin.mybatis.ddd.mapper.CustomerMapper;
import io.lhysin.mybatis.ddd.mapper.CustomerXmlMapper;
import io.lhysin.mybatis.ddd.mapper.DummyMapper;
import io.lhysin.mybatis.ddd.mapper.ItemMapper;
import io.lhysin.mybatis.ddd.mapper.OrderMapper;
import io.lhysin.mybatis.ddd.mapper.StudentMapper;
import io.lhysin.mybatis.ddd.spec.Pageable;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Crud tests.
 */
@Slf4j
@SpringBootTest
class CrudTests {

	@Autowired
	private CustomerXmlMapper customerXmlMapper;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private StudentMapper studentMapper;

	@Autowired
	private DummyMapper dummyMapper;

	/**
	 * Insertable false test.
	 */
	@Test
	void insertableFalseTest() {

		Customer freshCustomer = Customer.builder()
			.custNo("FRESH_CUSTOMER")
			.firstName("FIRST_N")
			.lastName("LAST_N_")
			.age(10)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		customerMapper.create(freshCustomer);

		Customer foundFreshCustomer = customerMapper.findById(freshCustomer.getCustNo())
			.orElseThrow(NoSuchElementException::new);
		assertNull(foundFreshCustomer.getUpdatedAt());
	}

	/**
	 * Updatable false test.
	 */
	@Test
	void updatableFalseTest() {
		Customer updatableCstomer = Customer.builder()
			.custNo("20220101")
			.firstName("FIRST_N")
			.lastName("LAST_N_")
			.age(20)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		customerMapper.update(updatableCstomer);

		Customer updatedCustomer = customerMapper.findById(updatableCstomer.getCustNo())
			.orElseThrow(NoSuchElementException::new);

		assertNotEquals(0, updatedCustomer.getCreatedAt().compareTo(updatableCstomer.getCreatedAt()));
	}

	/**
	 * Sequence test.
	 */
	@Test
	void sequenceTest() {
		Cart cart = Cart.builder().custNo("20220101").build();
		cartMapper.create(cart);

		assertNotNull(cart.getCartSeq());

		List<Order> orders = IntStream.range(0, 100)
			.mapToObj(i -> Order.builder()
				.custNo("20220109")
				.ordNo("order_".concat(i + ""))
				.build())
			.collect(Collectors.toList());

		orders.forEach(it -> orderMapper.create(it));

		assertNotNull(orders.get(10).getOrdSeq());

		Item item = Item.builder().build();
		itemMapper.create(item);

	}

	/**
	 * Create all test.
	 */
	@Test
	void createAllTest() {

		long count = customerMapper.count();
		List<Customer> customers = IntStream.range(0, 1000)
			.mapToObj(i -> Customer.builder()
				.custNo("2022_BULK_".concat(i + ""))
				.firstName("FIRST_N_".concat(i + ""))
				.lastName("LAST_N_".concat(i + ""))
				.age(i)
				.build())
			.collect(Collectors.toList());

		customerMapper.createAll(customers);

		assertEquals(customerMapper.count() - count, customers.size());

	}

	/**
	 * Count customer test.
	 */
	@Test
	void countCustomerTest() {
		long count = customerMapper.count();
		assertTrue(count > 0);
	}

	/**
	 * Dynamic update test.
	 */
	@Test
	void dynamicUpdateTest() {
		String custNo1 = "20220101";
		customerMapper.update(Customer.builder()
			.custNo(custNo1)
			.age(null)
			.updatedAt(LocalDateTime.now())
			.build());
		Customer updatedCustomer1 = customerMapper.findById(custNo1).orElseThrow(NoSuchElementException::new);

		assertNull(updatedCustomer1.getAge());

		String custNo2 = "20220102";
		customerMapper.dynamicUpdate(Customer.builder()
			.custNo(custNo2)
			.age(null)
			.updatedAt(LocalDateTime.now())
			.build());
		Customer dynamicUpdatedCustomer2 = customerMapper.findById(custNo2).orElseThrow(NoSuchElementException::new);
		assertNotNull(dynamicUpdatedCustomer2.getAge());

		Exception exception = assertThrows(Exception.class, () -> {
			customerMapper.dynamicUpdate(Customer.builder()
				.build());
		});

		assertTrue(exception.getMessage().contains("Not Exists"));
	}

	/**
	 * Paging and sorting test.
	 */
	@Test
	void pagingAndSortingTest() {
		Sort sort = Sort.by("AGE")
			.and(Sort.Direction.DESC, "FIRST_NAME");
		Pageable req = PageRequest.of(2, 3, sort);
		List<Customer> customers = customerMapper.findAll(req);

		assertFalse(customers.isEmpty());

		// check safty order by
		Sort fakeSort = Sort.by(Sort.Direction.ASC, "asd")
			.and("OOO")
			.and("exists")
			.and("INSERT INTO");
		Pageable fakePageable = PageRequest.of(0, 3, fakeSort);
		List<Customer> fakeCustomers = customerMapper.findAll(fakePageable);

		assertFalse(fakeCustomers.isEmpty());
	}

	/**
	 * Xml mapper and crud mapper test.
	 */
	@Test
	void xmlMapperAndCrudMapperTest() {
		Customer customer1 = customerXmlMapper.findById("20220101").orElseThrow(NoSuchElementException::new);
		Customer customer2 = customerMapper.findById("20220101").orElseThrow(NoSuchElementException::new);
		assertEquals(customer1, customer2);
	}

	/**
	 * Create and find customer test.
	 */
	@Test
	void createAndFindCustomerTest() {
		Customer customer = Customer.builder()
			.custNo("2022test")
			.age(13)
			.firstName("test1")
			.lastName("lastName2")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		customerMapper.create(customer);

		Customer createdCustomer = customerMapper.findById(customer.getCustNo())
			.orElseThrow(NoSuchElementException::new);
		assertEquals(customer, createdCustomer);
	}

	/**
	 * Find and update customer test.
	 */
	@Test
	void findAndUpdateCustomerTest() {
		Customer customer = customerMapper.findById("20220101")
			.orElseThrow(NoSuchElementException::new);

		customer.plusAge(20);
		customerMapper.update(customer);

		Customer updatedCustomer = customerMapper.findById(customer.getCustNo())
			.orElseThrow(NoSuchElementException::new);
		assertEquals(updatedCustomer.getAge(), customer.getAge());
	}

	/**
	 * Find and delete customer test.
	 */
	@Test
	void findAndDeleteCustomerTest() {
		Customer customer = customerMapper.findById("20220108")
			.orElseThrow(NoSuchElementException::new);

		customerMapper.delete(customer);
		Customer nullableCustomer = customerMapper.findById(customer.getCustNo())
			.orElse(null);
		assertNull(nullableCustomer);
	}

	/**
	 * Find and delete order test.
	 */
	@Test
	void findAndDeleteOrderTest() {
		Order.PK pk = Order.PK.builder()
			.custNo("20220109")
			.ordNo("order01")
			.ordSeq(1)
			.build();
		Order order = orderMapper.findById(pk)
			.orElseThrow(NoSuchElementException::new);
		assertNotNull(order);

		int deleteCnt = orderMapper.deleteById(pk);
		assertTrue(deleteCnt > 0);

		Order nullableOrder = orderMapper.findById(pk)
			.orElse(null);
		assertNull(nullableOrder);
	}

	/**
	 * Find all by ids and delete by ids test.
	 */
	@Test
	void findAllByIdsAndDeleteByIdsTest() {
		List<String> customerIds = new ArrayList<String>();
		customerIds.add("20220101");
		customerIds.add("20220102");
		customerIds.add("20220103");
		List<Customer> customers = customerMapper.findAllById(customerIds);
		assertFalse(customers.isEmpty());

		List<Order.PK> orderIds = new ArrayList<Order.PK>();
		orderIds.add(Order.PK.builder()
			.custNo("20220111")
			.ordNo("order10")
			.ordSeq(3)
			.build());
		orderIds.add(Order.PK.builder()
			.custNo("20220111")
			.ordNo("order11")
			.ordSeq(4)
			.build());
		orderIds.add(Order.PK.builder()
			.custNo("20220111")
			.ordNo("order12")
			.ordSeq(5)
			.build());

		List<Order> orders = orderMapper.findAllById(orderIds);
		assertFalse(orders.isEmpty());

		int deleteCnt = orderMapper.deleteAllById(orderIds);
		assertTrue(deleteCnt > 0);
	}

}

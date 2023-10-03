# charter-demo
Watson Peng from Antra
<br/>


<br>

Project Structure
============


I used Spring Boot and Spring MVC as the main framework, hibernate with MySQL as dev database and H2 as test database, JUnit and Mockito for unit test


**Model**
------------
There are two entities：
* User - contains userId, userName, reward point, and it has one to many relationship to Transaction entity
* Transaction - contains transactionId, transactionInfo, transactionPrice and transaction Date, and it has many to one relationship to User entity with userId as foreign key

And one extra data model
* ErrorResponse - contains errorCode and errorMsg for custom error response

**Repository**
------------
Both repositories extends JpaRepository to cover basic CRUD functions.
* UserRepository - basic function for JpaRepository is enough
* TransactionRepository - two defined JpaRepository functions
    * List`<Transaction>` findByUser(User user) - find list of transactions by user
    * List`<Transaction>` findByUserAndTransactionDateBetween(User user, Date startDate, Date endDate) - find list of transactions by user within the range of start date and end date

**Service**
------------
* UserService
    * User findUserById(Long userId) - use Optional class and throw exception is user is not found
    * User saveUser(User user) - set user's initial reward point to 0 and save in repository
    * int getRewardPoint(double price) - calculate reward point based on transaction price
    * User updateRewardPoint(User user, Transaction transaction) - update reward point when there is new transaction, called by TransactionService
    * int findUserRewardPointByMonth(Long userId, int year, int month) - get user reward point by specific year and month, called TransactionService to get filtered transaction

* TransactionService 
    * List`<Transaction>` findTransactionsByUserId(Long userId) - find list of transactions by userId
    * List`<Transaction>` findByUserAndTransactionDateBetween(Long userId, int year, int month) - find list of transactions by userId within the range of start date and end date, called by UserService :: findUserRewardPointByMonth
    * Transaction saveTransaction(Transaction transaction) - call UserService :: updateRewardPoint to update related user's reward point, then save the transaction to repo
    * List`<Transaction>` saveTransactions(List`<Transaction>` transactions) - call UserService :: updateRewardPoint to update related user's reward point, then save those transactions to repo

**Controller**
------------
* UserController
    * ResponseEntity`<Void>` addUser(@RequestBody User user) - post mapping restful api to add new user
    * ResponseEntity`<User>` findUserById(@PathVariable("userId") long userId) - get mapping restful api to find user by user id
    * ResponseEntity`<Integer>` findUserTotalRewardPoint(@PathVariable("userId") long userId) - get mapping restful api to find user's total reward point
    * ResponseEntity`<Integer>` findUserMonthlyRewardPoint(@PathVariable("userId") long userId, int year, int month) - get mapping restful api to find user's reward point by month

* TransactionController
    * ResponseEntity`<Void>` createTransaction(@RequestBody Transaction transaction) - post mapping restful api to add one transaction
    * ResponseEntity`<Void>` createTransactions(@RequestBody List`<Transaction>` transactions) - post mapping restful api to add some transactions

* GlobalExceptionhandle - use @ControllerAdvice to handle exceptions
    * ResponseEntity<ErrorResponse> handleIllegalArgumentException(Exception ex) - handle IllegalArgument Exception

**Unit Testing**
------------
I created unit testing for controller, repository and service. 
* Controller - WebMvcTest, Mockito and JUnit5.
* Service - SpringBootTest, Mockito and JUnit5.
* Repository - DataJpaTest, Mockito and JUnit5.

**Database**
------------
I used hibernate with MySQL for dev database and h2 for test database
<br>
Here is the table structure
```
create table users(
	
	user_id BIGINT primary key,
	username VARCHAR(20) not null,
	reward_point INT not null
);
```
```
create table transactions(
	
	transaction_id BIGINT primary key,
	user_id bigint,
	transaction_info VARCHAR(150) not null,
	transaction_price Decimal(8,2) not null,
	transaction_date DATE not null,
	foreign key (user_id) references users(user_id)
);
```
And some test data
```
insert into User(user_id, username, reward_point) values(1, "Watson", 0);
insert into User(user_id, username, reward_point) values(2, "Peng", 0);
```
```
insert into transactions(transaction_id, transaction_date, transaction_info, transaction_price, user_id) values(1, cast('2023-03-01' as date), "transaction 1", 50,  1);
insert into transactions(transaction_id, transaction_date, transaction_info, transaction_price, user_id) values(2, cast('2023-03-11' as date), "transaction 1", 89, 1);
insert into transactions(transaction_id, transaction_date, transaction_info, transaction_price, user_id) values(3, cast('2023-04-01' as date), "transaction 1", 120, 1);
insert into transactions(transaction_id, transaction_date, transaction_info, transaction_price, user_id) values(4, cast('2023-05-01' as date), "transaction 1", 150, 2);
```

**Solution demonstration**
------------
I assume that for the front-end, admin can import transactions in csv or xlsx format. And the user will save one transaction whenever the user comeplete one. And every user starts with 0 reward point

<br>

There will be two cases for adding transactions
```
Mock Data

User1 {
    userId: 1
    username: Watson
    rewardPoint: 0
}

Transaction1 {
    userId: 1
    transactionPrice: 49.00
    transactionDate: 2023/03/01
}
Transaction2 {
    userId: 1
    transactionPrice: 89.00
    transactionDate: 2023/03/02
}
Transaction2 {
    userId: 1
    transactionPrice: 120.00
    transactionDate: 2023/04/01
}
```
```
Case 1: user completes one transaction at a time

* TransactionController::createTransaction handles the request, and pass it to transactionService::saveTransaction, finally the transaction in repository
* it will then be passed to UserService::updateRewardPoint

1. User1 first completes Transaction1
UserService::getRewardPoint will calculate reward point. In this case, it is 0. Then UserService::updateRewardPoint will update user's rewardPoint
Now the updated User1 will be
User1 {
    userId: 1
    username: Watson
    rewardPoint: 0 + 0 = 0
}

2. User1 then completes Transaction2
In this case, the new reward point will be (89-50)*1 = 39
Now the updated User1 will be
User1 {
    userId: 1
    username: Watson
    rewardPoint: 0 + 39 = 39
}

3. User1 then completes Transaction3
In this case, the new reward point will be (120-100)*2 + 50 = 90
Now the updated User1 will be
User1 {
    userId: 1
    username: Watson
    rewardPoint: 39 + 90 = 129
}

If the user wants to check total reward point, 
* UserController::findUserTotalRewardPoint will handle the request, and pass it to userService::findUserById 
* It will find the corresponding user, and the controller will return with the user's reward point property
In this case, User1 will see the total reward point as 129

If the user wants to check reward point earned in March
* UserController::findUserMonthlyRewardPoint will handle the request, and pass it to userService::findUserRewardPointByMonth
* findUserRewardPointByMonth will call transactionServic::findByUserAndTransactionDateBetween to get filtered transactions by month for this user
* findUserRewardPointByMonth will then sum up the transaction price for filtered transaction and return the monthly reward point to controller
In this case, User1 will see March reward point as 39
```

```
Case 2: admin imports some transaction in xslx/csv format

* TransactionController::createTransactions handles the request, and pass it to transactionService::saveTransactions
* saveTransactions check each transaction, extract corresponding userInfo and call UserService::getRewardPoint each time.
* After all transaction are checked, it will then save those transactions in the repository

Admin imports csv file with Transaction1, Transaction2, Transaction3
Then UserService::updateRewardPoint will keep updating user's rewardPoint
And the updated user will now be:
User1 {
    userId: 1
    username: Watson
    rewardPoint: 0 + 0 + 39 + 90 = 129
}

For checking total and monthly reward point, the procedure is the same as Case 1
```
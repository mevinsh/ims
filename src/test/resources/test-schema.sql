DROP TABLE IF EXISTS Product;
CREATE TABLE Product(
ProductId INT PRIMARY KEY IDENTITY
,ProductName VARCHAR(255)
,ProductDescription VARCHAR(255)
,ProductQuantity INT
);

DROP TABLE IF EXISTS ProductOrderRule;
CREATE TABLE ProductOrderRule(
ProductOrderRuleId INT PRIMARY KEY IDENTITY
,ProductId INT
,MinProductQuantity INT
,MaxProductQuantity INT
,OneOffOrder INT
,IsBlocked BOOLEAN
);


DROP TABLE IF EXISTS Orders;
CREATE TABLE Orders(
OrderId INT PRIMARY KEY IDENTITY
,OrderDate DATE
,ProductId INT
,ProductQuantity INT
);
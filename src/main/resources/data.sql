DROP TABLE IF EXISTS Product;
CREATE TABLE Product(
ProductId INT PRIMARY KEY AUTO_INCREMENT
,ProductName VARCHAR(255)
,ProductDescription VARCHAR(255)
,ProductQuantity INT
);

INSERT INTO Product(ProductId,ProductName,ProductDescription,ProductQuantity) VALUES(1,'A','Desc for A',1);
INSERT INTO Product(ProductId,ProductName,ProductDescription,ProductQuantity) VALUES(2,'B','Desc for B',2);
INSERT INTO Product(ProductId,ProductName,ProductDescription,ProductQuantity) VALUES(3,'C','Desc for C',2);
INSERT INTO Product(ProductId,ProductName,ProductDescription,ProductQuantity) VALUES(4,'D','Desc for D',0);
INSERT INTO Product(ProductId,ProductName,ProductDescription,ProductQuantity) VALUES(5,'E','Desc for E',1);


DROP TABLE IF EXISTS ProductOrderRule;
CREATE TABLE ProductOrderRule(
ProductOrderRuleId INT PRIMARY KEY AUTO_INCREMENT
,ProductId INT
,MinProductQuantity INT
,MaxProductQuantity INT
,OneOffOrder INT
,IsBlocked BOOLEAN
);

INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(1,4,null,null,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(2,4,null,null,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(3,4,null,null,TRUE);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(4,8,null,15,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(5,4,null,null,null);

DROP TABLE IF EXISTS Orders;
CREATE TABLE Orders(
OrderId INT PRIMARY KEY AUTO_INCREMENT
,OrderDate DATE
,ProductId INT
,ProductQuantity INT
);
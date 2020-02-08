INSERT INTO Product(ProductName,ProductDescription,ProductQuantity) VALUES('A','Desc for A',1);
INSERT INTO Product(ProductName,ProductDescription,ProductQuantity) VALUES('B','Desc for B',2);
INSERT INTO Product(ProductName,ProductDescription,ProductQuantity) VALUES('C','Desc for C',2);
INSERT INTO Product(ProductName,ProductDescription,ProductQuantity) VALUES('D','Desc for D',0);
INSERT INTO Product(ProductName,ProductDescription,ProductQuantity) VALUES('E','Desc for E',1);


INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(1,4,null,null,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(2,4,null,null,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(3,4,null,null,TRUE);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(4,8,null,15,null);
INSERT INTO ProductOrderRule(ProductId,MinProductQuantity,MaxProductQuantity,OneOffOrder,IsBlocked) VALUES(5,4,null,null,null);
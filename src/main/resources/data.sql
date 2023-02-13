USE test;

CREATE TABLE CurrencySurface (
id INT AUTO_INCREMENT,
currency_code CHAR(6) NOT NULL,
currency_chinese NVARCHAR(20) NOT NULL,
create_date DATETIME NOT NULL,
create_by NVARCHAR(20) NOT NULL,
update_date DATETIME NULL,
update_by NVARCHAR(20) NULL,
PRIMARY KEY (id)
);

INSERT INTO CurrencySurface ( currency_code,currency_chinese,create_date,create_by)
VALUES 
('USD','美金',CURRENT_TIMESTAMP(),'Ken'),
('GBP','英鎊',CURRENT_TIMESTAMP(),'Ken'),
('EUR','歐元',CURRENT_TIMESTAMP(),'Ken');

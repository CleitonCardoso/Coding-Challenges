
INSERT INTO DEAL (id,create_date, end_date, publish_date, text, title, total_sold, type, url) values
(1, '2018-01-01', '2018-01-01', '2018-01-01', 'Oferta imperdível', 'Frigideira', 52, 'PRODUCT', null );

INSERT INTO DEAL (id,create_date, end_date, publish_date, text, title, total_sold, type, url) values
(2, '2018-01-01', '2018-01-01', '2018-01-01', 'Oferta imperdível', 'Panela', 35, 'PRODUCT', null );

INSERT INTO DEAL (id,create_date, end_date, publish_date, text, title, total_sold, type, url) values
(3, '2018-01-01', '2018-01-01', '2018-01-01', 'Oferta imperdível', 'Vasilha', 12, 'PRODUCT', null );

INSERT INTO BUY_OPTION (id, end_date, normal_price, percentage_discount, quantity_cupom, sale_price, start_date, title, deal_id) values
(1, '2018-01-01', 22.85, 10, 25, 20, '2018-01-01', '1 vz sem juros', 2);

INSERT INTO BUY_OPTION (id, end_date, normal_price, percentage_discount, quantity_cupom, sale_price, start_date, title, deal_id) values
(2, '2018-01-01', 22.85, 10, 25, 20, '2018-01-01', '2 vz sem juros', 2);

INSERT INTO BUY_OPTION (id, end_date, normal_price, percentage_discount, quantity_cupom, sale_price, start_date, title, deal_id) values
(3, '2018-01-01', 22.85, 10, 25, 20, '2018-01-01', '3 vz sem juros', 2);

INSERT INTO BUY_OPTION (id, end_date, normal_price, percentage_discount, quantity_cupom, sale_price, start_date, title, deal_id) values
(4, '2018-01-01', 22.85, 10, 0, 20, '2018-01-01', '4 vz sem juros', 2);
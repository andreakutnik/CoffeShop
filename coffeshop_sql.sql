USE coffee_shop;

CREATE TABLE users (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  first_name TEXT,
  last_name TEXT,
  username VARCHAR(45) UNIQUE,
  password TEXT,
  role ENUM('waiter', 'admin')
);

CREATE TABLE recipes (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  instructions TEXT
);

CREATE TABLE ingredients (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  name TEXT,
  quantity INTEGER
);

CREATE TABLE recipe_ingredients(
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  recipe_id INTEGER REFERENCES recipes(id),
  ingredient_id INTEGER REFERENCES ingredients(id)
);

CREATE TABLE products (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  name TEXT,
  price DECIMAL,
  recipe_id INTEGER REFERENCES recipes(id)
);

CREATE TABLE orders (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  user_id INTEGER REFERENCES users(id),
  payment_method ENUM('cash', 'card'),
  status ENUM('finalized', 'cancelled')
);

CREATE TABLE order_items (
  id INTEGER PRIMARY KEY UNIQUE NOT NULL AUTO_INCREMENT,
  order_id INTEGER REFERENCES orders(id),
  product_id INTEGER REFERENCES products(id),
  quantity INTEGER NOT NULL 
);
 INSERT INTO users (first_name, last_name, username, password, role)
VALUES ('John', 'Doe', 'waiter', 'hope4best', 'waiter'),
		('Jane', 'Doe', 'admin', 'hope4best', 'admin');
        
select * from users

INSERT INTO ingredients (name, quantity)
VALUES ('Espresso', 200),
       ('Steamed milk', 200),
       ('Milk', 200),
       ('Ice', 200),
       ('Syrup', 200),
       ('Foam', 200),
       ('Chocolate', 200),
       ('Foamed milk', 200),
       ('Hot water', 200),
       ('Warm milk', 200),
       ('Whipped cream', 200);
       
       
INSERT INTO recipes (instructions)
VALUES ('To make a flat white, start by brewing a double shot of espresso. Next, steam and froth 1/3 cup of milk until it is hot and has a creamy consistency. Pour the milk over the espresso and serve.'),
       ('To make a mocha, start by brewing a double shot of espresso. Next, mix in 1-2 tablespoons of chocolate syrup or cocoa powder with the espresso. Steam and froth 1 cup of milk, then pour it over the espresso and chocolate mixture. Top with whipped cream, if desired.'),
       ('To make iced coffee, start by brewing a double shot of espresso or a strong cup of coffee. Next, pour the hot coffee over a glass filled with ice. Add 1 cup of cold milk and sweeten to taste with sugar or syrup. You can also add flavored syrups or creamers for extra flavor.'),
       ('To make a cappuccino, start by brewing a double shot of espresso. Next, steam and froth 1 cup of milk until it is hot and has a creamy consistency. Pour the milk over the espresso, leaving about 1 inch of space at the top of the cup for the foam. Top with a dollop of foam and serve.'),
       ('To make an americano, start by brewing a double shot of espresso. Next, add hot water to the espresso until you reach your desired cup size. Serve hot.'),
       ('To make an espresso, start by grinding a small amount of finely ground coffee beans. Next, add the ground coffee to an espresso machine and tamp it down lightly. Place a shot glass under the machine and turn on the machine. The espresso will be ready when the shot glass is full, usually in about 30 seconds.'),
       ('To make a café latte, start by brewing a double shot of espresso. Next, steam and froth 1 cup of milk until it is hot and has a creamy consistency. Pour the milk over the espresso and serve, sweetening to taste with syrup if desired.'),
       ('To make a macchiato, start by brewing a single shot of espresso. Next, steam and froth a small amount of milk until it is hot and has a creamy consistency. Pour the milk over the espresso, leaving a small layer of foam on top. Serve hot.'),
       ('To make a cortado, start by brewing a single shot of espresso. Next, add an equal amount of warm milk to the espresso. The resulting drink should have a 1:1 ratio of espresso to milk and a creamy consistency. Serve hot.');

INSERT INTO recipe_ingredients (recipe_id, ingredient_id)
VALUES (1, 1), (1, 2), (2, 1), (2, 3), (2, 7), (3, 1), (3, 4), (3, 3), (4, 1), (4, 2), (5, 1), (5, 9), (6, 1), (7, 1), (7, 2), (8, 1), (8, 2), (9, 1), (9, 10);

INSERT INTO products (name, price, recipe_id)
VALUES ('Flat White', 4.50, 1),
       ('Mocha', 5.00, 2),
       ('Iced Coffee', 3.50, 3),
       ('Cappuccino', 4.00, 4),
       ('Americano', 3.00, 5),
       ('Espresso', 2.50, 6),
       ('Café Latte', 4.00, 7),
       ('Macchiato', 3.50, 8),
       ('Cortado', 3.00, 9);
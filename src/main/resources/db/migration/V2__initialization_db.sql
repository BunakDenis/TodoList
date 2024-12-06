INSERT INTO note (title, content) VALUES
('Do Androids Dream of Electric Sheep?', 'It was January 2021, and Rick Deckard had a license to kill. Somewhere among the hordes of humans out there, lurked several rogue androids. Deckards assignment--find them and then...retire them. Trouble was, the androids all looked exactly like humans, and they didnt want to be found!'),
('The Hitchhiker’s Guide to the Galaxy', 'Seconds before the Earth is demolished to make way for a galactic freeway, Arthur Dent is plucked off the planet by his friend Ford Prefect, a researcher for the revised edition of The Hitchhikers Guide to the Galaxy who, for the last fifteen years, has been posing as an out-of-work actor.'),
('Something Wicked This Way Comes', 'One of Ray Bradbury’s best-known and most popular novels, Something Wicked This Way Comes, now featuring a new introduction and material about its longstanding influence on culture and genre.'),
('A Story of Yesterday', 'A Story of Yesterday is a concise and straight punch to the jaw of life.Under a sky of different colors germinates a magical story of survival, where the result of each choice, enclosed in this particular tale, will snatch the whereabouts of each story forever.'),
('To Kill a Mockingbird', 'The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it. To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.');

INSERT INTO users (username, password, enabled, role) VALUES
('user', '{noop}jdbcDefault', 'true', 'ADMIN'),
('guess', '{noop}default', 'true', 'USER');
create table Genre 
(
	_id integer primary key autoincrement,
	Name text not null
);

create table Collection
(
	_id integer primary key autoincrement,
	Name text not null
);

create table Movie
(
	_id integer primary key autoincrement,
	Name text not null,
	Description text null,
	ImageName null,
	Price real null,
	GenreId integer not null,
	foreign key(GenreId) references Genre(_id)
);

create table CollectionMovie
(
	_id integer primary key autoincrement,
	CollectionId integer not null,
	MovieId integer not null,
	unique(CollectionId,MovieId),
	foreign key(CollectionId) references Collection(_id),
	foreign key(MovieId) references Movie(_id)
);
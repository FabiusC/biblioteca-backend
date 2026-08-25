create table if not exists users (
   id         bigserial primary key,
   first_name varchar(100) not null,
   last_name  varchar(100) not null,
   email      varchar(150) unique not null,
   birth_date date not null
);

create table if not exists books (
   id               bigserial primary key,
   title            varchar(255) not null,
   isbn             varchar(20) unique not null,
   edition          integer not null,
   publication_date date not null,
   author           varchar(255) not null
);

create table if not exists copies (
   id      bigserial primary key,
   book_id bigint not null,
   status  varchar(50) not null default 'AVAILABLE',
   constraint fk_copies_book foreign key ( book_id )
      references books ( id )
         on delete cascade
);

create table if not exists loans (
   id          bigserial primary key,
   user_id     bigint not null,
   book_id     bigint not null,
   copy_id     bigint not null,
   loan_date   date not null,
   return_date date,
   status      varchar(50) not null,
   constraint fk_loans_user foreign key ( user_id )
      references users ( id )
         on delete cascade,
   constraint fk_loans_book foreign key ( book_id )
      references books ( id )
         on delete cascade,
   constraint fk_loans_copy foreign key ( copy_id )
      references copies ( id )
         on delete cascade
);
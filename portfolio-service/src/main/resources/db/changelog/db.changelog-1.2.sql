
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists profession (
    id uuid default gen_random_uuid() unique not null ,
    name varchar unique not null ,
    description text not null
);

--changeset burgasvv:2
insert into profession(name, description) values ('Java разработчик','Описание профессии Java разработчик');
insert into profession(name, description) values ('Python разработчик','Описание профессии Python разработчик');
insert into profession(name, description) values ('Go разработчик','Описание профессии Go разработчик');
insert into profession(name, description) values ('Дизайнер уровней','Описание профессии Дизайнер уровней');
insert into profession(name, description) values ('UX/UI дизайнер','Описание профессии UX/UI дизайнер');
insert into profession(name, description) values ('Звукоинженер','Описание профессии Звукоинженер');
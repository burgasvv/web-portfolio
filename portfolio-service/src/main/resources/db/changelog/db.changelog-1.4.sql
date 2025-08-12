
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists project (
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    description text not null ,
    portfolio_id uuid references portfolio(id) on delete cascade on update cascade ,
    created_at timestamp not null ,
    updated_at timestamp not null
);

--changeset burgasvv:2
create table if not exists project_image (
    project_id uuid references project(id) on delete cascade on update cascade ,
    image_id uuid references image(id) on delete set null on update cascade ,
    primary key (project_id, image_id)
);

--changeset burgasvv:3
create table if not exists project_video (
    project_id uuid references project(id) on delete cascade on update cascade ,
    video_id uuid references video(id) on delete set null on update cascade ,
    primary key (project_id, video_id)
);

--changeset burgasvv:4
create table if not exists project_document (
    project_id uuid references project(id) on delete cascade on update cascade ,
    document_id uuid references document(id) on delete set null on update cascade ,
    primary key (project_id, document_id)
);
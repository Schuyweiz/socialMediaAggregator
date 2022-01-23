--changelog kpike: created verification tokens table
create table if not exists verification_tokens
(
    id          bigint
        constraint verification_tokens_pk
            primary key,
    token       varchar,
    user_id     bigint,
    expiry_date timestamp
);

alter table verification_tokens
    drop constraint if exists verification_tokens_app_user_id_fk;

alter table verification_tokens
    add constraint verification_tokens_app_user_id_fk
        foreign key (user_id) references app_user;

alter table verification_tokens
    alter column id
        add generated always as identity;

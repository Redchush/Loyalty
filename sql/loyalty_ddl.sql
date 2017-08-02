CREATE TABLE dynamusic_lp_transaction(
      id VARCHAR(32) not null,
      amount VARCHAR(32) not null,
      description VARCHAR(1000) null,
      creation_date TIMESTAMP,
      profile_id VARCHAR(32) not null REFERENCES dps_user(id),
      type INTEGER not NULL,
      primary key(id)
);

CREATE TABLE dynamusic_user_transactions (
        user_id      VARCHAR(32)     not null references dps_user(id),
        transaction_id     VARCHAR(32)  not null references dynamusic_lp_transaction(id),
        sequence_num integer NOT NULL,
        primary key(user_id, transaction_id)
);
commit work;
-- alter table dynamusic_user_transactions add column sequence_num integer ;
-- alter table dynamusic_lp_transaction add column type integer ;
-- UPDATE dynamusic_lp_transaction SET type = 0



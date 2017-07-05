CREATE TABLE dynamusic_lp_transaction(
      id VARCHAR(32) not null,
      amount VARCHAR(32) not null,
      description VARCHAR(32) null,
      creation_date TIMESTAMP,
      profile_id VARCHAR(32) not null REFERENCES dps_user(id),
      primary key(id)
);


CREATE TABLE dynamusic_user_transactions (
        user_id      VARCHAR(32)     not null references dps_user(id),
        transaction_id     VARCHAR(32)  not null references dynamusic_lp_transaction(id),
        primary key(user_id, transaction_id)
);
commit work;

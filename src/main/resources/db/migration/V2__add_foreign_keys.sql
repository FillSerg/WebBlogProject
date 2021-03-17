/*alter table post_comments
    add constraint FKc3b7s6wypcsvua2ycn4o1lv2c foreign key (parent_id) references post_comments (id);
alter table post_comments
    add constraint FKaawaqxjs3br8dw5v90w7uu514 foreign key (post_id) references posts (id);
alter table post_comments
    add constraint FKsnxoecngu89u3fh4wdrgf0f2g foreign key (user_id) references users (id);
alter table post_votes
    add constraint FK9jh5u17tmu1g7xnlxa77ilo3u foreign key (post_id) references posts (id);
alter table post_votes
    add constraint FK9q09ho9p8fmo6rcysnci8rocc foreign key (user_id) references users (id);
alter table posts
    add constraint FK6m7nr3iwh1auer2hk7rd05riw foreign key (moderator_id) references users (id);
alter table posts
    add constraint FK5lidm6cqbc7u4xhqpxm898qme foreign key (user_id) references users (id);
alter table tags2posts
    add constraint FKtranpyif8bkvolnad0po1yjfa foreign key (post_id) references posts (id);
alter table tags2posts
    add constraint FK7iegdu0y5fg48fn6ndb9bu5vk foreign key (tag_id) references tags (id);*/

alter table post_comments
    add constraint fk_parent_child_comments foreign key (parent_id) references post_comments (id);
alter table post_comments
    add constraint fk_post_comments foreign key (post_id) references posts (id);
alter table post_comments
    add constraint fk_users_comments foreign key (user_id) references users (id);
alter table post_votes
    add constraint fk_posts_votes foreign key (post_id) references posts (id);
alter table post_votes
    add constraint fk_users_votes foreign key (user_id) references users (id);
alter table posts
    add constraint fk_moderators foreign key (moderator_id) references users (id);
alter table posts
    add constraint fk_users_posts foreign key (user_id) references users (id);
alter table tags2posts
    add constraint fk_posts_tags2 foreign key (post_id) references posts (id);
alter table tags2posts
    add constraint fk_tags foreign key (tag_id) references tags (id);
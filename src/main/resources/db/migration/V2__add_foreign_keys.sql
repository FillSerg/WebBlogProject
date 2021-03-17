
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
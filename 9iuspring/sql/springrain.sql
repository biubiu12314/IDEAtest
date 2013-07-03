/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013/7/3 22:55:07                            */
/*==============================================================*/


drop table if exists T_USERS;

drop table if exists t_menu;

drop table if exists t_organization;

drop table if exists t_role;

drop table if exists t_subsystem;

drop table if exists t_user_menu;

drop table if exists t_user_org;

drop table if exists t_user_role;

/*==============================================================*/
/* Table: T_USERS                                               */
/*==============================================================*/
create table T_USERS
(
   id                   varchar(40) not null comment '���',
   name                 varchar(30) comment '����',
   code                 varchar(40) comment '����',
   account              varchar(40) comment '�˺�',
   password             varchar(40) comment '����',
   salt                 varchar(60) comment '����У����',
   age                  int comment '����',
   sex                  int comment '0.Ů1.��',
   phone                varchar(16) comment '�绰����',
   mobile               varchar(16) comment '�ֻ�����',
   eamil                varchar(60) comment '����',
   address              varchar(255) comment '��ַ',
   state                int comment '0.��Ч1.��Ч',
   primary key (id)
);

alter table T_USERS comment '�û���';

/*==============================================================*/
/* Table: t_menu                                                */
/*==============================================================*/
create table t_menu
(
   id                   varchar(40) not null,
   name                 varchar(60),
   pid                  varchar(40),
   description          text,
   pageurl              text,
   type                 0.��ͨ��Դ1.�˵���Դ,
   state                int,
   primary key (id)
);

/*==============================================================*/
/* Table: t_organization                                        */
/*==============================================================*/
create table t_organization
(
   id                   varchar(40) not null comment '���',
   name                 varchar(60) comment '����',
   comcode              varchar(40) comment '����',
   pid                  varchar(40) comment '�ϼ�����ID',
   sysid                varchar(40) comment '��ϵͳID',
   type                 int comment '0,��֯���� 1.����',
   leaf                 int comment 'Ҷ�ӽڵ�(0:��֦�ڵ�;1:Ҷ�ӽڵ�)',
   sortno               int comment '�����',
   description          text comment '����',
   state                int comment '0.ʧЧ 1.��Ч',
   primary key (id)
);

alter table t_organization comment '��֯����';

/*==============================================================*/
/* Table: t_role                                                */
/*==============================================================*/
create table t_role
(
   id                   varchar(40) not null comment '��ɫID',
   name                 varchar(60) comment '��ɫ����',
   permissions          varchar(255) comment 'Ȩ��',
   pid                  varchar(40) comment '��������',
   remark               varchar(255) comment '��ע',
   state                int comment '״̬(0:����2:����)',
   primary key (id)
);

alter table t_role comment '��ɫ';

/*==============================================================*/
/* Table: t_subsystem                                           */
/*==============================================================*/
create table t_subsystem
(
   id                   varchar(40) not null comment '��ϵͳ���',
   name                 varchar(60) comment '��ϵͳ����',
   sortno               int comment '��ϵͳ����',
   uri                  text comment '��ϵͳ����·��',
   remark               text comment '��ע',
   state                varchar(2) comment '״̬(0:������1:����)',
   primary key (id)
);

alter table t_subsystem comment '��ϵͳ';

/*==============================================================*/
/* Table: t_user_menu                                           */
/*==============================================================*/
create table t_user_menu
(
   id                   varchar(40) not null comment '���',
   role_id              varchar(40) comment '��ɫ���',
   menu_id              varchar(40) comment '�˵����',
   primary key (id)
);

/*==============================================================*/
/* Table: t_user_org                                            */
/*==============================================================*/
create table t_user_org
(
   id                   varchar(40) not null comment '���',
   user_id              varchar(40) comment '�û����',
   org_id               varchar(40) comment '�������',
   ismanager            int comment '0.����1.��',
   primary key (id)
);

alter table t_user_org comment '�û�������';

/*==============================================================*/
/* Table: t_user_role                                           */
/*==============================================================*/
create table t_user_role
(
   id                   varchar(40) not null comment '���',
   user_id              varchar(40) comment '�û����',
   role_id              varchar(40) comment '��ɫ���',
   primary key (id)
);


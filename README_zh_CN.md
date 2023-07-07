
新建表
DROP TABLE IF EXISTS "public"."t_ds_workflow_catalog";
CREATE TABLE "public"."t_ds_workflow_catalog" (
"id" int4 DEFAULT nextval('t_ds_workflow_catalog_id_sequence'::regclass),
"name" varchar(255) COLLATE "pg_catalog"."default",
"parent_id" int4,
"ancestors" varchar(255) COLLATE "pg_catalog"."default",
"type" int4,
"create_by" varchar(255) COLLATE "pg_catalog"."default",
"create_time" timestamp(6),
"update_by" varchar(255) COLLATE "pg_catalog"."default",
"update_time" timestamp(6),
"order_num" int4
)
;
ALTER TABLE "public"."t_ds_workflow_catalog" OWNER TO "root";
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."id" IS '主键';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."name" IS '目录名称';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."parent_id" IS '父ID';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."ancestors" IS '祖级列表';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."type" IS '类型：1：工作流定义 2：数据流定义 3：数据集';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."create_by" IS '创建人';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."update_by" IS '更新时间';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."t_ds_workflow_catalog"."order_num" IS '排序';
COMMENT ON TABLE "public"."t_ds_workflow_catalog" IS '目录表，工作流目录、数据流目录、数据集目录';

DROP SEQUENCE IF EXISTS t_ds_workflow_catalog_id_sequence;
CREATE SEQUENCE  t_ds_workflow_catalog_id_sequence;
ALTER TABLE t_ds_workflow_catalog ALTER COLUMN id SET DEFAULT NEXTVAL('t_ds_workflow_catalog_id_sequence');
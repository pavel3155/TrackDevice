PGDMP                         |            trackdevice    9.6.24    9.6.24 >    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            �           1262    20246    trackdevice    DATABASE     �   CREATE DATABASE trackdevice WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Russian_Russia.1251' LC_CTYPE = 'Russian_Russia.1251';
    DROP DATABASE trackdevice;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12387    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            �           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1259    20459    csa    TABLE     �   CREATE TABLE public.csa (
    id bigint NOT NULL,
    num character varying(255),
    address character varying(255),
    code character varying(255)
);
    DROP TABLE public.csa;
       public         postgres    false    3            �            1259    20564    device    TABLE     �   CREATE TABLE public.device (
    id bigint NOT NULL,
    model_id bigint DEFAULT 1,
    invnum character varying(255),
    sernum character varying(255),
    status character varying(255)
);
    DROP TABLE public.device;
       public         postgres    false    3            �            1259    20562    device_id_seq    SEQUENCE     v   CREATE SEQUENCE public.device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.device_id_seq;
       public       postgres    false    3    198            �           0    0    device_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.device_id_seq OWNED BY public.device.id;
            public       postgres    false    197            �            1259    20550    model_device    TABLE     |   CREATE TABLE public.model_device (
    id bigint NOT NULL,
    type_id bigint DEFAULT 1,
    name character varying(255)
);
     DROP TABLE public.model_device;
       public         postgres    false    3            �            1259    20548    model_device_id_seq    SEQUENCE     |   CREATE SEQUENCE public.model_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.model_device_id_seq;
       public       postgres    false    3    196            �           0    0    model_device_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.model_device_id_seq OWNED BY public.model_device.id;
            public       postgres    false    195            �            1259    20467    orders    TABLE     �   CREATE TABLE public.orders (
    id bigint NOT NULL,
    date character varying(255),
    num character varying(255),
    compl_id bigint DEFAULT 1,
    dev_id bigint DEFAULT 1,
    description character varying(255),
    status character varying(255)
);
    DROP TABLE public.orders;
       public         postgres    false    3            �            1259    20465    orders_id_seq    SEQUENCE     v   CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.orders_id_seq;
       public       postgres    false    3    192            �           0    0    orders_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;
            public       postgres    false    191            �            1259    20277    roles    TABLE     x   CREATE TABLE public.roles (
    id bigint NOT NULL,
    role character varying(255),
    type character varying(255)
);
    DROP TABLE public.roles;
       public         postgres    false    3            �            1259    20275    roles_id_seq    SEQUENCE     u   CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.roles_id_seq;
       public       postgres    false    186    3            �           0    0    roles_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;
            public       postgres    false    185            �            1259    20542    type_device    TABLE     ]   CREATE TABLE public.type_device (
    id bigint NOT NULL,
    type character varying(255)
);
    DROP TABLE public.type_device;
       public         postgres    false    3            �            1259    20540    type_device_id_seq    SEQUENCE     {   CREATE SEQUENCE public.type_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.type_device_id_seq;
       public       postgres    false    194    3            �           0    0    type_device_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.type_device_id_seq OWNED BY public.type_device.id;
            public       postgres    false    193            �            1259    20327    users    TABLE     �   CREATE TABLE public.users (
    id bigint NOT NULL,
    name character varying(255),
    surname character varying(255),
    email character varying(255),
    password character varying(255),
    role_id bigint DEFAULT 1
);
    DROP TABLE public.users;
       public         postgres    false    3            �            1259    20325    users_id_seq    SEQUENCE     u   CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public       postgres    false    3    188            �           0    0    users_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
            public       postgres    false    187            �            1259    20457    Сsa_id_seq    SEQUENCE     v   CREATE SEQUENCE public."Сsa_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public."Сsa_id_seq";
       public       postgres    false    3    190            �           0    0    Сsa_id_seq    SEQUENCE OWNED BY     <   ALTER SEQUENCE public."Сsa_id_seq" OWNED BY public.csa.id;
            public       postgres    false    189            �           2604    20586    csa id    DEFAULT     c   ALTER TABLE ONLY public.csa ALTER COLUMN id SET DEFAULT nextval('public."Сsa_id_seq"'::regclass);
 5   ALTER TABLE public.csa ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    189    190    190                       2604    20601 	   device id    DEFAULT     f   ALTER TABLE ONLY public.device ALTER COLUMN id SET DEFAULT nextval('public.device_id_seq'::regclass);
 8   ALTER TABLE public.device ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    198    197    198                       2604    20629    model_device id    DEFAULT     r   ALTER TABLE ONLY public.model_device ALTER COLUMN id SET DEFAULT nextval('public.model_device_id_seq'::regclass);
 >   ALTER TABLE public.model_device ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    196    195    196            �           2604    20500 	   orders id    DEFAULT     f   ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);
 8   ALTER TABLE public.orders ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    191    192    192            �           2604    20297    roles id    DEFAULT     d   ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);
 7   ALTER TABLE public.roles ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    186    185    186                       2604    20651    type_device id    DEFAULT     p   ALTER TABLE ONLY public.type_device ALTER COLUMN id SET DEFAULT nextval('public.type_device_id_seq'::regclass);
 =   ALTER TABLE public.type_device ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    194    193    194            �           2604    20339    users id    DEFAULT     d   ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
 7   ALTER TABLE public.users ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    188    187    188            �          0    20459    csa 
   TABLE DATA               5   COPY public.csa (id, num, address, code) FROM stdin;
    public       postgres    false    190   !@       �          0    20564    device 
   TABLE DATA               F   COPY public.device (id, model_id, invnum, sernum, status) FROM stdin;
    public       postgres    false    198   �@       �           0    0    device_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.device_id_seq', 17, true);
            public       postgres    false    197            �          0    20550    model_device 
   TABLE DATA               9   COPY public.model_device (id, type_id, name) FROM stdin;
    public       postgres    false    196   �A       �           0    0    model_device_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.model_device_id_seq', 12, true);
            public       postgres    false    195            �          0    20467    orders 
   TABLE DATA               V   COPY public.orders (id, date, num, compl_id, dev_id, description, status) FROM stdin;
    public       postgres    false    192   �B       �           0    0    orders_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.orders_id_seq', 8, true);
            public       postgres    false    191            �          0    20277    roles 
   TABLE DATA               /   COPY public.roles (id, role, type) FROM stdin;
    public       postgres    false    186   �C       �           0    0    roles_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.roles_id_seq', 1, false);
            public       postgres    false    185            �          0    20542    type_device 
   TABLE DATA               /   COPY public.type_device (id, type) FROM stdin;
    public       postgres    false    194   �C       �           0    0    type_device_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.type_device_id_seq', 8, true);
            public       postgres    false    193            �          0    20327    users 
   TABLE DATA               L   COPY public.users (id, name, surname, email, password, role_id) FROM stdin;
    public       postgres    false    188   �D       �           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 6, true);
            public       postgres    false    187            �           0    0    Сsa_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public."Сsa_id_seq"', 5, true);
            public       postgres    false    189                       2606    20603    device device_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.device
    ADD CONSTRAINT device_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.device DROP CONSTRAINT device_pkey;
       public         postgres    false    198    198                       2606    20631    model_device model_device_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.model_device
    ADD CONSTRAINT model_device_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.model_device DROP CONSTRAINT model_device_pkey;
       public         postgres    false    196    196                       2606    20502    orders orders_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.orders DROP CONSTRAINT orders_pkey;
       public         postgres    false    192    192                       2606    20299    roles roles_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.roles DROP CONSTRAINT roles_pkey;
       public         postgres    false    186    186                       2606    20653    type_device type_device_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.type_device
    ADD CONSTRAINT type_device_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.type_device DROP CONSTRAINT type_device_pkey;
       public         postgres    false    194    194            	           2606    20341    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public         postgres    false    188    188                       2606    20588    csa Сsa_pkey 
   CONSTRAINT     M   ALTER TABLE ONLY public.csa
    ADD CONSTRAINT "Сsa_pkey" PRIMARY KEY (id);
 9   ALTER TABLE ONLY public.csa DROP CONSTRAINT "Сsa_pkey";
       public         postgres    false    190    190                       2606    20589    orders fk_compl    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_compl FOREIGN KEY (compl_id) REFERENCES public.csa(id) ON UPDATE CASCADE ON DELETE SET DEFAULT;
 9   ALTER TABLE ONLY public.orders DROP CONSTRAINT fk_compl;
       public       postgres    false    190    192    2059                       2606    20604    orders fk_dev    FK CONSTRAINT     �   ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk_dev FOREIGN KEY (dev_id) REFERENCES public.device(id) ON UPDATE CASCADE ON DELETE SET DEFAULT;
 7   ALTER TABLE ONLY public.orders DROP CONSTRAINT fk_dev;
       public       postgres    false    2067    192    198                       2606    20632    device fk_model_device    FK CONSTRAINT     �   ALTER TABLE ONLY public.device
    ADD CONSTRAINT fk_model_device FOREIGN KEY (model_id) REFERENCES public.model_device(id) ON UPDATE CASCADE ON DELETE SET DEFAULT;
 @   ALTER TABLE ONLY public.device DROP CONSTRAINT fk_model_device;
       public       postgres    false    2065    198    196                       2606    20350    users fk_role    FK CONSTRAINT     �   ALTER TABLE ONLY public.users
    ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES public.roles(id) ON UPDATE CASCADE ON DELETE SET DEFAULT;
 7   ALTER TABLE ONLY public.users DROP CONSTRAINT fk_role;
       public       postgres    false    2055    186    188                       2606    20654    model_device fk_type_model    FK CONSTRAINT     �   ALTER TABLE ONLY public.model_device
    ADD CONSTRAINT fk_type_model FOREIGN KEY (type_id) REFERENCES public.type_device(id) ON UPDATE CASCADE ON DELETE SET DEFAULT;
 D   ALTER TABLE ONLY public.model_device DROP CONSTRAINT fk_type_model;
       public       postgres    false    194    2063    196            �   �   x�m�=�@��=� 6�ê���� �cca��ޠ�1����]-�b����T�$�{XI���&Rڤvs��1j�#-QŜ��hqG�w���Oh�6N`z`�g�N���8	�Q�FE9n�Lj=cF��a�<��c��
*q��ʗ�)o�HM�RJ�\�7���4G�i��l��L0ƞ䘂�      �   �   x�u�=ND1�ڹ�C��kق���j.���@{���[�v�%E�/�$�@�m�[��]3t���`�������8�_�����񕜵�.+���=V:���$�u&�LRY�u��J��t�u�� ���27��� s'�Lcė���3��!3�0�!�V��/���}B�����(��<��O�z�K�p���/��p)W����"j�;�s����&��?F��      �   �   x�u�AN�@E��)|��{2!]6I��6"4�`�fR�*�$��pNTnD 	�a���m����y
d�cR�=�E��|����$!�x����h(��;*�M�[��-mD����V��D��r���ᑡB�(<�?��9Pz%j�顷�}��w�h[��ThEK_��h�@ ���g{���S[;G���
�W��uu{^w�?r_��m���ô�)����刢kf�ⅷ.�B�e|A,��f���t�R_      �   �   x���M�@�םSp�t~��1< �qa"WA#J0��Y6�������k�κ��Km�.Xk8�;u	�h�xG-u\s�Ѓk�作��Q!<`\N	��F)~��唨��ю����k�Wj[�m��7�ܬ5��-EU�g�.�B=׉(-=�c#B'�~�8���p5b��"���4$����+c�e3��      �   2   x�3���q�wu��\F��`� N�ept�����\1z\\\ յ�      �   {   x�%���@D�v4�H�)�b��ĉ[�" R�Ijx�/9�53���t.\ȶ�DKGo'�|��B����Hu)T�itU>7��.��"�ru#4��ҿؖ�ݢ]/3/�#n7U�	�N�      �   �  x�e��n�@F����zl.�.�CB�1��8�s7۬��F�#DQ#Ej�g��Q!R7�������#�!Ȳ��U%�+�g�>3�K�ǯ�r�qאo�Ι1���41�!�:5o챭�w"g�ɮ �c �I��3}�����/}���W�g�
��P�lEk�X�«��G1�5�^4�F�a�\��B\yirf����.���������O[}�" ��e$q�=�Rm:?��u�5:�v�~
��[�U�p/mvdp���a�	�k���t��X^a����ɡ\
�ls�#�Z�7L@����Hܭ2�6j���d��g�ih�<l��n��"o�i��O��?i�������$i�a�V��ʭqn�ڍ@��3n4I�ׅa:h�(�z�L`R��j���$�c�O��>���d�S��^���2     
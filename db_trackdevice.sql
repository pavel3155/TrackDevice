PGDMP      '                    |            trackdevice    9.6.24    9.6.24 >    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
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
    public       postgres    false    194   D       �           0    0    type_device_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.type_device_id_seq', 8, true);
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
       public       postgres    false    194    2063    196            �   �   x^m�1�0й9E�DI� =+�)��B,!�Q�-B�W��	)b�=X~�։�[�&��T�h�Y7M9����ę֨SN+{tx��}���Vh�ɼ�����hc#���q�آF��
<<����D��a�BP�q@O%U�RA���u0\�gs��Ji3�p�i�~|���b�{䘂�      �   �   x^u�=N1�ڹ�"?��kق���j.���@\!s#2ѠuaF�"�'��ˀ@����MBB������"�f9���^���2�������WR���i�Z�Z-��ks:LR��N�I����%i^W�[�;��`�6��A�����W��C<_%<��|�x��F3�f�F햛�-̙.�	��-
C�����:?	��ּ��{ ��o�W� CD�j�;�k����&��?F��      �   �   x^u�KN�@D�ݧ���t��8K;$&N6#� a��� �+p�p#!!6캺��W�础@^R�=��ۑ?Q�i*֨�1(OS��w>}�>Ѐ��>�C�@ձ������(��?�Q=HᱡX$ȯ�����"F�����5ힲ�Pc8@D����~��6�lF�V��5���	����G���t�s4`�4e����w.�k�˦?�p{�"3��	Ŷ�y��Q|ˬT2�V�@ɼJ���0ލ�t�R_      �   �   x^��A�@EםSp̴3 ޅcx `��D��F�`�+�7���1�V�i����	ȒO�R�:D��Z��>�������^��~IãTrԷ3n� F���St�t�.R\����,Rtp��������1�cyܖ�}���6:�u�v�֫�]�iM����j����9kyO����JW��ي���ٖc�e3��      �   2   x^3���q�wu��\F��`� N�ept�����\1z\\\ յ�      �   |   x^%�K
�@D�ݧ�#D��0f���+w.� �T?�^��\TSU]�BB�n$;X����N�<�0�ֵpu3��f���p�L�����t��E���Nh=$'9ы*��q�L�����RU	�N�      �   �  x^e��n�@F����zl.�.�CB�1��8�s7۬��F�#DQ#Ej�g��Q!R7��۝���#�!Ȳ�۫((J�W�Ϻ}fؗB�_�n�!ߜ�3ch�uib<�Bfuj��c[�D���]�� ��>�g�B������_�>�W�]��f+Z��"�^�G�<�a���Y6����ڏ�ȳH�3S= ��v������ }l�Z�['./%�����j���w�+�ѱ����P(�ފ�J�{i�#�s����H�^�����%t��
��/N�R�gsx��!��a<�L�D�n�y��`�Pk�|���Q�3��4y6HH�SO�7��4�اJ�4o�j��\�_�4��PF`��8�i�F ���7��	�$���0�^��u=^&0�ND5n�qɱ�'�`��\C���_a�����2     
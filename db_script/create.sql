--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.11
-- Dumped by pg_dump version 11.1

-- Started on 2019-03-21 17:46:03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE ga_dev;
--
-- TOC entry 3204 (class 1262 OID 16391)
-- Name: ga_dev; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE ga_dev WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect ga_dev

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 189 (class 1259 OID 16489)
-- Name: buildings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.buildings (
    building_id integer NOT NULL,
    name text NOT NULL,
    location point
);


--
-- TOC entry 3205 (class 0 OID 0)
-- Dependencies: 189
-- Name: COLUMN buildings.location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.buildings.location IS 'Location on the NUIG map';


--
-- TOC entry 197 (class 1259 OID 16661)
-- Name: building_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.building_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3206 (class 0 OID 0)
-- Dependencies: 197
-- Name: building_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.building_id_sequence OWNED BY public.buildings.building_id;


--
-- TOC entry 195 (class 1259 OID 16622)
-- Name: courses; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.courses (
    course_id integer NOT NULL,
    name text NOT NULL,
    department_id integer,
    numenrolled integer NOT NULL
);


--
-- TOC entry 3207 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN courses.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.courses.name IS 'Course name and year, e.g. 3BCT or 2BA';


--
-- TOC entry 3208 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN courses.numenrolled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.courses.numenrolled IS 'The number of students enrolled in this course.';


--
-- TOC entry 198 (class 1259 OID 16664)
-- Name: course_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.course_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3209 (class 0 OID 0)
-- Dependencies: 198
-- Name: course_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.course_id_sequence OWNED BY public.courses.course_id;


--
-- TOC entry 196 (class 1259 OID 16643)
-- Name: course_module; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.course_module (
    course_id integer NOT NULL,
    module_id integer NOT NULL,
    code text
);


--
-- TOC entry 3210 (class 0 OID 0)
-- Dependencies: 196
-- Name: COLUMN course_module.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.course_module.code IS 'The catalog code for this module, e.g. CS402, MA492. When the same module is taught for groups of students belonging to different departments/courses, this means there should be two different records in this table';


--
-- TOC entry 192 (class 1259 OID 16529)
-- Name: department_building; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.department_building (
    department_id integer NOT NULL,
    building_id integer NOT NULL,
    score integer
);


--
-- TOC entry 3211 (class 0 OID 0)
-- Dependencies: 192
-- Name: COLUMN department_building.score; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.department_building.score IS 'Score which indicates how much preferences this department has for utilising this building.';


--
-- TOC entry 185 (class 1259 OID 16422)
-- Name: departments; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.departments (
    department_id integer NOT NULL,
    name text NOT NULL
);


--
-- TOC entry 199 (class 1259 OID 16668)
-- Name: department_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.department_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3212 (class 0 OID 0)
-- Dependencies: 199
-- Name: department_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.department_id_sequence OWNED BY public.departments.department_id;


--
-- TOC entry 208 (class 1259 OID 16720)
-- Name: jobs; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.jobs (
    job_id integer NOT NULL,
    start_date timestamp without time zone,
    total_generations integer,
    current_generation integer
);


--
-- TOC entry 207 (class 1259 OID 16718)
-- Name: job_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3213 (class 0 OID 0)
-- Dependencies: 207
-- Name: job_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.job_id_seq OWNED BY public.jobs.job_id;


--
-- TOC entry 186 (class 1259 OID 16446)
-- Name: lecturers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lecturers (
    lecturer_id integer NOT NULL,
    name text,
    department_id integer
);


--
-- TOC entry 200 (class 1259 OID 16671)
-- Name: lecturer_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.lecturer_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3214 (class 0 OID 0)
-- Dependencies: 200
-- Name: lecturer_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.lecturer_id_sequence OWNED BY public.lecturers.lecturer_id;


--
-- TOC entry 194 (class 1259 OID 16562)
-- Name: lecturer_timeslot_preferences; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lecturer_timeslot_preferences (
    lecturer_id integer NOT NULL,
    timeslot_id integer NOT NULL,
    rank integer
);


--
-- TOC entry 187 (class 1259 OID 16459)
-- Name: modules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.modules (
    module_id integer NOT NULL,
    name text NOT NULL,
    lecturer_id integer
);


--
-- TOC entry 201 (class 1259 OID 16674)
-- Name: module_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.module_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3215 (class 0 OID 0)
-- Dependencies: 201
-- Name: module_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.module_id_sequence OWNED BY public.modules.module_id;


--
-- TOC entry 191 (class 1259 OID 16519)
-- Name: schedules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.schedules (
    schedule_id integer NOT NULL,
    creation_date timestamp without time zone,
    creator_id integer,
    is_wip boolean,
    is_accepted boolean,
    is_genetic_algorithm_running boolean,
    is_master boolean,
    job_id integer
);


--
-- TOC entry 3216 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_wip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_wip IS 'User''s schedule which they are currently working with, but have not yet tagged as their accepted schedule: work in progress (WIP)';


--
-- TOC entry 3217 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_accepted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_accepted IS 'User''s schedule which is the current accepted schedule for their department. The schedule for which they would like to submit to the admin as a modification to the master schedule.';


--
-- TOC entry 3218 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_genetic_algorithm_running; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_genetic_algorithm_running IS 'Currently is being processed by the GA subsystem';


--
-- TOC entry 3219 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_master; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_master IS 'The admin''s master schedule. If the semester starts now, this schedule will be the one published for students. All deptartments/users base their modified schedules upon the master schedule.';


--
-- TOC entry 202 (class 1259 OID 16677)
-- Name: schedule_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.schedule_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3220 (class 0 OID 0)
-- Dependencies: 202
-- Name: schedule_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.schedule_id_sequence OWNED BY public.schedules.schedule_id;


--
-- TOC entry 206 (class 1259 OID 16689)
-- Name: scheduled_modules; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.scheduled_modules (
    schedule_id integer NOT NULL,
    module_id integer NOT NULL,
    timeslot_id integer NOT NULL,
    venue_id integer NOT NULL
);


--
-- TOC entry 193 (class 1259 OID 16544)
-- Name: timeslots; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.timeslots (
    timeslot_id integer NOT NULL,
    day integer NOT NULL,
    "time" integer NOT NULL
);


--
-- TOC entry 3221 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN timeslots.day; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.timeslots.day IS '0,1,2,3,4,5,6';


--
-- TOC entry 3222 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN timeslots."time"; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.timeslots."time" IS '8,9,10,11,12,13,14,15,16,17,18 (most common)';


--
-- TOC entry 203 (class 1259 OID 16680)
-- Name: timeslot_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.timeslot_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3223 (class 0 OID 0)
-- Dependencies: 203
-- Name: timeslot_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.timeslot_id_sequence OWNED BY public.timeslots.timeslot_id;


--
-- TOC entry 188 (class 1259 OID 16477)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username text NOT NULL,
    password character varying(256) NOT NULL,
    password_salt character varying(256) NOT NULL,
    display_name text NOT NULL,
    department_id integer,
    is_facilities boolean DEFAULT false,
    is_admin boolean DEFAULT false,
    email text
);


--
-- TOC entry 204 (class 1259 OID 16683)
-- Name: user_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3224 (class 0 OID 0)
-- Dependencies: 204
-- Name: user_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_id_sequence OWNED BY public.users.user_id;


--
-- TOC entry 190 (class 1259 OID 16497)
-- Name: venues; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.venues (
    venue_id integer NOT NULL,
    name text NOT NULL,
    building_id integer,
    is_lab boolean DEFAULT false,
    capacity integer NOT NULL
);


--
-- TOC entry 205 (class 1259 OID 16686)
-- Name: venue_id_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.venue_id_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3225 (class 0 OID 0)
-- Dependencies: 205
-- Name: venue_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.venue_id_sequence OWNED BY public.venues.venue_id;


--
-- TOC entry 3004 (class 2604 OID 16663)
-- Name: buildings building_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buildings ALTER COLUMN building_id SET DEFAULT nextval('public.building_id_sequence'::regclass);


--
-- TOC entry 3009 (class 2604 OID 16666)
-- Name: courses course_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses ALTER COLUMN course_id SET DEFAULT nextval('public.course_id_sequence'::regclass);


--
-- TOC entry 2998 (class 2604 OID 16670)
-- Name: departments department_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments ALTER COLUMN department_id SET DEFAULT nextval('public.department_id_sequence'::regclass);


--
-- TOC entry 3010 (class 2604 OID 16723)
-- Name: jobs job_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.jobs ALTER COLUMN job_id SET DEFAULT nextval('public.job_id_seq'::regclass);


--
-- TOC entry 2999 (class 2604 OID 16673)
-- Name: lecturers lecturer_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers ALTER COLUMN lecturer_id SET DEFAULT nextval('public.lecturer_id_sequence'::regclass);


--
-- TOC entry 3000 (class 2604 OID 16676)
-- Name: modules module_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modules ALTER COLUMN module_id SET DEFAULT nextval('public.module_id_sequence'::regclass);


--
-- TOC entry 3007 (class 2604 OID 16679)
-- Name: schedules schedule_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules ALTER COLUMN schedule_id SET DEFAULT nextval('public.schedule_id_sequence'::regclass);


--
-- TOC entry 3008 (class 2604 OID 16682)
-- Name: timeslots timeslot_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timeslots ALTER COLUMN timeslot_id SET DEFAULT nextval('public.timeslot_id_sequence'::regclass);


--
-- TOC entry 3003 (class 2604 OID 16685)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.user_id_sequence'::regclass);


--
-- TOC entry 3006 (class 2604 OID 16688)
-- Name: venues venue_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues ALTER COLUMN venue_id SET DEFAULT nextval('public.venue_id_sequence'::regclass);


--
-- TOC entry 3179 (class 0 OID 16489)
-- Dependencies: 189
-- Data for Name: buildings; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.buildings VALUES (1, 'IT Building', '(53.2801729999999978,-9.05864200000000075)');
INSERT INTO public.buildings VALUES (2, 'Arts and Sciences Building', '(53.2798129999999972,-9.06029200000000046)');
INSERT INTO public.buildings VALUES (3, 'Arts Millenium Building', '(53.2797339999999977,-9.06208299999999944)');
INSERT INTO public.buildings VALUES (4, 'Alice Perry Engineering Building', '(53.2836459999999974,-9.0638349999999992)');
INSERT INTO public.buildings VALUES (5, 'Áras Ui Chathail', '(53.2788719999999998,-9.05846000000000018)');


--
-- TOC entry 3186 (class 0 OID 16643)
-- Dependencies: 196
-- Data for Name: course_module; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.course_module VALUES (1, 1, 'CT100');
INSERT INTO public.course_module VALUES (2, 2, 'CT200');
INSERT INTO public.course_module VALUES (3, 3, 'CT300');
INSERT INTO public.course_module VALUES (42, 4, 'LW50');
INSERT INTO public.course_module VALUES (46, 5, 'LW300');
INSERT INTO public.course_module VALUES (13, 1, 'CS100');
INSERT INTO public.course_module VALUES (15, 2, 'CS200');
INSERT INTO public.course_module VALUES (16, 3, 'CS300');
INSERT INTO public.course_module VALUES (1, 6, 'CT15');
INSERT INTO public.course_module VALUES (3, 6, 'CT17');
INSERT INTO public.course_module VALUES (4, 6, 'CT18');
INSERT INTO public.course_module VALUES (15, 6, 'CS16');
INSERT INTO public.course_module VALUES (16, 6, 'CT17');
INSERT INTO public.course_module VALUES (38, 6, 'CT18');
INSERT INTO public.course_module VALUES (42, 6, 'LW15');
INSERT INTO public.course_module VALUES (46, 6, 'LW16');
INSERT INTO public.course_module VALUES (49, 6, 'LW18');
INSERT INTO public.course_module VALUES (51, 6, 'LW19');
INSERT INTO public.course_module VALUES (2, 6, 'CT16');
INSERT INTO public.course_module VALUES (13, 6, 'CS15');


--
-- TOC entry 3185 (class 0 OID 16622)
-- Dependencies: 195
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.courses VALUES (1, '1BCT', 1, 100);
INSERT INTO public.courses VALUES (2, '2BCT', 1, 80);
INSERT INTO public.courses VALUES (3, '3BCT', 1, 75);
INSERT INTO public.courses VALUES (4, '4BCT', 1, 70);
INSERT INTO public.courses VALUES (13, '1BA', 1, 50);
INSERT INTO public.courses VALUES (15, '2BA', 1, 30);
INSERT INTO public.courses VALUES (16, '3BA', 1, 25);
INSERT INTO public.courses VALUES (38, '4BA', 1, 20);
INSERT INTO public.courses VALUES (42, '1LAW', 3, 200);
INSERT INTO public.courses VALUES (46, '2LAW', 3, 150);
INSERT INTO public.courses VALUES (49, '3LAW', 3, 130);
INSERT INTO public.courses VALUES (51, '4LAW', 3, 100);


--
-- TOC entry 3182 (class 0 OID 16529)
-- Dependencies: 192
-- Data for Name: department_building; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.department_building VALUES (1, 1, 20);
INSERT INTO public.department_building VALUES (1, 2, 10);
INSERT INTO public.department_building VALUES (1, 3, 5);
INSERT INTO public.department_building VALUES (1, 4, 10);
INSERT INTO public.department_building VALUES (1, 5, 5);
INSERT INTO public.department_building VALUES (3, 3, 10);
INSERT INTO public.department_building VALUES (3, 5, 20);


--
-- TOC entry 3175 (class 0 OID 16422)
-- Dependencies: 185
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.departments VALUES (1, 'Information Technology');
INSERT INTO public.departments VALUES (3, 'Law');


--
-- TOC entry 3198 (class 0 OID 16720)
-- Dependencies: 208
-- Data for Name: jobs; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.jobs VALUES (1, '2019-03-20 22:30:25', 500, 100);
INSERT INTO public.jobs VALUES (25, '2019-03-21 17:27:06.946', NULL, NULL);


--
-- TOC entry 3184 (class 0 OID 16562)
-- Dependencies: 194
-- Data for Name: lecturer_timeslot_preferences; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3176 (class 0 OID 16446)
-- Dependencies: 186
-- Data for Name: lecturers; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.lecturers VALUES (1, 'Finlay Smith', 1);
INSERT INTO public.lecturers VALUES (2, 'Des Chambers', 1);
INSERT INTO public.lecturers VALUES (3, 'Johnny Law', 3);


--
-- TOC entry 3177 (class 0 OID 16459)
-- Dependencies: 187
-- Data for Name: modules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.modules VALUES (2, 'Data Structures', 2);
INSERT INTO public.modules VALUES (3, 'Enterprise Programming', 1);
INSERT INTO public.modules VALUES (5, 'Advanced Law', 3);
INSERT INTO public.modules VALUES (4, 'Intro Law', 3);
INSERT INTO public.modules VALUES (6, 'Writing Lab', 3);
INSERT INTO public.modules VALUES (1, 'Introduction to Programming', 1);


--
-- TOC entry 3196 (class 0 OID 16689)
-- Dependencies: 206
-- Data for Name: scheduled_modules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.scheduled_modules VALUES (1, 1, 3, 1);


--
-- TOC entry 3181 (class 0 OID 16519)
-- Dependencies: 191
-- Data for Name: schedules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.schedules VALUES (1, '2019-03-18 19:50:52.812', 3, false, false, false, true, NULL);
INSERT INTO public.schedules VALUES (4, '2019-03-18 20:30:50', 12287, false, false, false, false, NULL);
INSERT INTO public.schedules VALUES (2, '2019-03-18 19:49:58.55', 3, false, false, false, false, NULL);
INSERT INTO public.schedules VALUES (5, '2019-03-20 23:50:09.723', 3, false, false, false, false, NULL);
INSERT INTO public.schedules VALUES (3, '2019-03-20 23:47:53.236', 3, false, false, false, false, 25);


--
-- TOC entry 3183 (class 0 OID 16544)
-- Dependencies: 193
-- Data for Name: timeslots; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.timeslots VALUES (1, 0, 8);
INSERT INTO public.timeslots VALUES (2, 0, 9);
INSERT INTO public.timeslots VALUES (3, 0, 10);
INSERT INTO public.timeslots VALUES (4, 0, 11);
INSERT INTO public.timeslots VALUES (5, 0, 12);
INSERT INTO public.timeslots VALUES (6, 0, 13);
INSERT INTO public.timeslots VALUES (7, 0, 14);
INSERT INTO public.timeslots VALUES (8, 0, 15);
INSERT INTO public.timeslots VALUES (9, 0, 16);
INSERT INTO public.timeslots VALUES (10, 0, 17);
INSERT INTO public.timeslots VALUES (11, 0, 18);
INSERT INTO public.timeslots VALUES (12, 1, 8);
INSERT INTO public.timeslots VALUES (13, 1, 9);
INSERT INTO public.timeslots VALUES (14, 1, 10);
INSERT INTO public.timeslots VALUES (15, 1, 11);
INSERT INTO public.timeslots VALUES (16, 1, 12);
INSERT INTO public.timeslots VALUES (17, 1, 13);
INSERT INTO public.timeslots VALUES (18, 1, 14);
INSERT INTO public.timeslots VALUES (19, 1, 15);
INSERT INTO public.timeslots VALUES (20, 1, 16);
INSERT INTO public.timeslots VALUES (21, 1, 17);
INSERT INTO public.timeslots VALUES (22, 1, 18);
INSERT INTO public.timeslots VALUES (23, 2, 8);
INSERT INTO public.timeslots VALUES (24, 2, 9);
INSERT INTO public.timeslots VALUES (25, 2, 10);
INSERT INTO public.timeslots VALUES (26, 2, 11);
INSERT INTO public.timeslots VALUES (27, 2, 12);
INSERT INTO public.timeslots VALUES (28, 2, 13);
INSERT INTO public.timeslots VALUES (29, 2, 14);
INSERT INTO public.timeslots VALUES (30, 2, 15);
INSERT INTO public.timeslots VALUES (31, 2, 16);
INSERT INTO public.timeslots VALUES (32, 2, 17);
INSERT INTO public.timeslots VALUES (33, 2, 18);
INSERT INTO public.timeslots VALUES (34, 3, 8);
INSERT INTO public.timeslots VALUES (35, 3, 9);
INSERT INTO public.timeslots VALUES (36, 3, 10);
INSERT INTO public.timeslots VALUES (37, 3, 11);
INSERT INTO public.timeslots VALUES (38, 3, 12);
INSERT INTO public.timeslots VALUES (39, 3, 13);
INSERT INTO public.timeslots VALUES (40, 3, 14);
INSERT INTO public.timeslots VALUES (41, 3, 15);
INSERT INTO public.timeslots VALUES (42, 3, 16);
INSERT INTO public.timeslots VALUES (43, 3, 17);
INSERT INTO public.timeslots VALUES (44, 3, 18);
INSERT INTO public.timeslots VALUES (45, 4, 8);
INSERT INTO public.timeslots VALUES (46, 4, 9);
INSERT INTO public.timeslots VALUES (47, 4, 10);
INSERT INTO public.timeslots VALUES (48, 4, 11);
INSERT INTO public.timeslots VALUES (49, 4, 12);
INSERT INTO public.timeslots VALUES (50, 4, 13);
INSERT INTO public.timeslots VALUES (51, 4, 14);
INSERT INTO public.timeslots VALUES (52, 4, 15);
INSERT INTO public.timeslots VALUES (53, 4, 16);
INSERT INTO public.timeslots VALUES (54, 4, 17);
INSERT INTO public.timeslots VALUES (55, 4, 18);


--
-- TOC entry 3178 (class 0 OID 16477)
-- Dependencies: 188
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES (12287, 'admin', '123', '123', 'Administrator', 1, true, true, 'andrew@andreweast.net');
INSERT INTO public.users VALUES (3, 'it', 'NOT_HASHED_PASSWORD', 'salty', 'IT Dept Admin', 1, false, false, 'andrew@andreweast.net');


--
-- TOC entry 3180 (class 0 OID 16497)
-- Dependencies: 190
-- Data for Name: venues; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.venues VALUES (1, 'IT250', 1, false, 250);
INSERT INTO public.venues VALUES (2, 'IT125', 1, false, 125);
INSERT INTO public.venues VALUES (3, 'IT102', 1, true, 50);
INSERT INTO public.venues VALUES (4, 'AC201', 2, false, 100);


--
-- TOC entry 3226 (class 0 OID 0)
-- Dependencies: 197
-- Name: building_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.building_id_sequence', 7, true);


--
-- TOC entry 3227 (class 0 OID 0)
-- Dependencies: 198
-- Name: course_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.course_id_sequence', 51, true);


--
-- TOC entry 3228 (class 0 OID 0)
-- Dependencies: 199
-- Name: department_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.department_id_sequence', 3, true);


--
-- TOC entry 3229 (class 0 OID 0)
-- Dependencies: 207
-- Name: job_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.job_id_seq', 25, true);


--
-- TOC entry 3230 (class 0 OID 0)
-- Dependencies: 200
-- Name: lecturer_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.lecturer_id_sequence', 3, true);


--
-- TOC entry 3231 (class 0 OID 0)
-- Dependencies: 201
-- Name: module_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.module_id_sequence', 6, true);


--
-- TOC entry 3232 (class 0 OID 0)
-- Dependencies: 202
-- Name: schedule_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.schedule_id_sequence', 8, true);


--
-- TOC entry 3233 (class 0 OID 0)
-- Dependencies: 203
-- Name: timeslot_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.timeslot_id_sequence', 55, true);


--
-- TOC entry 3234 (class 0 OID 0)
-- Dependencies: 204
-- Name: user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_id_sequence', 5, true);


--
-- TOC entry 3235 (class 0 OID 0)
-- Dependencies: 205
-- Name: venue_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.venue_id_sequence', 4, true);


--
-- TOC entry 3022 (class 2606 OID 16496)
-- Name: buildings buildings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pkey PRIMARY KEY (building_id);


--
-- TOC entry 3036 (class 2606 OID 16650)
-- Name: course_module course_module_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_pk PRIMARY KEY (course_id, module_id);


--
-- TOC entry 3034 (class 2606 OID 16629)
-- Name: courses course_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT course_pkey PRIMARY KEY (course_id);


--
-- TOC entry 3028 (class 2606 OID 16578)
-- Name: department_building department_building_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_pk PRIMARY KEY (department_id, building_id);


--
-- TOC entry 3012 (class 2606 OID 16429)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (department_id);


--
-- TOC entry 3040 (class 2606 OID 16725)
-- Name: jobs job_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.jobs
    ADD CONSTRAINT job_pkey PRIMARY KEY (job_id);


--
-- TOC entry 3032 (class 2606 OID 16566)
-- Name: lecturer_timeslot_preferences lec_time_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lec_time_pk PRIMARY KEY (lecturer_id, timeslot_id);


--
-- TOC entry 3014 (class 2606 OID 16453)
-- Name: lecturers lecturers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers
    ADD CONSTRAINT lecturers_pkey PRIMARY KEY (lecturer_id);


--
-- TOC entry 3016 (class 2606 OID 16466)
-- Name: modules module_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT module_pkey PRIMARY KEY (module_id);


--
-- TOC entry 3038 (class 2606 OID 16693)
-- Name: scheduled_modules scheduled_modules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_pkey PRIMARY KEY (schedule_id, module_id);


--
-- TOC entry 3026 (class 2606 OID 16523)
-- Name: schedules schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_pkey PRIMARY KEY (schedule_id);


--
-- TOC entry 3030 (class 2606 OID 16548)
-- Name: timeslots timeslots_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timeslots
    ADD CONSTRAINT timeslots_pkey PRIMARY KEY (timeslot_id);


--
-- TOC entry 3018 (class 2606 OID 16484)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3020 (class 2606 OID 16486)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3024 (class 2606 OID 16504)
-- Name: venues venues_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues
    ADD CONSTRAINT venues_pkey PRIMARY KEY (venue_id);


--
-- TOC entry 3051 (class 2606 OID 16630)
-- Name: courses course_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT course_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3052 (class 2606 OID 16651)
-- Name: course_module course_module_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


--
-- TOC entry 3053 (class 2606 OID 16656)
-- Name: course_module course_module_module_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_module_id_fkey FOREIGN KEY (module_id) REFERENCES public.modules(module_id);


--
-- TOC entry 3048 (class 2606 OID 16539)
-- Name: department_building department_building_building_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_building_id_fkey FOREIGN KEY (building_id) REFERENCES public.buildings(building_id);


--
-- TOC entry 3047 (class 2606 OID 16534)
-- Name: department_building department_building_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3049 (class 2606 OID 16567)
-- Name: lecturer_timeslot_preferences lecturer_timeslot_preferences_lecturer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lecturer_timeslot_preferences_lecturer_id_fkey FOREIGN KEY (lecturer_id) REFERENCES public.lecturers(lecturer_id);


--
-- TOC entry 3050 (class 2606 OID 16572)
-- Name: lecturer_timeslot_preferences lecturer_timeslot_preferences_timeslot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lecturer_timeslot_preferences_timeslot_id_fkey FOREIGN KEY (timeslot_id) REFERENCES public.timeslots(timeslot_id);


--
-- TOC entry 3041 (class 2606 OID 16454)
-- Name: lecturers lecturers_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers
    ADD CONSTRAINT lecturers_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3042 (class 2606 OID 16472)
-- Name: modules module_lecturers_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT module_lecturers_fk FOREIGN KEY (lecturer_id) REFERENCES public.lecturers(lecturer_id);


--
-- TOC entry 3054 (class 2606 OID 16694)
-- Name: scheduled_modules scheduled_modules_module_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_module_id_fkey FOREIGN KEY (module_id) REFERENCES public.modules(module_id);


--
-- TOC entry 3055 (class 2606 OID 16699)
-- Name: scheduled_modules scheduled_modules_schedule_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES public.schedules(schedule_id);


--
-- TOC entry 3056 (class 2606 OID 16704)
-- Name: scheduled_modules scheduled_modules_timeslot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_timeslot_id_fkey FOREIGN KEY (timeslot_id) REFERENCES public.timeslots(timeslot_id);


--
-- TOC entry 3057 (class 2606 OID 16709)
-- Name: scheduled_modules scheduled_modules_venue_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_venue_id_fkey FOREIGN KEY (venue_id) REFERENCES public.venues(venue_id);


--
-- TOC entry 3045 (class 2606 OID 16524)
-- Name: schedules schedules_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(user_id);


--
-- TOC entry 3046 (class 2606 OID 16727)
-- Name: schedules schedules_jobs_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_jobs_fk FOREIGN KEY (job_id) REFERENCES public.jobs(job_id);


--
-- TOC entry 3043 (class 2606 OID 16609)
-- Name: users users_departments_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_departments_fk FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3044 (class 2606 OID 16505)
-- Name: venues venues_building_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues
    ADD CONSTRAINT venues_building_id_fkey FOREIGN KEY (building_id) REFERENCES public.buildings(building_id);


-- Completed on 2019-03-21 17:46:11

--
-- PostgreSQL database dump complete
--


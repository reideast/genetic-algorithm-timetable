--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.11
-- Dumped by pg_dump version 11.2

-- Started on 2019-04-01 23:08:23

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
-- TOC entry 3209 (class 0 OID 0)
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
-- TOC entry 3210 (class 0 OID 0)
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
-- TOC entry 3211 (class 0 OID 0)
-- Dependencies: 195
-- Name: COLUMN courses.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.courses.name IS 'Course name and year, e.g. 3BCT or 2BA';


--
-- TOC entry 3212 (class 0 OID 0)
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
-- TOC entry 3213 (class 0 OID 0)
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
-- TOC entry 3214 (class 0 OID 0)
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
-- TOC entry 3215 (class 0 OID 0)
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
-- TOC entry 3216 (class 0 OID 0)
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
    current_generation integer,
    last_status_update_time timestamp without time zone
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
-- TOC entry 3217 (class 0 OID 0)
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
-- TOC entry 3218 (class 0 OID 0)
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
    lecturer_id integer,
    is_lab boolean DEFAULT false
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
-- TOC entry 3219 (class 0 OID 0)
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
    is_master boolean,
    job_id integer,
    version integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 3220 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_wip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_wip IS 'User''s schedule which they are currently working with, but have not yet tagged as their accepted schedule: work in progress (WIP)';


--
-- TOC entry 3221 (class 0 OID 0)
-- Dependencies: 191
-- Name: COLUMN schedules.is_accepted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.schedules.is_accepted IS 'User''s schedule which is the current accepted schedule for their department. The schedule for which they would like to submit to the admin as a modification to the master schedule.';


--
-- TOC entry 3222 (class 0 OID 0)
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
-- TOC entry 3223 (class 0 OID 0)
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
    venue_id integer NOT NULL,
    is_locked_venue boolean DEFAULT false,
    is_locked_timeslot boolean DEFAULT false,
    is_valid boolean DEFAULT false
);


--
-- TOC entry 3224 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN scheduled_modules.is_locked_venue; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.scheduled_modules.is_locked_venue IS 'The user has tagged this module as LOCKED to this venue';


--
-- TOC entry 3225 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN scheduled_modules.is_locked_timeslot; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.scheduled_modules.is_locked_timeslot IS 'The user has tagged this module as LOCKED to this timeslot';


--
-- TOC entry 3226 (class 0 OID 0)
-- Dependencies: 206
-- Name: COLUMN scheduled_modules.is_valid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.scheduled_modules.is_valid IS 'The genetic algorithm has reported that this module has not been scheduled in an appropriate timeslot and/or venue';


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
-- TOC entry 3227 (class 0 OID 0)
-- Dependencies: 193
-- Name: COLUMN timeslots.day; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.timeslots.day IS '0,1,2,3,4,5,6';


--
-- TOC entry 3228 (class 0 OID 0)
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
-- TOC entry 3229 (class 0 OID 0)
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
    display_name text NOT NULL,
    department_id integer,
    is_facilities boolean DEFAULT false,
    is_admin boolean DEFAULT false,
    email text,
    roles text
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
-- TOC entry 3230 (class 0 OID 0)
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
-- TOC entry 3231 (class 0 OID 0)
-- Dependencies: 205
-- Name: venue_id_sequence; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.venue_id_sequence OWNED BY public.venues.venue_id;


--
-- TOC entry 3005 (class 2604 OID 16663)
-- Name: buildings building_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buildings ALTER COLUMN building_id SET DEFAULT nextval('public.building_id_sequence'::regclass);


--
-- TOC entry 3011 (class 2604 OID 16666)
-- Name: courses course_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses ALTER COLUMN course_id SET DEFAULT nextval('public.course_id_sequence'::regclass);


--
-- TOC entry 2998 (class 2604 OID 16670)
-- Name: departments department_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments ALTER COLUMN department_id SET DEFAULT nextval('public.department_id_sequence'::regclass);


--
-- TOC entry 3015 (class 2604 OID 16723)
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
-- TOC entry 3008 (class 2604 OID 16679)
-- Name: schedules schedule_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules ALTER COLUMN schedule_id SET DEFAULT nextval('public.schedule_id_sequence'::regclass);


--
-- TOC entry 3010 (class 2604 OID 16682)
-- Name: timeslots timeslot_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timeslots ALTER COLUMN timeslot_id SET DEFAULT nextval('public.timeslot_id_sequence'::regclass);


--
-- TOC entry 3004 (class 2604 OID 16685)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.user_id_sequence'::regclass);


--
-- TOC entry 3007 (class 2604 OID 16688)
-- Name: venues venue_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues ALTER COLUMN venue_id SET DEFAULT nextval('public.venue_id_sequence'::regclass);


--
-- TOC entry 3184 (class 0 OID 16489)
-- Dependencies: 189
-- Data for Name: buildings; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.buildings VALUES (3, 'Arts Millenium Building', '(53.2797339999999977,-9.06208299999999944)');
INSERT INTO public.buildings VALUES (4, 'Alice Perry Engineering Building', '(53.2836459999999974,-9.0638349999999992)');
INSERT INTO public.buildings VALUES (5, 'Áras Ui Chathail', '(53.2788719999999998,-9.05846000000000018)');
INSERT INTO public.buildings VALUES (6, 'Áras Cairnes', '(53.2840190000000007,-9.06576100000000018)');
INSERT INTO public.buildings VALUES (7, 'Ryan Institute Annex', '(53.2774780000000021,-9.05938399999999966)');
INSERT INTO public.buildings VALUES (8, 'Áras Moyola', '(53.2836649999999992,-9.0650429999999993)');
INSERT INTO public.buildings VALUES (9, 'Block E (former Civil Engineering Building)', '(53.2781829999999985,-9.06003800000000048)');
INSERT INTO public.buildings VALUES (10, 'Concourse, Tower 2 (1st Floor), Arts/Science Building', '(53.2806950000000015,-9.06079000000000079)');
INSERT INTO public.buildings VALUES (2, 'Concourse (Arts/Science Building)', '(53.2798129999999972,-9.06029200000000046)');
INSERT INTO public.buildings VALUES (1, 'Information Technology Building (IT)', '(53.2801729999999978,-9.05864200000000075)');
INSERT INTO public.buildings VALUES (11, 'Áras Na Gaeilge', '(53.2785799999999981,-9.06052200000000063)');
INSERT INTO public.buildings VALUES (12, 'Clinical Sciences', '(53.2753379999999979,-9.06619399999999942)');
INSERT INTO public.buildings VALUES (13, 'Block D (Education)', '(53.2782389999999992,-9.06058900000000023)');
INSERT INTO public.buildings VALUES (14, 'James Hardiman Library', '(53.279949000000002,-9.06125699999999945)');
INSERT INTO public.buildings VALUES (15, 'St Anthony', '(53.2839460000000003,-9.06627399999999994)');


--
-- TOC entry 3191 (class 0 OID 16643)
-- Dependencies: 196
-- Data for Name: course_module; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.course_module VALUES (4, 7, 'CT436');
INSERT INTO public.course_module VALUES (5, 8, 'CT1120');
INSERT INTO public.course_module VALUES (1, 9, 'CT102');
INSERT INTO public.course_module VALUES (9, 10, 'CT853');
INSERT INTO public.course_module VALUES (10, 10, 'CT853');
INSERT INTO public.course_module VALUES (11, 10, 'CT853');
INSERT INTO public.course_module VALUES (8, 11, 'ST313');
INSERT INTO public.course_module VALUES (4, 12, 'CT421');
INSERT INTO public.course_module VALUES (7, 12, 'CT319');
INSERT INTO public.course_module VALUES (11, 13, 'CT861');
INSERT INTO public.course_module VALUES (9, 13, 'CT861');
INSERT INTO public.course_module VALUES (10, 13, 'CT861');
INSERT INTO public.course_module VALUES (5, 14, 'CT1100');
INSERT INTO public.course_module VALUES (2, 15, 'CT213');
INSERT INTO public.course_module VALUES (1, 18, 'CT101');
INSERT INTO public.course_module VALUES (2, 20, 'CT230');
INSERT INTO public.course_module VALUES (6, 20, 'CT230');
INSERT INTO public.course_module VALUES (11, 20, 'CT511');
INSERT INTO public.course_module VALUES (9, 20, 'CT511');
INSERT INTO public.course_module VALUES (10, 20, 'CT511');
INSERT INTO public.course_module VALUES (3, 22, 'CT3532');
INSERT INTO public.course_module VALUES (2, 23, 'MA2286');
INSERT INTO public.course_module VALUES (8, 24, 'EE445 ');
INSERT INTO public.course_module VALUES (2, 25, 'MA204');
INSERT INTO public.course_module VALUES (2, 26, 'MA284');
INSERT INTO public.course_module VALUES (4, 27, 'MA3101');
INSERT INTO public.course_module VALUES (8, 28, 'ST1100');
INSERT INTO public.course_module VALUES (1, 29, 'EE130');
INSERT INTO public.course_module VALUES (4, 30, 'CT404');
INSERT INTO public.course_module VALUES (3, 31, 'MA3343 ');
INSERT INTO public.course_module VALUES (3, 32, 'CT318');
INSERT INTO public.course_module VALUES (7, 32, 'CT318');
INSERT INTO public.course_module VALUES (7, 33, 'CT327');
INSERT INTO public.course_module VALUES (11, 34, 'CT870');
INSERT INTO public.course_module VALUES (9, 34, 'CT870');
INSERT INTO public.course_module VALUES (10, 34, 'CT870');
INSERT INTO public.course_module VALUES (8, 36, 'CT5120');
INSERT INTO public.course_module VALUES (2, 38, 'ST237');
INSERT INTO public.course_module VALUES (4, 39, 'CT475');
INSERT INTO public.course_module VALUES (8, 39, 'CT475');
INSERT INTO public.course_module VALUES (1, 40, 'MA160');
INSERT INTO public.course_module VALUES (1, 41, 'MA190');
INSERT INTO public.course_module VALUES (1, 42, 'MA190');
INSERT INTO public.course_module VALUES (4, 43, 'MA490');
INSERT INTO public.course_module VALUES (3, 44, 'MA341');
INSERT INTO public.course_module VALUES (8, 45, 'MP305');
INSERT INTO public.course_module VALUES (4, 46, 'CT422');
INSERT INTO public.course_module VALUES (8, 46, 'CT422');
INSERT INTO public.course_module VALUES (3, 47, 'CT3531');
INSERT INTO public.course_module VALUES (3, 48, 'CT3531');
INSERT INTO public.course_module VALUES (1, 49, 'CT1113 ');
INSERT INTO public.course_module VALUES (2, 50, 'CT255');
INSERT INTO public.course_module VALUES (3, 52, 'CT3111');
INSERT INTO public.course_module VALUES (3, 54, 'MA385');
INSERT INTO public.course_module VALUES (7, 55, 'CT3535');
INSERT INTO public.course_module VALUES (2, 55, 'CT2106');
INSERT INTO public.course_module VALUES (6, 55, 'CT2101');
INSERT INTO public.course_module VALUES (3, 57, 'ST235');
INSERT INTO public.course_module VALUES (8, 57, 'ST235 ');
INSERT INTO public.course_module VALUES (1, 58, 'CT1112');
INSERT INTO public.course_module VALUES (8, 59, 'CT5102');
INSERT INTO public.course_module VALUES (1, 61, 'CT103');
INSERT INTO public.course_module VALUES (5, 61, 'CT1101');
INSERT INTO public.course_module VALUES (11, 61, 'CT874');
INSERT INTO public.course_module VALUES (9, 61, 'CT874');
INSERT INTO public.course_module VALUES (10, 61, 'CT874');
INSERT INTO public.course_module VALUES (3, 63, 'CT326');
INSERT INTO public.course_module VALUES (3, 65, 'CT331');
INSERT INTO public.course_module VALUES (9, 67, 'CT5117');
INSERT INTO public.course_module VALUES (4, 68, 'MA416');
INSERT INTO public.course_module VALUES (10, 69, 'CT5118');
INSERT INTO public.course_module VALUES (2, 70, 'CT216');
INSERT INTO public.course_module VALUES (3, 72, 'CT5106');
INSERT INTO public.course_module VALUES (4, 74, 'CT417');
INSERT INTO public.course_module VALUES (4, 75, 'CT561');
INSERT INTO public.course_module VALUES (8, 75, 'CT561');
INSERT INTO public.course_module VALUES (4, 76, 'CT561');
INSERT INTO public.course_module VALUES (8, 76, 'CT561');
INSERT INTO public.course_module VALUES (8, 77, 'CT5105');
INSERT INTO public.course_module VALUES (8, 78, 'CT5105');
INSERT INTO public.course_module VALUES (6, 79, 'CT2105');
INSERT INTO public.course_module VALUES (2, 16, 'CT213-L');
INSERT INTO public.course_module VALUES (5, 17, 'CT1100-L');
INSERT INTO public.course_module VALUES (1, 19, 'CT101-L');
INSERT INTO public.course_module VALUES (2, 21, 'CT230 -L');
INSERT INTO public.course_module VALUES (11, 35, 'CT870-L');
INSERT INTO public.course_module VALUES (8, 37, 'CT5120-L');
INSERT INTO public.course_module VALUES (2, 51, 'CT255-L');
INSERT INTO public.course_module VALUES (3, 53, 'CT3111-L');
INSERT INTO public.course_module VALUES (2, 56, 'CT2106-L');
INSERT INTO public.course_module VALUES (8, 60, 'CT5102-L');
INSERT INTO public.course_module VALUES (5, 62, 'CT1101-L');
INSERT INTO public.course_module VALUES (3, 64, 'CT326-L');
INSERT INTO public.course_module VALUES (3, 66, 'CT331-L');
INSERT INTO public.course_module VALUES (2, 71, 'CT216-L');
INSERT INTO public.course_module VALUES (3, 73, 'CT5106-L');
INSERT INTO public.course_module VALUES (11, 81, 'CT874-L');
INSERT INTO public.course_module VALUES (9, 82, 'CT874-L');
INSERT INTO public.course_module VALUES (10, 83, 'CT874-L');
INSERT INTO public.course_module VALUES (1, 80, 'CT103-L');
INSERT INTO public.course_module VALUES (6, 84, 'CT2101-L');
INSERT INTO public.course_module VALUES (7, 85, 'CT3535-L');
INSERT INTO public.course_module VALUES (6, 86, 'CT230-L');
INSERT INTO public.course_module VALUES (11, 87, 'CT511-L');
INSERT INTO public.course_module VALUES (9, 88, 'CT511-L');
INSERT INTO public.course_module VALUES (10, 89, 'CT511-L');
INSERT INTO public.course_module VALUES (9, 90, 'CT870-L');
INSERT INTO public.course_module VALUES (10, 91, 'CT870-L');


--
-- TOC entry 3190 (class 0 OID 16622)
-- Dependencies: 195
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.courses VALUES (8, '1CSD', 1, 30);
INSERT INTO public.courses VALUES (9, '1MF1', 1, 20);
INSERT INTO public.courses VALUES (10, '1SD1', 1, 25);
INSERT INTO public.courses VALUES (11, '1SD3', 1, 15);
INSERT INTO public.courses VALUES (2, '2BCT', 1, 70);
INSERT INTO public.courses VALUES (4, '4BCT', 1, 60);
INSERT INTO public.courses VALUES (6, '2BA', 1, 70);
INSERT INTO public.courses VALUES (7, '3BA', 1, 65);
INSERT INTO public.courses VALUES (1, '1BCT', 1, 75);
INSERT INTO public.courses VALUES (3, '3BCT', 1, 65);
INSERT INTO public.courses VALUES (5, '1BA', 1, 75);


--
-- TOC entry 3187 (class 0 OID 16529)
-- Dependencies: 192
-- Data for Name: department_building; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.department_building VALUES (1, 1, 20);
INSERT INTO public.department_building VALUES (1, 2, 15);
INSERT INTO public.department_building VALUES (1, 3, 10);
INSERT INTO public.department_building VALUES (1, 4, 9);
INSERT INTO public.department_building VALUES (1, 5, 9);
INSERT INTO public.department_building VALUES (1, 6, 1);
INSERT INTO public.department_building VALUES (1, 7, 0);
INSERT INTO public.department_building VALUES (1, 8, 1);
INSERT INTO public.department_building VALUES (1, 9, 5);
INSERT INTO public.department_building VALUES (1, 10, 1);
INSERT INTO public.department_building VALUES (1, 11, 0);
INSERT INTO public.department_building VALUES (1, 12, 0);
INSERT INTO public.department_building VALUES (1, 13, 0);
INSERT INTO public.department_building VALUES (1, 14, 1);
INSERT INTO public.department_building VALUES (1, 15, 0);
INSERT INTO public.department_building VALUES (2, 1, 10);
INSERT INTO public.department_building VALUES (2, 2, 12);
INSERT INTO public.department_building VALUES (2, 3, 13);
INSERT INTO public.department_building VALUES (2, 4, 20);
INSERT INTO public.department_building VALUES (2, 5, 5);
INSERT INTO public.department_building VALUES (2, 6, 15);
INSERT INTO public.department_building VALUES (2, 7, 0);
INSERT INTO public.department_building VALUES (2, 8, 12);
INSERT INTO public.department_building VALUES (2, 9, 2);
INSERT INTO public.department_building VALUES (2, 10, 1);
INSERT INTO public.department_building VALUES (2, 11, 0);
INSERT INTO public.department_building VALUES (2, 12, 0);
INSERT INTO public.department_building VALUES (2, 13, 0);
INSERT INTO public.department_building VALUES (2, 14, 1);
INSERT INTO public.department_building VALUES (2, 15, 0);


--
-- TOC entry 3180 (class 0 OID 16422)
-- Dependencies: 185
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.departments VALUES (1, 'Information Technology');
INSERT INTO public.departments VALUES (2, 'Electronic & Computer Engineering');


--
-- TOC entry 3203 (class 0 OID 16720)
-- Dependencies: 208
-- Data for Name: jobs; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3189 (class 0 OID 16562)
-- Dependencies: 194
-- Data for Name: lecturer_timeslot_preferences; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (1, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (5, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (6, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (7, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (8, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (9, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (10, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (12, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (13, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (16, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 34, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 35, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 36, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 37, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 38, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 39, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 40, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 41, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 42, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 43, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 44, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 45, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 46, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 47, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 48, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 49, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 50, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 51, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 52, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 53, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 54, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (17, 55, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (18, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (19, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (20, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (21, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (22, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (23, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (24, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (25, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (26, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (27, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 1, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 2, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 3, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 4, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 5, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 6, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 7, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 8, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 9, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 10, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 11, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 12, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 13, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 14, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 15, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 16, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 17, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 18, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 19, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 20, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 21, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 22, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 23, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 24, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 25, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 26, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 27, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 28, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 29, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 30, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 31, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 32, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 33, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 34, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 35, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 36, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 37, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 38, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 39, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 40, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 41, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 42, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 43, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 45, 8);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 46, 15);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 47, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 48, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 49, 20);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 50, 19);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 51, 18);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 52, 14);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 53, 7);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 54, 3);
INSERT INTO public.lecturer_timeslot_preferences VALUES (28, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (29, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (30, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (31, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (32, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (33, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (34, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (35, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (36, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (37, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (38, 55, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 1, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 2, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 3, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 4, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 5, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 6, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 7, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 8, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 9, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 10, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 11, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 12, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 13, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 14, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 15, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 16, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 17, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 18, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 19, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 20, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 21, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 22, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 23, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 24, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 25, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 26, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 27, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 28, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 29, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 30, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 31, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 32, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 33, 10);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 34, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 35, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 36, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 37, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 38, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 39, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 40, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 41, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 42, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 43, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 44, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 45, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 46, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 47, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 48, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 49, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 50, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 51, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 52, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 53, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 54, 0);
INSERT INTO public.lecturer_timeslot_preferences VALUES (39, 55, 0);


--
-- TOC entry 3181 (class 0 OID 16446)
-- Dependencies: 186
-- Data for Name: lecturers; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.lecturers VALUES (5, 'A Brennan', 1);
INSERT INTO public.lecturers VALUES (6, 'A McCluskey', 1);
INSERT INTO public.lecturers VALUES (7, 'C Mulvihill', 1);
INSERT INTO public.lecturers VALUES (9, 'D Chambers', 1);
INSERT INTO public.lecturers VALUES (10, 'D Flannery', 1);
INSERT INTO public.lecturers VALUES (12, 'D Roshansangachin', 1);
INSERT INTO public.lecturers VALUES (13, 'E Barrett', 1);
INSERT INTO public.lecturers VALUES (16, 'E Curry', 1);
INSERT INTO public.lecturers VALUES (17, 'F Glavin', 1);
INSERT INTO public.lecturers VALUES (18, 'F Smith', 1);
INSERT INTO public.lecturers VALUES (19, 'G Ellis', 1);
INSERT INTO public.lecturers VALUES (20, 'H Melvin', 1);
INSERT INTO public.lecturers VALUES (21, 'H Vornhagen', 1);
INSERT INTO public.lecturers VALUES (22, 'H Yang', 1);
INSERT INTO public.lecturers VALUES (23, 'I Ullah', 1);
INSERT INTO public.lecturers VALUES (24, 'J Breslin', 1);
INSERT INTO public.lecturers VALUES (25, 'J Cruickshank', 1);
INSERT INTO public.lecturers VALUES (26, 'J Duggan', 1);
INSERT INTO public.lecturers VALUES (27, 'J Griffith', 1);
INSERT INTO public.lecturers VALUES (28, 'J Newell', 1);
INSERT INTO public.lecturers VALUES (29, 'J Burns', 1);
INSERT INTO public.lecturers VALUES (30, 'M Madden', 1);
INSERT INTO public.lecturers VALUES (31, 'M Nickles', 1);
INSERT INTO public.lecturers VALUES (32, 'M Schukat', 1);
INSERT INTO public.lecturers VALUES (33, 'M Tuite', 1);
INSERT INTO public.lecturers VALUES (34, 'N Madden', 1);
INSERT INTO public.lecturers VALUES (35, 'N Snigireva', 1);
INSERT INTO public.lecturers VALUES (36, 'O Molloy', 1);
INSERT INTO public.lecturers VALUES (37, 'R Quinlan', 1);
INSERT INTO public.lecturers VALUES (38, 'S Hill', 1);
INSERT INTO public.lecturers VALUES (39, 'S Redfern', 1);
INSERT INTO public.lecturers VALUES (1, 'Lab tutor', 1);
INSERT INTO public.lecturers VALUES (8, 'C O''Riordan', 1);


--
-- TOC entry 3182 (class 0 OID 16459)
-- Dependencies: 187
-- Data for Name: modules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.modules VALUES (7, 'Advanced Professional Skills', 5, false);
INSERT INTO public.modules VALUES (8, 'Algorithms', 7, false);
INSERT INTO public.modules VALUES (9, 'Algorithms & Information Systems', 27, false);
INSERT INTO public.modules VALUES (10, 'Algorithms and Logical Methods', 7, false);
INSERT INTO public.modules VALUES (11, 'Applied Regression Models', 22, false);
INSERT INTO public.modules VALUES (12, 'Artificial Intelligence', 7, false);
INSERT INTO public.modules VALUES (13, 'Computer Architecture and Operating Systems', 32, false);
INSERT INTO public.modules VALUES (14, 'Computer Systems', 18, false);
INSERT INTO public.modules VALUES (15, 'Computer Systems & Organisation', 17, false);
INSERT INTO public.modules VALUES (16, 'Computer Systems & Organisation Lab', 1, true);
INSERT INTO public.modules VALUES (17, 'Computer Systems Lab', 1, true);
INSERT INTO public.modules VALUES (18, 'Computing Systems', 17, false);
INSERT INTO public.modules VALUES (19, 'Computing Systems Lab', 1, true);
INSERT INTO public.modules VALUES (20, 'Database Systems I', 27, false);
INSERT INTO public.modules VALUES (21, 'Database Systems I Lab', 1, true);
INSERT INTO public.modules VALUES (22, 'Database Systems II', 8, false);
INSERT INTO public.modules VALUES (23, 'Differential Forms', 19, false);
INSERT INTO public.modules VALUES (24, 'Digital Signal Processing', 1, false);
INSERT INTO public.modules VALUES (25, 'Discrete Mathematics', 34, false);
INSERT INTO public.modules VALUES (26, 'Discrete Mathematics (H)', 34, false);
INSERT INTO public.modules VALUES (27, 'Eculidean & Non-Euclidean Geometry', 29, false);
INSERT INTO public.modules VALUES (28, 'Engineering Statistics', 28, false);
INSERT INTO public.modules VALUES (29, 'Fundamentals of EEE', 24, false);
INSERT INTO public.modules VALUES (30, 'Graphics & Image Processing', 39, false);
INSERT INTO public.modules VALUES (31, 'Groups', 37, false);
INSERT INTO public.modules VALUES (32, 'Human Computer Interaction', 21, false);
INSERT INTO public.modules VALUES (33, 'Humanities Applications', 5, false);
INSERT INTO public.modules VALUES (34, 'Internet Programming ', 13, false);
INSERT INTO public.modules VALUES (35, 'Internet Programming Lab', 1, true);
INSERT INTO public.modules VALUES (36, 'Intro to Natural Lang. Processing', 1, false);
INSERT INTO public.modules VALUES (37, 'Intro to Natural Lang. Processing Lab', 1, true);
INSERT INTO public.modules VALUES (38, 'Intro. To Statistical Data and Probability', 12, false);
INSERT INTO public.modules VALUES (39, 'Machine Learning and Data Mining', 30, false);
INSERT INTO public.modules VALUES (40, 'Mathematics', 25, false);
INSERT INTO public.modules VALUES (41, 'Mathematics (H)', 19, false);
INSERT INTO public.modules VALUES (42, 'Mathematics workshop', 29, false);
INSERT INTO public.modules VALUES (43, 'Measure Theory', 35, false);
INSERT INTO public.modules VALUES (44, 'Metric Spaces', 6, false);
INSERT INTO public.modules VALUES (45, 'Modelling I', 1, false);
INSERT INTO public.modules VALUES (46, 'Modern Information Management', 8, false);
INSERT INTO public.modules VALUES (47, 'Network & Data Communications 2', 9, false);
INSERT INTO public.modules VALUES (48, 'Network & Data Communications 2 Lab', 9, false);
INSERT INTO public.modules VALUES (49, 'Next Generation Technologies l', 39, false);
INSERT INTO public.modules VALUES (50, 'Next Generation Technologies ll', 32, false);
INSERT INTO public.modules VALUES (51, 'Next Generation Technologies ll Lab', 1, true);
INSERT INTO public.modules VALUES (52, 'Next Generation Technology III', 39, false);
INSERT INTO public.modules VALUES (53, 'Next Generation Technology III Lab', 1, true);
INSERT INTO public.modules VALUES (54, 'Numerical Analysis I (H)', 34, false);
INSERT INTO public.modules VALUES (55, 'Object Oriented Programming', 38, false);
INSERT INTO public.modules VALUES (56, 'Object Oriented Programming Lab', 1, true);
INSERT INTO public.modules VALUES (57, 'Probability', 12, false);
INSERT INTO public.modules VALUES (58, 'Professional Skills I', 13, false);
INSERT INTO public.modules VALUES (59, 'Programming for Data Analytics ', 26, false);
INSERT INTO public.modules VALUES (60, 'Programming for Data Analytics Lab', 1, true);
INSERT INTO public.modules VALUES (61, 'Programming I', 36, false);
INSERT INTO public.modules VALUES (62, 'Programming I Lab', 1, true);
INSERT INTO public.modules VALUES (63, 'Programming III', 9, false);
INSERT INTO public.modules VALUES (64, 'Programming III Lab', 1, true);
INSERT INTO public.modules VALUES (65, 'Programming Paradigms', 18, false);
INSERT INTO public.modules VALUES (66, 'Programming Paradigms Lab', 1, true);
INSERT INTO public.modules VALUES (67, 'Research Project', 7, false);
INSERT INTO public.modules VALUES (68, 'Rings', 10, false);
INSERT INTO public.modules VALUES (69, 'Software Design & Development Project', 18, false);
INSERT INTO public.modules VALUES (70, 'Software Engineering I', 13, false);
INSERT INTO public.modules VALUES (71, 'Software Engineering I Lab', 1, true);
INSERT INTO public.modules VALUES (72, 'Software Engineering II', 36, false);
INSERT INTO public.modules VALUES (73, 'Software Engineering II Lab', 1, true);
INSERT INTO public.modules VALUES (74, 'Software Engineering III', 32, false);
INSERT INTO public.modules VALUES (75, 'Systems Modelling and Simulation', 26, false);
INSERT INTO public.modules VALUES (76, 'Systems Modelling and Simulation Lab', 26, false);
INSERT INTO public.modules VALUES (77, 'Tools & Techniques for Large Scale DA Lab', 31, false);
INSERT INTO public.modules VALUES (78, 'Tools & Techniques for Large Scale Data Analytics ', 31, false);
INSERT INTO public.modules VALUES (79, 'Web Information Systems', 27, false);
INSERT INTO public.modules VALUES (80, 'Programming I Lab', 1, true);
INSERT INTO public.modules VALUES (81, 'Programming I Lab', 1, true);
INSERT INTO public.modules VALUES (82, 'Programming I Lab', 1, true);
INSERT INTO public.modules VALUES (83, 'Programming I Lab', 1, true);
INSERT INTO public.modules VALUES (84, 'Object Oriented Programming Lab', 1, true);
INSERT INTO public.modules VALUES (85, 'Object Oriented Programming Lab', 1, true);
INSERT INTO public.modules VALUES (86, 'Database Systems I Lab', 1, true);
INSERT INTO public.modules VALUES (87, 'Database Systems I Lab', 1, true);
INSERT INTO public.modules VALUES (88, 'Database Systems I Lab', 1, true);
INSERT INTO public.modules VALUES (89, 'Database Systems I Lab', 1, true);
INSERT INTO public.modules VALUES (90, 'Internet Programming Lab', 1, true);
INSERT INTO public.modules VALUES (91, 'Internet Programming Lab', 1, true);


--
-- TOC entry 3201 (class 0 OID 16689)
-- Dependencies: 206
-- Data for Name: scheduled_modules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.scheduled_modules VALUES (3, 7, 29, 47, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 8, 6, 87, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 9, 27, 48, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 10, 45, 58, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 11, 7, 41, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 12, 44, 59, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 13, 19, 82, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 14, 47, 48, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 15, 27, 44, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 16, 38, 79, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 17, 55, 104, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 18, 1, 67, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 19, 19, 77, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 20, 14, 67, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 21, 9, 96, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 22, 5, 21, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 23, 26, 44, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 24, 21, 22, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 25, 3, 58, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 26, 32, 68, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 27, 5, 80, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 28, 27, 76, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 29, 15, 21, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 30, 19, 53, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 31, 2, 57, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 32, 39, 38, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 33, 8, 70, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 34, 46, 28, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 35, 48, 78, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 36, 49, 39, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 37, 50, 108, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 38, 17, 38, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 39, 53, 39, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 40, 51, 67, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 41, 16, 48, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 42, 37, 56, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 43, 8, 69, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 44, 40, 88, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 45, 47, 43, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 46, 20, 15, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 47, 38, 46, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 48, 27, 80, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 49, 44, 45, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 50, 28, 70, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 51, 46, 94, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 52, 43, 58, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 53, 17, 78, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 54, 42, 55, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 55, 30, 39, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 56, 6, 79, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 57, 41, 59, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 58, 54, 45, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 59, 5, 25, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 60, 13, 96, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 61, 50, 45, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 62, 34, 79, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 63, 26, 87, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 64, 51, 94, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 65, 29, 66, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 66, 45, 78, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 67, 35, 87, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 68, 18, 46, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 69, 37, 82, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 70, 50, 70, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 71, 43, 77, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 72, 50, 57, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 73, 12, 77, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 74, 24, 58, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 75, 16, 81, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 76, 17, 87, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 77, 52, 56, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 78, 48, 66, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 79, 36, 57, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 80, 25, 79, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 81, 32, 98, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 82, 23, 91, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 83, 53, 108, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 84, 39, 78, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 85, 9, 94, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 86, 49, 78, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 87, 5, 98, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 88, 47, 96, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 89, 55, 92, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 90, 48, 103, false, false, false);
INSERT INTO public.scheduled_modules VALUES (3, 91, 29, 78, false, false, false);


--
-- TOC entry 3186 (class 0 OID 16519)
-- Dependencies: 191
-- Data for Name: schedules; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.schedules VALUES (12, '2019-03-18 13:23:33.564', 10, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (13, '2019-03-17 11:49:58.55', 10, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (14, '2019-03-18 13:23:33.564', 11, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (4, '2019-03-18 20:30:50', 12287, true, false, false, NULL, 5);
INSERT INTO public.schedules VALUES (15, '2019-03-17 11:49:58.55', 11, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (16, '2019-03-18 13:23:33.564', 12, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (17, '2019-03-17 11:49:58.55', 12, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (18, '2019-03-18 13:23:33.564', 13, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (19, '2019-03-17 11:49:58.55', 13, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (20, '2019-03-18 13:23:33.564', 14, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (21, '2019-03-17 11:49:58.55', 14, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (22, '2019-03-18 13:23:33.564', 15, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (23, '2019-03-17 11:49:58.55', 15, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (24, '2019-03-18 13:23:33.564', 16, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (25, '2019-03-17 11:49:58.55', 16, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (26, '2019-03-18 13:23:33.564', 17, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (27, '2019-03-17 11:49:58.55', 17, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (28, '2019-03-18 13:23:33.564', 18, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (29, '2019-03-17 11:49:58.55', 18, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (30, '2019-03-18 13:23:33.564', 19, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (31, '2019-03-17 11:49:58.55', 19, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (32, '2019-03-18 13:23:33.564', 20, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (33, '2019-03-17 11:49:58.55', 20, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (34, '2019-03-18 13:23:33.564', 21, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (35, '2019-03-17 11:49:58.55', 21, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (36, '2019-03-18 13:23:33.564', 22, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (37, '2019-03-17 11:49:58.55', 22, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (38, '2019-03-18 13:23:33.564', 23, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (39, '2019-03-17 11:49:58.55', 23, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (40, '2019-03-18 13:23:33.564', 24, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (41, '2019-03-17 11:49:58.55', 24, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (42, '2019-03-18 13:23:33.564', 25, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (43, '2019-03-17 11:49:58.55', 25, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (44, '2019-03-18 13:23:33.564', 26, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (45, '2019-03-17 11:49:58.55', 26, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (46, '2019-03-18 13:23:33.564', 27, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (47, '2019-03-17 11:49:58.55', 27, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (48, '2019-03-18 13:23:33.564', 28, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (49, '2019-03-17 11:49:58.55', 28, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (50, '2019-03-18 13:23:33.564', 29, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (51, '2019-03-17 11:49:58.55', 29, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (52, '2019-03-18 13:23:33', 4, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (53, '2019-03-17 11:49:58', 4, false, false, false, NULL, 0);
INSERT INTO public.schedules VALUES (2, '2019-03-18 19:49:58.55', 3, true, false, false, NULL, 340);
INSERT INTO public.schedules VALUES (1, '2019-03-18 19:50:52.812', 3, true, false, true, NULL, 332);
INSERT INTO public.schedules VALUES (3, '2019-03-25 23:47:53.236', 3, true, false, false, NULL, 737);


--
-- TOC entry 3188 (class 0 OID 16544)
-- Dependencies: 193
-- Data for Name: timeslots; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.timeslots VALUES (1, 0, 9);
INSERT INTO public.timeslots VALUES (2, 0, 10);
INSERT INTO public.timeslots VALUES (3, 0, 11);
INSERT INTO public.timeslots VALUES (4, 0, 12);
INSERT INTO public.timeslots VALUES (5, 0, 13);
INSERT INTO public.timeslots VALUES (6, 0, 14);
INSERT INTO public.timeslots VALUES (7, 0, 15);
INSERT INTO public.timeslots VALUES (8, 0, 16);
INSERT INTO public.timeslots VALUES (9, 0, 17);
INSERT INTO public.timeslots VALUES (10, 0, 18);
INSERT INTO public.timeslots VALUES (11, 0, 19);
INSERT INTO public.timeslots VALUES (12, 1, 9);
INSERT INTO public.timeslots VALUES (13, 1, 10);
INSERT INTO public.timeslots VALUES (14, 1, 11);
INSERT INTO public.timeslots VALUES (15, 1, 12);
INSERT INTO public.timeslots VALUES (16, 1, 13);
INSERT INTO public.timeslots VALUES (17, 1, 14);
INSERT INTO public.timeslots VALUES (18, 1, 15);
INSERT INTO public.timeslots VALUES (19, 1, 16);
INSERT INTO public.timeslots VALUES (20, 1, 17);
INSERT INTO public.timeslots VALUES (21, 1, 18);
INSERT INTO public.timeslots VALUES (22, 1, 19);
INSERT INTO public.timeslots VALUES (23, 2, 9);
INSERT INTO public.timeslots VALUES (24, 2, 10);
INSERT INTO public.timeslots VALUES (25, 2, 11);
INSERT INTO public.timeslots VALUES (26, 2, 12);
INSERT INTO public.timeslots VALUES (27, 2, 13);
INSERT INTO public.timeslots VALUES (28, 2, 14);
INSERT INTO public.timeslots VALUES (29, 2, 15);
INSERT INTO public.timeslots VALUES (30, 2, 16);
INSERT INTO public.timeslots VALUES (31, 2, 17);
INSERT INTO public.timeslots VALUES (32, 2, 18);
INSERT INTO public.timeslots VALUES (33, 2, 19);
INSERT INTO public.timeslots VALUES (34, 3, 9);
INSERT INTO public.timeslots VALUES (35, 3, 10);
INSERT INTO public.timeslots VALUES (36, 3, 11);
INSERT INTO public.timeslots VALUES (37, 3, 12);
INSERT INTO public.timeslots VALUES (38, 3, 13);
INSERT INTO public.timeslots VALUES (39, 3, 14);
INSERT INTO public.timeslots VALUES (40, 3, 15);
INSERT INTO public.timeslots VALUES (41, 3, 16);
INSERT INTO public.timeslots VALUES (42, 3, 17);
INSERT INTO public.timeslots VALUES (43, 3, 18);
INSERT INTO public.timeslots VALUES (44, 3, 19);
INSERT INTO public.timeslots VALUES (45, 4, 9);
INSERT INTO public.timeslots VALUES (46, 4, 10);
INSERT INTO public.timeslots VALUES (47, 4, 11);
INSERT INTO public.timeslots VALUES (48, 4, 12);
INSERT INTO public.timeslots VALUES (49, 4, 13);
INSERT INTO public.timeslots VALUES (50, 4, 14);
INSERT INTO public.timeslots VALUES (51, 4, 15);
INSERT INTO public.timeslots VALUES (52, 4, 16);
INSERT INTO public.timeslots VALUES (53, 4, 17);
INSERT INTO public.timeslots VALUES (54, 4, 18);
INSERT INTO public.timeslots VALUES (55, 4, 19);


--
-- TOC entry 3183 (class 0 OID 16477)
-- Dependencies: 188
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.users VALUES (20, 'user10', '$2a$10$Pfe82hkul1OXGGJG9t1DqeRJuQWxlQ3MpgC46IzDkFZ5eEDs2BO7.', 'Scheduler 10', 1, false, false, 'user10@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (21, 'user11', '$2a$10$5JQ8QxRrf.UwgySMRNBLiemQEvz95vFxdmi7FpNa/JePJ3h1p8cna', 'Scheduler 11', 1, false, false, 'user11@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (22, 'user12', '$2a$10$gcXbMnfAG9iMwFweNLDdMO.4ayFlUDvxr7mRzJGiWF9wpOcDX8n1q', 'Scheduler 12', 1, false, false, 'user12@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (23, 'user13', '$2a$10$XHEWxjc0gO/TZhodg7KWZ.23w3RwaPX3MzUbqmtOBl.EPYXc8Sk.m', 'Scheduler 13', 1, false, false, 'user13@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (24, 'user14', '$2a$10$4T60wHRAAowuEh5YvyIFq.eZIgS8M42T6L2vaPM5tIq06Xz.ql8qW', 'Scheduler 14', 1, false, false, 'user14@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (25, 'user15', '$2a$10$LAg3Os9dKwj2j91OIubVT./.eRoq7N8mOIlrGL6N.XQ2GZGvmMgja', 'Scheduler 15', 1, false, false, 'user15@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (26, 'user16', '$2a$10$Rp46LacpGucFHToUUI6sYeRhtXRZayPz.eS4nMe1YB5FEGDZ6I1T.', 'Scheduler 16', 1, false, false, 'user16@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (27, 'user17', '$2a$10$c7bdtc.hMfKMWCvqWDbHpO28ch2Mmkk6/z2.FIgWKjomMQeIWaQH2', 'Scheduler 17', 1, false, false, 'user17@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (28, 'user18', '$2a$10$L0CHsPmqb2EsdgOfMYDbDeD5OZjF6r11NRyhbn.ErbnAAeznP7bX2', 'Scheduler 18', 1, false, false, 'user18@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (29, 'user19', '$2a$10$j5GQRKwIdGX0ZC9YZNdS2OTDlT..VH6Ph9tTIud5gQXBLaSqkq5mG', 'Scheduler 19', 1, false, false, 'user19@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (12287, 'admin', '$2a$10$usIGjWQXGnIEeLQu6IF30Os4e5ICFvmviR6PetdjHPLq.LUX4IxxG', 'Administrator', 1, true, true, 'andrew@andreweast.net', 'ROLE_ADMIN,ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (10, 'user0', '$2a$10$mUA1TBqdhcqce0vurxD8LeGUv4QMB9zymZ.AaxTfoECIvszR7k.n.', 'Scheduler 0', 1, false, false, 'user0@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (11, 'user1', '$2a$10$4dNe0q.1QR5MVPZchWfFAuU7gLl2E3E2PVyBQ5xxD7nKXdEGCUVTW', 'Scheduler 1', 1, false, false, 'user1@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (12, 'user2', '$2a$10$ZVN3VorMqyzM1q67Sv2Q8uM.jOOOQkm2grmHuoV3QsfK17uQmmBZW', 'Scheduler 2', 1, false, false, 'user2@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (13, 'user3', '$2a$10$tRGu9jsUvAIiguQmwc7JVOIZI9Bg08Xjznzs4byiQ9i4W2feyMXLG', 'Scheduler 3', 1, false, false, 'user3@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (14, 'user4', '$2a$10$biKzP1uJSVf2bGmH7Y9cyeVBrl18fCnCUg/I3faXtBJSjI.SoSC0i', 'Scheduler 4', 1, false, false, 'user4@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (15, 'user5', '$2a$10$K0sBluANvb86Xh0YXFaaOONst.MH8MM.KzcKHHz0.7kOTQ9zGZJUe', 'Scheduler 5', 1, false, false, 'user5@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (16, 'user6', '$2a$10$uGDb59qmkinGG52pCBYeRunFMjN6.O7iyFsxNsQe5VqQDV2qAjH/6', 'Scheduler 6', 1, false, false, 'user6@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (17, 'user7', '$2a$10$.prkEiAdcq3gbNRG0TJkVeX8tXkof2fmnDSMctXyFSbToMQVma89i', 'Scheduler 7', 1, false, false, 'user7@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (18, 'user8', '$2a$10$3YLy/1.UG.d.pmpwhH5Ine6up8J3ff1T9u1Nh8y3ZOXfa3N/4uNk.', 'Scheduler 8', 1, false, false, 'user8@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (19, 'user9', '$2a$10$SfmldWUFSntPQtSFJifKhumeU4K4Ec/uXoLW7XZc7TSXXEbDRxVZK', 'Scheduler 9', 1, false, false, 'user9@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (3, 'it', '$2a$10$m06zMQq2PNfgtTInjuz41eaPpGyFu6iUQ4/dXvXckFMzJMTWCu8nK', 'IT Dept Admin', 1, false, false, 'it@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users VALUES (4, 'ece', '$2a$10$eCXnuf0dM/CstF5trWvkT.f060gMg1yrgn9JIegNappN2UUHlqI2u', 'ECE Dept Admin', 2, false, false, 'ece@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');


--
-- TOC entry 3185 (class 0 OID 16497)
-- Dependencies: 190
-- Data for Name: venues; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.venues VALUES (103, 'ENG2017 PC Suite', 4, true, 70);
INSERT INTO public.venues VALUES (6, 'CA001', 6, false, 30);
INSERT INTO public.venues VALUES (7, 'CA002', 6, false, 30);
INSERT INTO public.venues VALUES (8, 'CA003', 6, false, 30);
INSERT INTO public.venues VALUES (9, 'CA004', 6, false, 30);
INSERT INTO public.venues VALUES (10, 'CA114', 6, false, 20);
INSERT INTO public.venues VALUES (11, 'CA115', 6, false, 20);
INSERT INTO public.venues VALUES (12, 'CA116a', 6, false, 60);
INSERT INTO public.venues VALUES (13, 'CA117 (MBA Room)', 6, false, 48);
INSERT INTO public.venues VALUES (14, 'CA118', 6, false, 60);
INSERT INTO public.venues VALUES (15, 'CA111 (Lecture Hall 1)', 6, false, 105);
INSERT INTO public.venues VALUES (16, 'CA101 (Lecture Hall 2)', 6, false, 40);
INSERT INTO public.venues VALUES (17, 'MY123', 8, false, 30);
INSERT INTO public.venues VALUES (18, 'MY124', 8, false, 30);
INSERT INTO public.venues VALUES (19, 'MY125', 8, false, 30);
INSERT INTO public.venues VALUES (20, 'MY126', 8, false, 30);
INSERT INTO public.venues VALUES (21, 'MY127', 8, false, 80);
INSERT INTO public.venues VALUES (22, 'MY129', 8, false, 105);
INSERT INTO public.venues VALUES (23, 'MY231', 8, false, 20);
INSERT INTO public.venues VALUES (24, 'MY232', 8, false, 20);
INSERT INTO public.venues VALUES (25, 'MY243', 8, false, 232);
INSERT INTO public.venues VALUES (26, 'MY336', 8, false, 30);
INSERT INTO public.venues VALUES (27, 'MY337', 8, false, 30);
INSERT INTO public.venues VALUES (28, 'AUC-G002 (Theatre Áras Ui Chathail)', 5, false, 166);
INSERT INTO public.venues VALUES (29, 'AM104', 3, false, 34);
INSERT INTO public.venues VALUES (30, 'AM105', 3, false, 30);
INSERT INTO public.venues VALUES (31, 'AM107', 3, false, 40);
INSERT INTO public.venues VALUES (32, 'AM108', 3, false, 40);
INSERT INTO public.venues VALUES (33, 'AM109', 3, false, 40);
INSERT INTO public.venues VALUES (34, 'AM110', 3, false, 40);
INSERT INTO public.venues VALUES (35, 'AM112', 3, false, 30);
INSERT INTO public.venues VALUES (36, 'AM121', 3, false, 12);
INSERT INTO public.venues VALUES (37, 'AM122', 3, false, 12);
INSERT INTO public.venues VALUES (39, 'Fottrell Theatre', 3, false, 212);
INSERT INTO public.venues VALUES (41, 'AMB-G036', 3, false, 45);
INSERT INTO public.venues VALUES (42, 'AMB-G043', 3, false, 30);
INSERT INTO public.venues VALUES (43, 'BLE-1006', 9, false, 80);
INSERT INTO public.venues VALUES (45, 'Anderson Theatre', 2, false, 242);
INSERT INTO public.venues VALUES (47, 'AC201', 2, false, 97);
INSERT INTO public.venues VALUES (48, 'AC202', 2, false, 88);
INSERT INTO public.venues VALUES (49, 'AC203', 2, false, 48);
INSERT INTO public.venues VALUES (50, 'AC204', 2, false, 48);
INSERT INTO public.venues VALUES (51, 'AC213', 2, false, 70);
INSERT INTO public.venues VALUES (52, 'AC214', 2, false, 56);
INSERT INTO public.venues VALUES (53, 'AC215', 2, false, 70);
INSERT INTO public.venues VALUES (54, 'AC216', 2, false, 70);
INSERT INTO public.venues VALUES (55, 'Kirwan Theatre', 2, false, 312);
INSERT INTO public.venues VALUES (56, 'Larmor Theatre', 2, false, 93);
INSERT INTO public.venues VALUES (57, 'Dillon Theatre', 2, false, 95);
INSERT INTO public.venues VALUES (58, 'McMunn Theatre', 2, false, 94);
INSERT INTO public.venues VALUES (59, 'Tyndall Theatre', 2, false, 125);
INSERT INTO public.venues VALUES (60, 'TB301', 10, false, 20);
INSERT INTO public.venues VALUES (61, 'TB302', 10, false, 20);
INSERT INTO public.venues VALUES (62, 'TB303', 10, false, 30);
INSERT INTO public.venues VALUES (63, 'TB304', 10, false, 19);
INSERT INTO public.venues VALUES (64, 'TB305', 10, false, 19);
INSERT INTO public.venues VALUES (65, 'TB307', 10, false, 22);
INSERT INTO public.venues VALUES (66, 'ENG-G017', 4, false, 136);
INSERT INTO public.venues VALUES (67, 'ENG-G018', 4, false, 246);
INSERT INTO public.venues VALUES (68, 'ENG-G047', 4, false, 117);
INSERT INTO public.venues VALUES (69, 'ENG-2001', 4, false, 74);
INSERT INTO public.venues VALUES (70, 'ENG-2002', 4, false, 75);
INSERT INTO public.venues VALUES (71, 'ENG-2003', 4, false, 52);
INSERT INTO public.venues VALUES (72, 'ENG-2033', 4, false, 50);
INSERT INTO public.venues VALUES (73, 'ENG-2034', 4, false, 50);
INSERT INTO public.venues VALUES (74, 'ENG-2035', 4, false, 50);
INSERT INTO public.venues VALUES (75, 'ENG-3035', 4, false, 74);
INSERT INTO public.venues VALUES (76, 'ENG-3036', 4, false, 40);
INSERT INTO public.venues VALUES (80, 'IT125', 1, false, 122);
INSERT INTO public.venues VALUES (81, 'IT125G', 1, false, 122);
INSERT INTO public.venues VALUES (82, 'IT202', 1, false, 60);
INSERT INTO public.venues VALUES (83, 'IT203', 1, false, 36);
INSERT INTO public.venues VALUES (84, 'IT204', 1, false, 59);
INSERT INTO public.venues VALUES (85, 'IT206', 1, false, 20);
INSERT INTO public.venues VALUES (86, 'IT207', 1, false, 33);
INSERT INTO public.venues VALUES (87, 'IT250', 1, false, 244);
INSERT INTO public.venues VALUES (88, 'MRA201 (MRI Theatre)', 7, false, 159);
INSERT INTO public.venues VALUES (89, 'Áras Na Gaeilge PC Suite', 11, true, 16);
INSERT INTO public.venues VALUES (90, 'Environmental Science PC Suite Room 209', 2, true, 13);
INSERT INTO public.venues VALUES (91, 'Software Engineering PC Suite  Room 228/228A', 2, true, 40);
INSERT INTO public.venues VALUES (92, 'Arts Faculty PC Suite Room 229/229A', 2, true, 35);
INSERT INTO public.venues VALUES (93, 'Arts-Science PC Suite Room 105', 2, true, 45);
INSERT INTO public.venues VALUES (95, 'Block E PC Suite Room E102', 9, true, 39);
INSERT INTO public.venues VALUES (97, 'Cairnes PC Suite Room 112', 6, true, 38);
INSERT INTO public.venues VALUES (98, 'MIME PC Suite', 12, true, 21);
INSERT INTO public.venues VALUES (99, 'Clinical Science PC Suite', 12, true, 22);
INSERT INTO public.venues VALUES (100, 'Clinical Science Library PC Suite', 12, true, 11);
INSERT INTO public.venues VALUES (101, 'Education PC Suite Room D101', 13, true, 30);
INSERT INTO public.venues VALUES (102, 'ENG2016 PC Suite', 4, true, 41);
INSERT INTO public.venues VALUES (104, 'ENGG0046 PC Suite', 4, true, 80);
INSERT INTO public.venues VALUES (105, 'Library 1st Floor PC Suite', 14, true, 20);
INSERT INTO public.venues VALUES (106, 'Library 2nd Floor PC Suite', 14, true, 32);
INSERT INTO public.venues VALUES (107, 'Library Special Collections PC Suite', 14, true, 6);
INSERT INTO public.venues VALUES (108, 'Friary PC Suite Room 105', 15, true, 31);
INSERT INTO public.venues VALUES (109, 'Menlo PC Suite Room 102', 15, true, 19);
INSERT INTO public.venues VALUES (38, 'O''Tnuathail Theatre', 3, false, 147);
INSERT INTO public.venues VALUES (40, 'O''hEocha Theatre', 3, false, 252);
INSERT INTO public.venues VALUES (44, 'O''Flaherty Theatre', 2, false, 356);
INSERT INTO public.venues VALUES (46, 'D''Arcy Thompson Theatre', 2, false, 160);
INSERT INTO public.venues VALUES (79, 'IT106 PC Suite', 1, true, 100);
INSERT INTO public.venues VALUES (77, 'IT101 PC Suite', 1, true, 85);
INSERT INTO public.venues VALUES (78, 'IT102 PC Suite', 1, true, 70);
INSERT INTO public.venues VALUES (96, 'Finnegan PC Suite', 9, true, 70);
INSERT INTO public.venues VALUES (94, 'Arts Millennium PC Suite Room 201', 3, true, 70);


--
-- TOC entry 3232 (class 0 OID 0)
-- Dependencies: 197
-- Name: building_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.building_id_sequence', 20, true);


--
-- TOC entry 3233 (class 0 OID 0)
-- Dependencies: 198
-- Name: course_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.course_id_sequence', 62, true);


--
-- TOC entry 3234 (class 0 OID 0)
-- Dependencies: 199
-- Name: department_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.department_id_sequence', 3, true);


--
-- TOC entry 3235 (class 0 OID 0)
-- Dependencies: 207
-- Name: job_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.job_id_seq', 809, true);


--
-- TOC entry 3236 (class 0 OID 0)
-- Dependencies: 200
-- Name: lecturer_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.lecturer_id_sequence', 39, true);


--
-- TOC entry 3237 (class 0 OID 0)
-- Dependencies: 201
-- Name: module_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.module_id_sequence', 91, true);


--
-- TOC entry 3238 (class 0 OID 0)
-- Dependencies: 202
-- Name: schedule_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.schedule_id_sequence', 53, true);


--
-- TOC entry 3239 (class 0 OID 0)
-- Dependencies: 203
-- Name: timeslot_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.timeslot_id_sequence', 55, true);


--
-- TOC entry 3240 (class 0 OID 0)
-- Dependencies: 204
-- Name: user_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_id_sequence', 29, true);


--
-- TOC entry 3241 (class 0 OID 0)
-- Dependencies: 205
-- Name: venue_id_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.venue_id_sequence', 109, true);


--
-- TOC entry 3027 (class 2606 OID 16496)
-- Name: buildings buildings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.buildings
    ADD CONSTRAINT buildings_pkey PRIMARY KEY (building_id);


--
-- TOC entry 3041 (class 2606 OID 16650)
-- Name: course_module course_module_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_pk PRIMARY KEY (course_id, module_id);


--
-- TOC entry 3039 (class 2606 OID 16629)
-- Name: courses course_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT course_pkey PRIMARY KEY (course_id);


--
-- TOC entry 3033 (class 2606 OID 16578)
-- Name: department_building department_building_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_pk PRIMARY KEY (department_id, building_id);


--
-- TOC entry 3017 (class 2606 OID 16429)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (department_id);


--
-- TOC entry 3045 (class 2606 OID 16725)
-- Name: jobs job_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.jobs
    ADD CONSTRAINT job_pkey PRIMARY KEY (job_id);


--
-- TOC entry 3037 (class 2606 OID 16566)
-- Name: lecturer_timeslot_preferences lec_time_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lec_time_pk PRIMARY KEY (lecturer_id, timeslot_id);


--
-- TOC entry 3019 (class 2606 OID 16453)
-- Name: lecturers lecturers_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers
    ADD CONSTRAINT lecturers_pkey PRIMARY KEY (lecturer_id);


--
-- TOC entry 3021 (class 2606 OID 16466)
-- Name: modules module_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT module_pkey PRIMARY KEY (module_id);


--
-- TOC entry 3043 (class 2606 OID 16693)
-- Name: scheduled_modules scheduled_modules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_pkey PRIMARY KEY (schedule_id, module_id);


--
-- TOC entry 3031 (class 2606 OID 16523)
-- Name: schedules schedules_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_pkey PRIMARY KEY (schedule_id);


--
-- TOC entry 3035 (class 2606 OID 16548)
-- Name: timeslots timeslots_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.timeslots
    ADD CONSTRAINT timeslots_pkey PRIMARY KEY (timeslot_id);


--
-- TOC entry 3023 (class 2606 OID 16484)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3025 (class 2606 OID 16486)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3029 (class 2606 OID 16504)
-- Name: venues venues_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues
    ADD CONSTRAINT venues_pkey PRIMARY KEY (venue_id);


--
-- TOC entry 3056 (class 2606 OID 16630)
-- Name: courses course_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT course_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3057 (class 2606 OID 16651)
-- Name: course_module course_module_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


--
-- TOC entry 3058 (class 2606 OID 16656)
-- Name: course_module course_module_module_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.course_module
    ADD CONSTRAINT course_module_module_id_fkey FOREIGN KEY (module_id) REFERENCES public.modules(module_id);


--
-- TOC entry 3053 (class 2606 OID 16539)
-- Name: department_building department_building_building_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_building_id_fkey FOREIGN KEY (building_id) REFERENCES public.buildings(building_id);


--
-- TOC entry 3052 (class 2606 OID 16534)
-- Name: department_building department_building_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.department_building
    ADD CONSTRAINT department_building_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3054 (class 2606 OID 16567)
-- Name: lecturer_timeslot_preferences lecturer_timeslot_preferences_lecturer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lecturer_timeslot_preferences_lecturer_id_fkey FOREIGN KEY (lecturer_id) REFERENCES public.lecturers(lecturer_id);


--
-- TOC entry 3055 (class 2606 OID 16572)
-- Name: lecturer_timeslot_preferences lecturer_timeslot_preferences_timeslot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturer_timeslot_preferences
    ADD CONSTRAINT lecturer_timeslot_preferences_timeslot_id_fkey FOREIGN KEY (timeslot_id) REFERENCES public.timeslots(timeslot_id);


--
-- TOC entry 3046 (class 2606 OID 16454)
-- Name: lecturers lecturers_department_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lecturers
    ADD CONSTRAINT lecturers_department_id_fkey FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3047 (class 2606 OID 16472)
-- Name: modules module_lecturers_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.modules
    ADD CONSTRAINT module_lecturers_fk FOREIGN KEY (lecturer_id) REFERENCES public.lecturers(lecturer_id);


--
-- TOC entry 3059 (class 2606 OID 16694)
-- Name: scheduled_modules scheduled_modules_module_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_module_id_fkey FOREIGN KEY (module_id) REFERENCES public.modules(module_id);


--
-- TOC entry 3060 (class 2606 OID 16699)
-- Name: scheduled_modules scheduled_modules_schedule_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_schedule_id_fkey FOREIGN KEY (schedule_id) REFERENCES public.schedules(schedule_id);


--
-- TOC entry 3061 (class 2606 OID 16704)
-- Name: scheduled_modules scheduled_modules_timeslot_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_timeslot_id_fkey FOREIGN KEY (timeslot_id) REFERENCES public.timeslots(timeslot_id);


--
-- TOC entry 3062 (class 2606 OID 16709)
-- Name: scheduled_modules scheduled_modules_venue_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.scheduled_modules
    ADD CONSTRAINT scheduled_modules_venue_id_fkey FOREIGN KEY (venue_id) REFERENCES public.venues(venue_id);


--
-- TOC entry 3050 (class 2606 OID 16524)
-- Name: schedules schedules_creator_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_creator_id_fkey FOREIGN KEY (creator_id) REFERENCES public.users(user_id);


--
-- TOC entry 3051 (class 2606 OID 16727)
-- Name: schedules schedules_jobs_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_jobs_fk FOREIGN KEY (job_id) REFERENCES public.jobs(job_id);


--
-- TOC entry 3048 (class 2606 OID 16609)
-- Name: users users_departments_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_departments_fk FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3049 (class 2606 OID 16505)
-- Name: venues venues_building_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.venues
    ADD CONSTRAINT venues_building_id_fkey FOREIGN KEY (building_id) REFERENCES public.buildings(building_id);


-- Completed on 2019-04-01 23:08:28

--
-- PostgreSQL database dump complete
--


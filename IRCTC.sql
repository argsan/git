create database solace;
use solace;

create TABLE domain_master(
domin_id INT NOT NULL AUTO_INCREMENT,
domain_name VARCHAR(100),
PRIMARY KEY (domin_id)
);
INSERT INTO domain_master values (1,'trains');
INSERT INTO domain_master values (2,'login');
INSERT INTO domain_master values (3,'product');

create TABLE domain_detail(
domin_detail_id INT NOT NULL AUTO_INCREMENT,
domin_id INT NOT NULL,
domain_detail_name VARCHAR(100),
PRIMARY KEY (domin_detail_id),
FOREIGN KEY (domin_id) REFERENCES domain_master(domin_id)
                      ON DELETE CASCADE
);
INSERT INTO domain_detail values (1,1,'train_no');
INSERT INTO domain_detail values (2,1,'train_name');
INSERT INTO domain_detail values (3,1,'origin');
INSERT INTO domain_detail values (4,1,'destination');
INSERT INTO domain_detail values (5,1,'deperture');
INSERT INTO domain_detail values (6,1,'arrival');
INSERT INTO domain_detail values (7,1,'travel_time');
INSERT INTO domain_detail values (8,1,'days_of_run');
INSERT INTO domain_detail values (9,1,'monday');
INSERT INTO domain_detail values (10,1,'tuesday');
INSERT INTO domain_detail values (11,1,'wednesday');
INSERT INTO domain_detail values (12,1,'thursday');
INSERT INTO domain_detail values (13,1,'friday');
INSERT INTO domain_detail values (14,1,'saturday');
INSERT INTO domain_detail values (15,1,'sunday');
INSERT INTO domain_detail values (16,2,'user');
INSERT INTO domain_detail values (17,2,'pass');
INSERT INTO domain_detail values (18,3,'name');
INSERT INTO domain_detail values (19,3,'description');
INSERT INTO domain_detail values (20,3,'price');
INSERT INTO domain_detail values (21,3,'rating');
INSERT INTO domain_detail values (22,3,'image');



create TABLE domain_value_detail(
domin_value_detail_id INT NOT NULL AUTO_INCREMENT,
domin_detail_id INT NOT NULL ,
domin_id INT NOT NULL,
domain_value_detail_name VARCHAR(100),
domain_detail_name VARCHAR(100),
PRIMARY KEY (domin_value_detail_id),
FOREIGN KEY (domin_detail_id) REFERENCES domain_detail(domin_detail_id)
                      ON DELETE CASCADE
);

INSERT INTO domain_value_detail values (1,1,1,'12345','train_no');
INSERT INTO domain_value_detail values (2,2,1,'Darjeeling Mail','train_name');
INSERT INTO domain_value_detail values (3,3,1,'Howrah','origin');
INSERT INTO domain_value_detail values (4,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (5,5,1,'10:40 pm','deperture');
INSERT INTO domain_value_detail values (6,6,1,'8:00 am','arrival');
INSERT INTO domain_value_detail values (7,7,1,'10 hrs','travel_time');
INSERT INTO domain_value_detail values (8,8,1,'16','days_of_run');
INSERT INTO domain_value_detail values (9,9,1,'Y','monday');
INSERT INTO domain_value_detail values (10,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (11,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (12,12,1,'Y','thursday');
INSERT INTO domain_value_detail values (13,13,1,'Y','friday');
INSERT INTO domain_value_detail values (14,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (15,15,1,'Y','sunday');

INSERT INTO domain_value_detail values (16,1,1,'12345','train_no');
INSERT INTO domain_value_detail values (17,2,1,'Kanchanjangha Mail','train_name');
INSERT INTO domain_value_detail values (18,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (19,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (20,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (21,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (22,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (23,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (24,9,1,'Y','monday');
INSERT INTO domain_value_detail values (25,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (26,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (27,12,1,'N','thursday');
INSERT INTO domain_value_detail values (28,13,1,'N','friday');
INSERT INTO domain_value_detail values (29,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (30,15,1,'Y','sunday');

//user details

INSERT INTO domain_value_detail values (31,16,2,'admin','user');
INSERT INTO domain_value_detail values (32,17,2,'admin','pass');
//Product
INSERT INTO domain_value_detail values (33,18,3,'Panasonic 81cm (32) HD Ready LED TV','name');
INSERT INTO domain_value_detail values (34,19,3,'HD Ready | 1366 x 768 Resolution, 176(H)-degree Viewing Angle-16 W Speaker Output','description');
INSERT INTO domain_value_detail values (35,20,3,'16,490','price');
INSERT INTO domain_value_detail values (36,21,3,'4.5','rating');
INSERT INTO domain_value_detail values (37,22,3,'image','imageName.jpg');

INSERT INTO domain_value_detail values (38,18,3,'Vu 80cm (32) HD Ready LED TV','name');
INSERT INTO domain_value_detail values (39,19,3,'HD Ready | 1366 x 768 Resolution-178(H)-degree Viewing Angle-12-W Speaker Output','description');
INSERT INTO domain_value_detail values (40,20,3,'13,990','price');
INSERT INTO domain_value_detail values (41,21,3,'4.3','rating');
INSERT INTO domain_value_detail values (42,22,3,'image','imageName.jpg');

INSERT INTO domain_value_detail values (43,18,3,'BPL Vivid 80cm (32) HD Ready LED TV','name');
INSERT INTO domain_value_detail values (44,19,3,'HD Ready | 1366 x 768 Resolution-175(H)/165(V) degree Viewing Angle-116 W Speaker Output','description');
INSERT INTO domain_value_detail values (45,20,3,'14,990','price');
INSERT INTO domain_value_detail values (46,21,3,'4.9','rating');
INSERT INTO domain_value_detail values (47,22,3,'image','imageName.jpg');

//train list
INSERT INTO domain_value_detail values (48,1,1,'13147','train_no');
INSERT INTO domain_value_detail values (49,2,1,'UTTAR BANGA EXP','train_name');
INSERT INTO domain_value_detail values (50,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (51,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (52,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (53,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (54,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (55,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (56,9,1,'Y','monday');
INSERT INTO domain_value_detail values (57,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (58,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (59,12,1,'N','thursday');
INSERT INTO domain_value_detail values (60,13,1,'N','friday');
INSERT INTO domain_value_detail values (61,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (62,15,1,'Y','sunday');

INSERT INTO domain_value_detail values (63,1,1,'12377','train_no');
INSERT INTO domain_value_detail values (64,2,1,'PADATIK EXPRESS','train_name');
INSERT INTO domain_value_detail values (65,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (66,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (67,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (68,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (69,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (70,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (71,9,1,'Y','monday');
INSERT INTO domain_value_detail values (72,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (73,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (74,12,1,'N','thursday');
INSERT INTO domain_value_detail values (75,13,1,'N','friday');
INSERT INTO domain_value_detail values (76,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (77,15,1,'Y','sunday');

INSERT INTO domain_value_detail values (78,1,1,'15959','train_no');
INSERT INTO domain_value_detail values (79,2,1,'KAMRUP EXPRESS','train_name');
INSERT INTO domain_value_detail values (80,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (81,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (82,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (83,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (84,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (85,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (86,9,1,'Y','monday');
INSERT INTO domain_value_detail values (87,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (88,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (89,12,1,'N','thursday');
INSERT INTO domain_value_detail values (90,13,1,'N','friday');
INSERT INTO domain_value_detail values (91,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (92,15,1,'Y','sunday');

INSERT INTO domain_value_detail values (93,1,1,'12345','train_no');
INSERT INTO domain_value_detail values (94,2,1,'Kanchanjangha Mail','train_name');
INSERT INTO domain_value_detail values (95,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (96,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (97,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (98,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (99,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (100,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (101,9,1,'Y','monday');
INSERT INTO domain_value_detail values (102,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (103,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (104,12,1,'N','thursday');
INSERT INTO domain_value_detail values (105,13,1,'N','friday');
INSERT INTO domain_value_detail values (106,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (107,15,1,'Y','sunday');

INSERT INTO domain_value_detail values (108,1,1,'12345','train_no');
INSERT INTO domain_value_detail values (109,2,1,'SARAIGHAT EXP','train_name');
INSERT INTO domain_value_detail values (110,3,1,'Sealdah','origin');
INSERT INTO domain_value_detail values (111,4,1,'New Jalpaiguri','destination');
INSERT INTO domain_value_detail values (112,5,1,'10:40 am','deperture');
INSERT INTO domain_value_detail values (113,6,1,'8:00 pm','arrival');
INSERT INTO domain_value_detail values (114,7,1,'19 hrs','travel_time');
INSERT INTO domain_value_detail values (115,8,1,'18','days_of_run');
INSERT INTO domain_value_detail values (116,9,1,'Y','monday');
INSERT INTO domain_value_detail values (117,10,1,'Y','tuesday');
INSERT INTO domain_value_detail values (118,11,1,'Y','wednesday');
INSERT INTO domain_value_detail values (119,12,1,'N','thursday');
INSERT INTO domain_value_detail values (120,13,1,'N','friday');
INSERT INTO domain_value_detail values (121,14,1,'Y','saturday');
INSERT INTO domain_value_detail values (122,15,1,'Y','sunday');
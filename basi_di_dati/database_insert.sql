INSERT INTO base_emp VALUES
('NGNRRT02M29H931J',  'Roberto',         'Ingenito',     'ingenitoroby@gmail.com',             'password',    '29/08/2002',  'manager',  'developer',             10000  ),
('SQNLNZ02R08L259U',  'Lorenzo',         'Sequino',      'sequinolorenzo1@gmail.com',          'lorenzo123',  '08/10/2002',  'junior',   'project manager',       12000  ),
('CLNCRN73M48F839M',  'Caterina',        'Calandruccio', 'caterina.calandruccio89@libero.it',  'password',    '20/02/1989',  'middle',   'program manager',       8750   ),
('CPCPPL79C15I119Z',  'Pierpaolo',       'Capoccia',     'pierpaolocapoccia23@alice.it',       'password',    '15/04/1960',  'senior',   'App Developer',         14789  ),
('DSTGLN69T15C352D',  'Giuliano',        'De Stefani',   'destefanigiuliano9@hotmail.it',      'password',    '14/05/1987',  'senior',   'developer',             12456  ),
('MCNMHL78M41H501R',  'Michela',         'Miconi',       'miconimichela456@gmail.com',         'password',    '15/08/1996',  'senior',   'developer',             8000   ),
('RSNDNL83H22H501Q',  'Daniele',         'Orsini',       'orsinidani@gmail.com',               'password',    '20/03/1994',  'senior',   'IT architect',          9000   ),
('PTRRLL60A43F880S',  'Rosella',         'Petroni',      'rosellapetroni@libero.it',           'password',    '14/06/1960',  'junior',   'App Developer',         4000   ),
('RSSNTN70C07L419D',  'Antonio',         'Russo',        'antoniorusso@gmail.com',             'password',    '15/09/1987',  'senior',   'Security manager',      4500   ),
('CRRSVT56B27F399B',  'Salvatore',       'Carrino',      'salvatorecarrino@gmail.com',         'password',    '14/07/1998',  'junior',   'Web developer',         15000  ),
('DNDMRS64P60F399G',  'Maura Rosaria',   'Donadio',      'maurarosaria@gmail.com',             'password',    '20/09/1964',  'senior',   'developer',             15456  ),
('DNDNCL76L24G786K',  'Nicola',          'Donadio',      'donadionicol@libero.it',             'password',    '24/07/1976',  'middle',   'Security manager',      12365  ),
('TCCDNC73D25F052A',  'Domenico Santo',  'Tucci',        'domenicotucci@gmail.com',            'password',    '25/04/1973',  'middle',   'E-commerce manager ',   14000  ),
('MNTNNR66M15F052I',  'Antonio Rocco',   'Montesano',    'roccomontesano@alice.it',            'password',    '15/08/1996',  'junior',   'Call center manager',   15789  ),
('DNDSVT65T22L477D',  'Salvatore',       'Donadio',      'salvatoredonadio@hotmail.it',        'password',    '27/06/1969',  'junior',   'developer',             16000  ),
('FNCGLG68E05F399E',  'Gianluigi',       'Finocchiaro',  'gianluigifinocchiaro@gamil.com',     'password',    '23/08/1984',  'junior',   'Web developer',         17540  ),
('BNDNDR90E15G712A',  'Andrea',          'Benedetto',    'andreabenedetto@gmail.com',          'password',    '18/08/1987',  'middle',   'App Developer',         15123  ),
('BLLVCN72L22F052W',  'Vincenzo',        'Bellino',      'bellinoandrea123@libero.it',         'password',    '14/05/1987',  'middle',   'E-commerce manager',    19000  ),
('PZZGPP79H15G786W',  'Giuseppe',        'Pizzella',     'pizzagiuseppe@gmail.com',            'password',    '17/08/1989',  'junior',   'Web developer',         20000  ),
('SCRRCC66H16F052M',  'Vito',            'Tucci',        'vitotucci@gmail.com',                'password',    '09/05/1986',  'middle',   'developer',             15420  );

DELETE FROM career_log;
INSERT INTO career_log(ex_role,new_role,new_role_date,CF) VALUES
 ('','junior','2010-01-01','NGNRRT02M29H931J')
,('','junior','2010-01-01','SQNLNZ02R08L259U')
,('','junior','2010-01-01','CLNCRN73M48F839M')
,('','junior','2010-01-01','CPCPPL79C15I119Z')
,('','junior','2010-01-01','DSTGLN69T15C352D')
,('','junior','2010-01-01','MCNMHL78M41H501R')
,('','junior','2010-01-01','RSNDNL83H22H501Q')
,('','junior','2010-01-01','PTRRLL60A43F880S')
,('','junior','2010-01-01','RSSNTN70C07L419D')
,('','junior','2010-01-01','CRRSVT56B27F399B')
,('','junior','2010-01-01','DNDMRS64P60F399G')
,('','junior','2010-01-01','DNDNCL76L24G786K')
,('','junior','2010-01-01','TCCDNC73D25F052A')
,('','junior','2010-01-01','MNTNNR66M15F052I')
,('','junior','2010-01-01','DNDSVT65T22L477D')
,('','junior','2010-01-01','FNCGLG68E05F399E')
,('','junior','2010-01-01','BNDNDR90E15G712A')
,('','junior','2010-01-01','BLLVCN72L22F052W')
,('','junior','2010-01-01','PZZGPP79H15G786W')
,('','junior','2010-01-01','SCRRCC66H16F052M')
,('junior','middle','2013-01-01','NGNRRT02M29H931J')
,('junior','middle','2013-01-01','SQNLNZ02R08L259U')
,('junior','middle','2013-01-01','CLNCRN73M48F839M')
,('junior','middle','2013-01-01','CPCPPL79C15I119Z')
,('junior','middle','2013-01-01','DSTGLN69T15C352D')
,('junior','middle','2013-01-01','MCNMHL78M41H501R')
,('junior','middle','2013-01-01','RSNDNL83H22H501Q')
,('junior','middle','2013-01-01','PTRRLL60A43F880S')
,('junior','middle','2013-01-01','RSSNTN70C07L419D')
,('junior','middle','2013-01-01','CRRSVT56B27F399B')
,('junior','middle','2013-01-01','DNDMRS64P60F399G')
,('junior','middle','2013-01-01','DNDNCL76L24G786K')
,('junior','middle','2013-01-01','TCCDNC73D25F052A')
,('junior','middle','2013-01-01','MNTNNR66M15F052I')
,('junior','middle','2013-01-01','DNDSVT65T22L477D')
,('junior','middle','2013-01-01','FNCGLG68E05F399E')
,('middle','senior','2017-01-01','NGNRRT02M29H931J')
,('middle','senior','2017-01-01','SQNLNZ02R08L259U')
,('middle','senior','2017-01-01','CLNCRN73M48F839M')
,('middle','senior','2017-01-01','CPCPPL79C15I119Z')
,('middle','senior','2017-01-01','DSTGLN69T15C352D')
,('middle','senior','2017-01-01','MCNMHL78M41H501R')
,('middle','senior','2017-01-01','RSNDNL83H22H501Q')
,('middle','senior','2017-01-01','PTRRLL60A43F880S')
,('middle','senior','2017-01-01','RSSNTN70C07L419D')
,('middle','senior','2017-01-01','CRRSVT56B27F399B')
,('senior','manager','2020-01-01','NGNRRT02M29H931J')
,('senior','manager','2020-01-01','SQNLNZ02R08L259U')
,('senior','manager','2020-01-01','CLNCRN73M48F839M')
,('senior','manager','2020-01-01','CPCPPL79C15I119Z')
,('senior','manager','2020-01-01','DSTGLN69T15C352D')
,('senior','manager','2020-01-01','MCNMHL78M41H501R');

INSERT INTO site VALUES
(DEFAULT, 'Deloit',         'Via Nazionale',         '2',     '80059',  'Torre del Greco'),
(DEFAULT, 'Altem',          'Via Longobardi',        '15',    '80020',  'Frattaminore'),
(DEFAULT, 'Scintilloro',    'Via Leopardi',          '20',    '80020',  'Crispano'),
(DEFAULT, 'Lys & Sol',      'Via Alcide de Gasperi', '11',    '80058',  'Torre Annunziata'),
(DEFAULT, 'Via Plinio',     'Via Roma',              '23',    '80056',  'Ercolano'),
(DEFAULT, 'Sole & Sirio',   'Via Scala',             '32',    '80045',  'Pompei');


INSERT INTO laboratory(lab_code,lab_name,topic,site_number,cf_scientific_manager) VALUES
 (DEFAULT,'Bioinformatica','studio dell''informatica applicata alla biologia',1,'RSNDNL83H22H501Q')
,(DEFAULT,'Biochimica applicata','studio delle reazioni chimiche complesse che avvengo all''interno degli esseri viventi',1,'RSNDNL83H22H501Q')
,(DEFAULT,'Biologia Animale','studio degli organismi animali e della loro compatibilità con impianti neurali ad alta tecnologia',1,'RSNDNL83H22H501Q')
,(DEFAULT,'Machine Learning','creazione di sistemi e modelli che imitano l''intelligenza umana',1,'PTRRLL60A43F880S')
,(DEFAULT,'Intelligenza Artificiale','studio e creazione di intelligenze artificiali in grado di interfacciarsi con il sistema aziendale',1,'RSSNTN70C07L419D')
,(DEFAULT,'Quantum Computing','creazione di nuovi processori sperimentali con l''utilizzo della teoria quantistica',2,'PTRRLL60A43F880S')
,(DEFAULT,'Fisica delle nanoparticelle','studio delle nanoparticelle e i suoi campi di applicazione',2,'RSSNTN70C07L419D')
,(DEFAULT,'Sperimentazione e Collaudo','sperimentazione e collaudo di nuove attrezzature sviluppate',2,'RSNDNL83H22H501Q')
,(DEFAULT,'Ricerca Energie Rinnovabili','studio e ricerca di nuove energie rinnovabili da utilizzare',2,'RSSNTN70C07L419D')
,(DEFAULT,'Architettura dei Microprocessori','creazione di nuovi microprocessori all''avanguardia',2,'RSNDNL83H22H501Q')
,(DEFAULT,'Ricerca Medica Sperimentale','studio della medicina sperimentale e attrezzature mediche',3,'CRRSVT56B27F399B')
,(DEFAULT,'Calcolo Probabilistico','ricerca e studio delle probabilità',3,'PTRRLL60A43F880S')
,(DEFAULT,'Algoritmi Sperimentali','ricerca di nuovi algoritmi per lo sviluppo di tecnologie avanzate',3,'CRRSVT56B27F399B')
,(DEFAULT,'Crittografia e Crittoanalisi','studio dei sistemi di crittografia e algoritmi di crittografia avanzati',3,'RSNDNL83H22H501Q')
,(DEFAULT,'Creazione di Sistemi Informativi','creazione di sistemi informativi aziendali',3,'CRRSVT56B27F399B')
,(DEFAULT,'Conversione Analogico-Digitale','studio delle apparecchiature informatiche',4,'RSSNTN70C07L419D')
,(DEFAULT,'Neurotecnologie Avanzate','ricerca di metodi sperimentali per l''impianto di interfacce aptiche avanzate',4,'RSNDNL83H22H501Q')
,(DEFAULT,'Analisi matematica Comutazionale','studio e analisi matematiche',4,'CRRSVT56B27F399B')
,(DEFAULT,'Ricerca Operativa e ottimizzazione','studio e ricerca operativa',4,'PTRRLL60A43F880S')
,(DEFAULT,'Meccanica dei Computer','studio della meccanica dei computer',5,'RSNDNL83H22H501Q')
,(DEFAULT,'Fisica dei Computer','studio della fisica dei computer',6,'CRRSVT56B27F399B');

INSERT INTO project(CUP,funds,name,description,start_date,end_date,deadline,cf_manager,cf_scientific_referent) VALUES
 ('aaaa-0000000000',10000,'Emobility','gestione di colonnine e ricarica pubblica','2020-02-01',NULL,'2022-02-01','NGNRRT02M29H931J','RSNDNL83H22H501Q')
,('aaaa-0000000001',20000,'Lab 32','laboratorio di idee e azioni','2020-02-02',NULL,'2022-05-01','NGNRRT02M29H931J','PTRRLL60A43F880S')
,('aaaa-0000000002',10500,'Magazzini Arte','libri per arte scultorea','2020-02-03',NULL,NULL,'SQNLNZ02R08L259U','RSSNTN70C07L419D')
,('aaaa-0000000003',5000,'Anaconda','studio animali selvatici','2020-02-04',NULL,NULL,'CLNCRN73M48F839M','RSNDNL83H22H501Q')
,('aaaa-0000000004',8000,'Uncas','storia inglese','2020-02-05','2022-02-01','2021-12-01','NGNRRT02M29H931J','RSSNTN70C07L419D')
,('aaaa-0000000005',500,'Pigreco 3.14','ricerca numero magico','2020-02-06','2022-02-01',NULL,'SQNLNZ02R08L259U','PTRRLL60A43F880S')
,('aaaa-0000000006',50000,'Mach 4','start up scarpe','2020-02-07','2022-02-01','2022-04-01','SQNLNZ02R08L259U','RSNDNL83H22H501Q')
,('aaaa-0000000007',3000,'BLQ','aereoporto di bologna','2020-02-08',NULL,NULL,'CPCPPL79C15I119Z','CRRSVT56B27F399B')
,('aaaa-0000000008',12000,'Mergellina Lab','storia mozzarella napoletana','2020-02-09',NULL,'2022-11-01','CPCPPL79C15I119Z','RSNDNL83H22H501Q')
,('aaaa-0000000009',63000,'Eliticas','bicicletta ellittica','2020-02-10',NULL,NULL,'DSTGLN69T15C352D','CRRSVT56B27F399B')
,('aaaa-0000000010',7523,'Noidiqua','storia locale','2020-02-11','2022-02-01',NULL,'CLNCRN73M48F839M','CRRSVT56B27F399B');


INSERT INTO take_part(start_date,end_date,CUP,lab_code) VALUES
 ('2020-02-01','2020-05-01','aaaa-0000000000',1)
,('2020-08-06',NULL,'aaaa-0000000000',1)
,('2020-02-02',NULL,'aaaa-0000000001',1)
,('2020-02-03',NULL,'aaaa-0000000002',1)
,('2020-02-07','2022-02-01','aaaa-0000000006',1)
,('2020-02-01',NULL,'aaaa-0000000000',2)
,('2020-02-07','2021-12-16','aaaa-0000000006',2)
,('2020-02-04',NULL,'aaaa-0000000003',2)
,('2020-02-11','2022-02-01','aaaa-0000000010',2)
,('2020-02-09','2020-06-23','aaaa-0000000008',2)
,('2020-02-10','2020-03-14','aaaa-0000000009',2)
,('2020-02-05','2022-02-01','aaaa-0000000004',3)
,('2020-02-06','2022-02-01','aaaa-0000000005',3)
,('2020-02-05','2022-02-01','aaaa-0000000004',4)
,('2020-02-06','2022-02-01','aaaa-0000000005',4)
,('2020-02-05','2022-02-01','aaaa-0000000004',5)
,('2020-02-06','2022-02-01','aaaa-0000000005',5)
,('2020-02-08',NULL,'aaaa-0000000007',6)
,('2020-02-09',NULL,'aaaa-0000000008',6)
,('2020-02-09',NULL,'aaaa-0000000008',7)
,('2020-02-10',NULL,'aaaa-0000000009',7)
,('2020-02-09',NULL,'aaaa-0000000008',8)
,('2020-02-10',NULL,'aaaa-0000000009',8)
,('2020-02-11','2022-02-01','aaaa-0000000010',8)
,('2020-02-02',NULL,'aaaa-0000000001',9)
,('2020-02-03',NULL,'aaaa-0000000002',9)
,('2020-02-05','2022-02-01','aaaa-0000000004',10)
,('2020-02-06','2022-02-01','aaaa-0000000005',11)
,('2020-02-07','2022-02-01','aaaa-0000000006',12)
,('2020-02-08','2021-01-07','aaaa-0000000007',13)
,('2020-02-09','2021-01-07','aaaa-0000000008',13)
,('2020-02-08','2021-01-07','aaaa-0000000007',14)
,('2020-02-09','2021-01-07','aaaa-0000000008',14);

INSERT INTO project_salaried VALUES
('BBBJSC90L54C352E',  'Bruna',      'Lollobrigida',  'bruna.lollo@gmail.com',         'brun123',        'ingegnere',      '01/01/1960'  ),
('BBNNMR97R70B774R',  'Maria',      'Briga',         'maria.briga@gmail.com',         'maribriga',      'sviluppatore',   '15/04/1961'  ),
('BBRGLC96H16D122N',  'Sara',       'Fortunato',     'sara.fort@gmail.com',           'sarafort',       'programmatore',  '20/08/1958'  ),
('BBRLIA96M41C349S',  'Francesca',  'Loffredo',      'francesca.loffredo@gmail.com',  'francloffredo',  'ingegnere',      '08/10/2002'  ),
('CHRGNR98D28M208A',  'Antonino',   'Chiaro',        'anto.chiaro@libero.it',         'anto124!',       'programmatore',  '20/04/1958'  ),
('CLAMNL96P55D122O',  'Attilio',    'Vello',         'attilio.vello2@gmail.com',      'attli32?',       'sviluppatore',   '20/08/2003'  ),
('CHRRSO95C43D988A',  'Luigi',      'Guida',         'luig.guida@gmail.com',          'guida1!',        'ingegnere',      '14/08/1994'  ),
('CLLNMO97B61F537H',  'Ciro',       'Guida',         'ciro.guida@gmail.com',          'sarafort',       'programmatore',  '20/08/1958'  ),
('CLBDNC81M15A773U',  'Chiara',     'Formicola',     'chiara.formicola@gmail.com',    'sarafort2',      'ingegnere',      '20/08/1957'  );

INSERT INTO works_on(hire_date,expiration,CUP,CF,pay) VALUES
 ('2020-02-01','2020-05-01','aaaa-0000000000','BBBJSC90L54C352E',2315)
,('2020-04-11','2021-04-07','aaaa-0000000001','BBBJSC90L54C352E',2124)
,('2020-08-06','2021-02-01','aaaa-0000000004','BBNNMR97R70B774R',1500)
,('2020-02-02','2021-02-01','aaaa-0000000001','BBRGLC96H16D122N',800)
,('2020-02-03','2021-02-01','aaaa-0000000002','BBRLIA96M41C349S',1200)
,('2020-02-07','2022-02-01','aaaa-0000000006','CHRGNR98D28M208A',1500)
,('2020-02-07','2021-12-16','aaaa-0000000006','CLAMNL96P55D122O',1500)
,('2020-02-03','2021-02-01','aaaa-0000000002','CLAMNL96P55D122O',1234)
,('2020-02-08','2021-07-27','aaaa-0000000007','CHRRSO95C43D988A',1245)
,('2020-02-04','2021-02-01','aaaa-0000000003','CLLNMO97B61F537H',1234)
,('2020-02-11','2022-02-01','aaaa-0000000010','CLBDNC81M15A773U',1234);

INSERT INTO works_at(start_date,end_date,lab_code,cf_base_emp) VALUES
 ('2021-01-01',NULL,1,'BNDNDR90E15G712A')
,('2021-01-02',NULL,2,'BLLVCN72L22F052W')
,('2021-01-03',NULL,3,'PZZGPP79H15G786W')
,('2021-01-04',NULL,4,'SCRRCC66H16F052M')
,('2021-01-05',NULL,5,'DNDMRS64P60F399G')
,('2021-01-06',NULL,6,'DNDNCL76L24G786K')
,('2021-01-07',NULL,7,'TCCDNC73D25F052A')
,('2021-01-08',NULL,8,'MNTNNR66M15F052I')
,('2021-01-09',NULL,9,'DNDSVT65T22L477D')
,('2021-01-10',NULL,10,'FNCGLG68E05F399E')
,('2021-01-11',NULL,11,'RSNDNL83H22H501Q')
,('2021-01-12',NULL,12,'PTRRLL60A43F880S')
,('2021-01-13',NULL,13,'RSSNTN70C07L419D')
,('2021-01-14',NULL,14,'CRRSVT56B27F399B')
,('2021-01-15',NULL,15,'NGNRRT02M29H931J')
,('2021-01-16',NULL,16,'SQNLNZ02R08L259U')
,('2021-01-17',NULL,17,'CLNCRN73M48F839M')
,('2021-01-18',NULL,18,'CPCPPL79C15I119Z')
,('2021-01-19',NULL,19,'DSTGLN69T15C352D')
,('2021-01-20',NULL,20,'MCNMHL78M41H501R');

INSERT INTO equipment_request(name,specs,quantity,CUP,lab_code,type,code) VALUES
 ('Macbook m2','16 GB ram , 512 archiviaizione',10,'aaaa-0000000000',1,'computer','9b790ec8-37c1-40b9-b3cd-66059a845b2a')
,('Rifrattometro Digitale','dimensione : 55x109x31',3,'aaaa-0000000000',1,'strumentazione','fd0fab86-d339-4e60-b4e3-9919c6e34e23')
,('Macbook m1','8 GB ram , 256 archiviazione',4,'aaaa-0000000000',2,'computer','500b0f9a-3186-4b4c-a63a-c7451e5cb6da')
,('Dinamometro Digitale','portata : 0-15kg',5,'aaaa-0000000003',2,'strumentazione','271119ba-6f7f-4013-8c27-684ff793458d')
,('Braccio ergonomico per PC','peso : 10kg , apertura massima 95°',10,'aaaa-0000000003',2,'strumentazione','d37002d2-ddbb-4701-aa3c-74bf42b0980a')
,('Archiviazione di massa','capienza : 3 TB',20,'aaaa-0000000007',6,'archiviazione','f945feba-f9b5-4ec4-bbc2-5e87040cc1b2')
,('Fascette cavi','dimensione : 20x5 mm',100,'aaaa-0000000007',6,'strumentazione','45e1704c-7516-4ed5-b79d-7029db656e87')
,('Cavo di rete','lunghezza : 10 m',5,'aaaa-0000000007',6,'strumentazione','ffbc139d-5bf6-425b-b731-48cdf7ae2759')
,('Macchinetta del caffè','cialde , possibilità di fare ginseng',1,'aaaa-0000000008',6,'macchinetta','081b7df7-e1ff-4fff-99e4-d53c2fdc5c5b')
,('Scheda di rete','wifi 6.0',3,'aaaa-0000000008',7,'strumentazione','09e98d95-be48-4b53-80e2-18b18b1d8649')
,('Computer fisso','processore : i9 11900k',5,'aaaa-0000000009',7,'computer','f3012487-8863-41fb-a49d-ba0e04a5200b')
,('Sedia ergonomica','peso : 100 Kg',10,'aaaa-0000000008',8,'sedia','8370f19b-169d-48d0-8a87-da5e3c2f56fb');

CALL buy_equipment('fd0fab86-d339-4e60-b4e3-9919c6e34e23', 200);
CALL buy_equipment('500b0f9a-3186-4b4c-a63a-c7451e5cb6da', 1000);
CALL buy_equipment('271119ba-6f7f-4013-8c27-684ff793458d', 250);
CALL buy_equipment('45e1704c-7516-4ed5-b79d-7029db656e87', 0.10);
CALL buy_equipment('ffbc139d-5bf6-425b-b731-48cdf7ae2759', 10);
CALL buy_equipment('081b7df7-e1ff-4fff-99e4-d53c2fdc5c5b', 500);
CALL buy_equipment('09e98d95-be48-4b53-80e2-18b18b1d8649', 30);
CALL buy_equipment('f3012487-8863-41fb-a49d-ba0e04a5200b', 2000);
CALL buy_equipment('8370f19b-169d-48d0-8a87-da5e3c2f56fb', 150);
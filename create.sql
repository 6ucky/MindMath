create sequence hibernate_sequence start with 1 increment by 1

    create table ContentErrorType (
       id bigint not null,
        content_url varchar(255),
        erreur_type varchar(255),
        format varchar(255),
        glossaire varchar(255),
        primary key (id)
    )

    create table FeedbackContent (
       id bigint not null,
        feedbackName varchar(255),
        motivation_leaf varchar(255),
        ponderation double not null,
        primary key (id)
    )

    create table FeedbackContent_contents (
       FeedbackContent_id bigint not null,
        contents_id bigint not null
    )

    create table Glossaire (
       id bigint not null,
        glossaire_content varchar(255),
        glossaire_name varchar(255),
        primary key (id)
    )

    create table Log (
       id bigint not null,
        action varchar(255),
        name varchar(255),
        time varchar(255),
        type varchar(255),
        primary key (id)
    )

    create table Motivation (
       id bigint not null,
        activityMode varchar(255),
        motivation_data varchar(255),
        motivation_leaf varchar(255),
        primary key (id)
    )

    create table Params (
       id bigint not null,
        VT_2_1 varchar(255),
        VT_2_2 varchar(255),
        VT_2_3 varchar(255),
        VT_2_4 varchar(255),
        primary key (id)
    )

    create table Sensors (
       id bigint not null,
        activityMode varchar(255),
        capteur_bool_RF_CO2_1 varchar(255),
        capteur_bool_RF_CO2_2 varchar(255),
        capteur_bool_RF_CO2_3 varchar(255),
        capteur_bool_RJ varchar(255),
        capteur_nb_aide varchar(255),
        capteur_nb_effacer varchar(255),
        capteur_nb_temps varchar(255),
        capteur_nb_valider varchar(255),
        codeError varchar(255),
        correctAnswer varchar(255),
        domain varchar(255),
        generator varchar(255),
        taskFamily varchar(255),
        primary key (id)
    )

    create table Task (
       id bigint not null,
        feedback_id varchar(255),
        id_learner varchar(255),
        task varchar(255),
        trigger varchar(255),
        params_id bigint,
        sensors_id bigint,
        primary key (id)
    )

    create table Task_logs (
       Task_id bigint not null,
        logs_id bigint not null
    )

    alter table FeedbackContent_contents 
       add constraint UK_a6pgd0mbr9c5528jain8d363d unique (contents_id)

    alter table Task_logs 
       add constraint UK_4rsbqsnfkgkeq8b11yuw2m2gg unique (logs_id)

    alter table FeedbackContent_contents 
       add constraint FKcyllmvbqp21ihn4skih0303gg 
       foreign key (contents_id) 
       references ContentErrorType

    alter table FeedbackContent_contents 
       add constraint FKt82jhh765ioxh8khlyuffow4r 
       foreign key (FeedbackContent_id) 
       references FeedbackContent

    alter table Task 
       add constraint FKsn8c2g3gqmr4t6v9rleni2lr9 
       foreign key (params_id) 
       references Params

    alter table Task 
       add constraint FKey7c4juxh14egwhh1tikisyjt 
       foreign key (sensors_id) 
       references Sensors

    alter table Task_logs 
       add constraint FKjs4sofvod609c23t5vtcwodl7 
       foreign key (logs_id) 
       references Log

    alter table Task_logs 
       add constraint FK6a9r72r8akpykgb4m5bg6q3r6 
       foreign key (Task_id) 
       references Task

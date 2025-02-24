CREATE TABLE IF NOT EXISTS "users"(
    "login" VARCHAR(35) PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "password" VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS "groups" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "user_group_roles" (
    "group_id" INTEGER NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,
    "role" VARCHAR(50) NOT NULL,
    PRIMARY KEY ("group_id", "user_login"),

    CONSTRAINT "FK_user_group_roles_group_id"
      FOREIGN KEY ("group_id")
          REFERENCES "groups"("id") ON DELETE CASCADE,

    CONSTRAINT "FK_user_group_roles_user_login"
      FOREIGN KEY ("user_login")
          REFERENCES "users"("login") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "tests" (
    "uuid_training" TEXT PRIMARY KEY,
    "name" TEXT NOT NULL,
    "creator_login" VARCHAR(35) NOT NULL,
    "uuid_monitoring" TEXT,
    "points" INTEGER,
    "group_id" INTEGER,
--     "folder_id" INTEGER,

    CONSTRAINT "FK_tests_creator_login"
       FOREIGN KEY ("creator_login")
           REFERENCES "users"("login"),

    CONSTRAINT "FK_tests_group_id"
       FOREIGN KEY ("group_id")
           REFERENCES "groups"("id")
);

CREATE TABLE IF NOT EXISTS "folders" (
     "id" SERIAL PRIMARY KEY,
     "name" VARCHAR(50) NOT NULL,
     "user_login" VARCHAR(35) NOT NULL,
     "parent_folder_id" INTEGER,

     CONSTRAINT "FK_folders_user_login"
         FOREIGN KEY ("user_login")
             REFERENCES "users"("login"),

     CONSTRAINT "FK_folders_parent_folder_id"
         FOREIGN KEY ("parent_folder_id")
             REFERENCES "folders"("id")
);

CREATE TABLE IF NOT EXISTS "questions" (
    "id" SERIAL PRIMARY KEY,
    "test_id" TEXT NOT NULL,
    "question" TEXT NOT NULL,
    "points" INTEGER,
    "type" TEXT NOT NULL,

    CONSTRAINT "FK_questions_test_id"
       FOREIGN KEY ("test_id")
           REFERENCES "tests"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "folder_tests" (
    "test_id" TEXT NOT NULL,
    "folder_id" INTEGER NOT NULL,
    PRIMARY KEY ("test_id", "folder_id"),

    CONSTRAINT "FK_folder_tests_test_id"
      FOREIGN KEY ("test_id")
          REFERENCES "tests"("uuid_training"),

    CONSTRAINT "FK_folder_tests_folder_id"
      FOREIGN KEY ("folder_id")
          REFERENCES "folders"("id")
);

CREATE TABLE IF NOT EXISTS "answer_options" (
    "id" SERIAL PRIMARY KEY,
    "question_id" INTEGER NOT NULL,
    "option" TEXT NOT NULL,
    "correct" BOOLEAN,

    CONSTRAINT "FK_answer_options_question_id"
        FOREIGN KEY ("question_id")
            REFERENCES "questions"("id")
);

CREATE TABLE IF NOT EXISTS "access_to_tests" (
    "id" SERIAL PRIMARY KEY,
    "folder_id" INTEGER,
    "test_id" TEXT NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,

    CONSTRAINT "FK_access_to_tests_folder_id"
        FOREIGN KEY ("folder_id")
            REFERENCES "folders"("id"),

    CONSTRAINT "FK_access_to_tests_user_login"
     FOREIGN KEY ("user_login")
         REFERENCES "users"("login"),

    CONSTRAINT "FK_access_to_tests_test_id"
     FOREIGN KEY ("test_id")
         REFERENCES "tests"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "results" (
     "id" SERIAL PRIMARY KEY,
     "test_id" TEXT NOT NULL,
     "user_login" VARCHAR(35) NOT NULL,
     "start_time" TIMESTAMP NOT NULL,
     "end_time" TIMESTAMP,
     "test_time" TIMESTAMP,
    "total_points" double precision,
    "status" VARCHAR(20),

     CONSTRAINT "FK_results_user_login"
         FOREIGN KEY ("user_login")
             REFERENCES "users"("login"),

     CONSTRAINT "FK_results_test_id"
         FOREIGN KEY ("test_id")
             REFERENCES "tests"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "answers" (
    "id" SERIAL PRIMARY KEY,
    "question_id" INTEGER NOT NULL,
    "test_results_id" INTEGER NOT NULL,
    "user_answer" TEXT,
    "points" INTEGER,

    CONSTRAINT "FK_answers_test_results_id"
     FOREIGN KEY ("test_results_id")
         REFERENCES "results"("id"),

    CONSTRAINT "FK_answers_question_id"
     FOREIGN KEY ("question_id")
         REFERENCES "questions"("id")
);

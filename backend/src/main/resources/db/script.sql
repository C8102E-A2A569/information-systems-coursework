CREATE TABLE IF NOT EXISTS "users"(
    "login" VARCHAR(35) PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "password" VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Groups" (
    "id" SERIAL PRIMARY KEY,
    "role" VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Test" (
    "uuid_training" TEXT PRIMARY KEY,
    "name" TEXT NOT NULL,
    "creator_login" VARCHAR(35) NOT NULL,
    "uuid_monitoring" TEXT,
    "points" INTEGER,
    "group_id" INTEGER,

    CONSTRAINT "FK_Test.creator_login"
      FOREIGN KEY ("creator_login")
          REFERENCES "User"("login"),

    CONSTRAINT "FK_Test.group_id"
      FOREIGN KEY ("group_id")
          REFERENCES "Groups"("id")
);

CREATE TABLE IF NOT EXISTS "Folder" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,
    "parent_folder_id" INTEGER,

    CONSTRAINT "FK_Folder.user_login"
        FOREIGN KEY ("user_login")
            REFERENCES "User"("login"),

    CONSTRAINT "FK_Folder.parent_folder_id"
        FOREIGN KEY ("parent_folder_id")
            REFERENCES "Folder"("id")
);

CREATE TABLE IF NOT EXISTS "Questions" (
    "id" SERIAL PRIMARY KEY,
    "test_id" TEXT NOT NULL,
    "question" TEXT NOT NULL,
    "points" INTEGER,
    "type" TEXT NOT NULL,

    CONSTRAINT "FK_Questions.test_id"
       FOREIGN KEY ("test_id")
           REFERENCES "Test"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "FolderTest" (
    "test_id" TEXT NOT NULL,
    "folder_id" INTEGER NOT NULL,
    PRIMARY KEY ("test_id", "folder_id"),

    CONSTRAINT "FK_FolderTest.test_id"
        FOREIGN KEY ("test_id")
            REFERENCES "Test"("uuid_training"),

    CONSTRAINT "FK_FolderTest.folder_id"
        FOREIGN KEY ("folder_id")
            REFERENCES "Folder"("id")
);

CREATE TABLE IF NOT EXISTS "Answer_options" (
    "id" SERIAL PRIMARY KEY,
    "question_id" INTEGER NOT NULL,
    "option" TEXT NOT NULL,
    "correct" BOOLEAN,

    CONSTRAINT "FK_Answer_options.question_id"
        FOREIGN KEY ("question_id")
            REFERENCES "Questions"("id")
);


CREATE TABLE IF NOT EXISTS "AccessToTests" (
    "test_id" TEXT NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,

    PRIMARY KEY ("test_id", "user_login"),

    CONSTRAINT "FK_AccessToTests.user_login"
       FOREIGN KEY ("user_login")
           REFERENCES "User"("login"),

    CONSTRAINT "FK_AccessToTests.test_id"
       FOREIGN KEY ("test_id")
           REFERENCES "Test"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "Results" (
    "id" SERIAL PRIMARY KEY,
    "test_id" TEXT NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,
    "start_time" TIMESTAMP NOT NULL,
    "end_time" TIMESTAMP,
    "test_time" TIMESTAMP,

    CONSTRAINT "FK_Results.user_login"
     FOREIGN KEY ("user_login")
         REFERENCES "User"("login"),

    CONSTRAINT "FK_Results.test_id"
     FOREIGN KEY ("test_id")
         REFERENCES "Test"("uuid_training")
);

CREATE TABLE IF NOT EXISTS "Answers" (
     "id" SERIAL PRIMARY KEY,
     "question_id" INTEGER NOT NULL,
     "test_results_id" INTEGER NOT NULL,
     "user_answer" TEXT,
     "points" INTEGER,

     CONSTRAINT "FK_Answers.test_results_id"
         FOREIGN KEY ("test_results_id")
             REFERENCES "Results"("id"),

     CONSTRAINT "FK_Answers.question_id"
         FOREIGN KEY ("question_id")
             REFERENCES "Questions"("id")
);

CREATE TABLE IF NOT EXISTS "Have_group" (
    "group_id" INTEGER NOT NULL,
    "user_login" VARCHAR(35) NOT NULL,

    PRIMARY KEY ("group_id", "user_login"),

    CONSTRAINT "FK_Have_group.group_id"
        FOREIGN KEY ("group_id")
            REFERENCES "Groups"("id"),

    CONSTRAINT "FK_Have_group.user_login"
        FOREIGN KEY ("user_login")
            REFERENCES "User"("login")
);


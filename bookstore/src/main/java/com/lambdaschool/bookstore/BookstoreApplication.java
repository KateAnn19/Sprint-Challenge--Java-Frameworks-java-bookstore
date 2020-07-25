package com.lambdaschool.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main class to start the application.
 */
@EnableJpaAuditing
@SpringBootApplication
public class BookstoreApplication
{
    /**
     * Connect to the system environment where environment variables live.
     */
    @Autowired
    private static Environment env;

    /**
     * If an environment variable is not found, set this to true
     */
    private static boolean stop = false;

    /**
     * If an application relies on an environment variable, check to make sure that environment variable is available!
     * If the environment variable is not available, you could set a default value, or as is done here, stop execution of the program
     *
     * @param envvar The system environment where environment variable live
     */
    private static void checkEnvironmentVariable(String envvar)
    {
        if (System.getenv(envvar) == null)
        {
            stop = true;
        }
    }

    /**
     * Main method to start the application.
     *
     * @param args Not used in this application.
     */
    public static void main(String[] args)
    {
        // Check to see if the environment variables exists. If they do not, stop execution of application.
        checkEnvironmentVariable("OAUTHCLIENTID");
        checkEnvironmentVariable("OAUTHCLIENTSECRET");

        if (!stop)
        {
            // so run the application!
            SpringApplication.run(BookstoreApplication.class,
                                  args);
        }
    }
}


//    1) Can you explain exception handling in your application?
//    Spring has built in error handling so we add some config and custom error handling files
//    and connect our services to the custom error handling exceptions in order to overrride the
//    default  messages
//    Can you explain your user authentication flow?
//    We have to add a bunch of authentication config files which authorize a user and then
//    authenticate the user. We are using auth2 to do our security and use environment variables in order
//    send and receive a token which is crypted

//    Can you show me your unit tests and describe how they work?
//    there are tests for the bookservice and bookcontroller. the book service uses the seed data
//    already there and book controller builds its' own data not relying on seed data
//    the tests in book controller mock up data. the tests in book service check the expected outcome
//    against the data already there

//    Can you show how you deployed your application to a cloud service with a persistent database?
//    I used Heroku to deploy my application. I had to add a new config file and some application properties
//    as well as a Heroku plugin to my POM file and postgresSql dependency. I deployed to Heroku
//    using the CLI and then connected the application in my POM file using the name of the application
//    I used in created my new Heroku application
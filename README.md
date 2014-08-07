Intern Project
==============

1. Clone the project.

2. Install Mongo (http://docs.mongodb.org/manual/installation/)

3. run command : mongo localhost:27017/Specials vehicles.js from the home directory of the project to populate your database 

    NOTE: 'mongo' in the above command is the path to your mongo client. If you have that path set as an enviroment            variable you can just run mongo. If you don't have that set you need to use the full path to the mongo client instead      of just mongo. (i.e. /Users/<username>/Documents/mongodb/mongodb-osx-x86_64-2.6.1/bin/mongo localhost:27017/Specials           vehicles.js)

4. cd into the folder in the Service with the pom.xml file (cd InterProject/Service/Specials) and run command : mvn spring-boot:run

5. Install and Android friendly IDE like Android Studio or Eclipse

6. Open up the SDK Manager in your IDE and update packages

7. Connect you phone to your computer and run the application to your phone. Your IDE should recognize your phone.

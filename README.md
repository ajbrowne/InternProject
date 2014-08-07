Intern Project
==============

1. Clone the project.

2. Install Mongo (http://docs.mongodb.org/manual/installation/)

3. run command : mongo localhost:27017/Specials vehicles.js from the home directory of the project to populate your database 

    NOTE: 'mongo' in the above command is the path to your mongo client. If you have that path set as an enviroment            variable you can just run mongo. If you don't have that set you need to use the full path to the mongo client instead      of just mongo. (i.e. /Users/<username>/Documents/mongodb/mongodb-osx-x86_64-2.6.1/bin/mongo localhost:27017/Specials           vehicles.js)

4. cd into the folder in the Service with the pom.xml file (cd InternProject/Service/Specials) and run command : mvn spring-boot:run

5. Install an Android friendly IDE like Android Studio, Intellij or Eclipse

6. Open up the SDK Manager in your IDE and update packages

7. Make sure that your computer and phone are on the same network and get your computer's IP address -> ifconfig

8. In SpecialsApp/app/src/main/assets/specials.properties, change the ip in the baseUrl to your IP address.
   In SpecialsApp/app/src/main/java/Rest/SpecialsRestClient, change the String IP to your IP address.

9. Connect you phone to your computer and run the application to your phone (it will build when run). Your IDE should recognize your phone.

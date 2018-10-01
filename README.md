mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  \
    -Dfile=/some/path/on/my/local/filesystem/felix/servicebinder/target/org.apache.felix.servicebinder-0.9.0-SNAPSHOT.jar \
    -DgroupId=org.apache.felix -DartifactId=org.apache.felix.servicebinder \
    -Dversion=0.9.0-SNAPSHOT -Dpackaging=jar \
    -DlocalRepositoryPath=${master_project}/local-maven-repo
    
    
    
    
 mvn install:install-file -Dfile=opencv/opencv-343.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=3.4.3 -Dpackaging=jar
     -DlocalRepositoryPath=${master_project}/local-maven-repo
     
     opencv/opencv-343.jar
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/oracle.sqldeveloper.jar -DgroupId=com.oracle -DartifactId=sqldeveloper -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/dbapi.jar -DgroupId=com.oracle -DartifactId=dbapi -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/dbtools-common.jar -DgroupId=com.oracle -DartifactId=dbtools-common -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/oracle.ide.jar -DgroupId=com.oracle -DartifactId=ide -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/oracle.jdeveloper.db.connection.jar -DgroupId=com.oracle -DartifactId=db-connection -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/javatools-nodeps.jar -DgroupId=com.oracle -DartifactId=nodeps -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/javatools.jar -DgroupId=com.oracle -DartifactId=javatools -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
start /wait mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=lib/org-openide-util.jar -DgroupId=com.oracle -DartifactId=openide-util -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true




<br />
<div align="center">

  <h3 align="center">Encryption/DEcryption in Hadoop</h3>

  <p align="center">
    Encryption is must when storing data obtained from IoT devices.<br><br>
    <a href="https://github.com/Kartikay304/HadoopEncyption">View Demo</a>
    ·
    <a href="https://github.com/Kartikay304/HadoopEncyption/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    ·
    <a href="https://github.com/Kartikay304/HadoopEncyption/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>


To install hadoop <br>
->install java-jdk 8.<br>
->install hadoop 3.2.3 from hadoop archive.<br>
->customize the shell environment in Linux with:: ```sudo nano .bashrc```<br><br>
-> copy and pase these in the end of the file <br>
```
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export PATH=$PATH:/usr/lib/jvm/java-8-openjdk-amd64/bin
export HADOOP_HOME=~/hadoop-3.2.3/
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
export HADOOP_MAPRED_HOME=$HADOOP_HOME
export YARN_HOME=$HADOOP_HOME
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"
export HADOOP_STREAMING=$HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.2.3.jar
export HADOOP_LOG_DIR=$HADOOP_HOME/logs
export PDSH_RCMD_TYPE=ssh
```
->open hadoop-env.sh:: sudo nano hadoop-env.sh and add this line ```JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64```<br><br>
->open core-site.xml:: ```sudo nano core-site.xml``` and add these lines<br>
```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
    <property>
        <name>hadoop.proxyuser.dataflair.groups</name <value>*</value>
    </property>
    <property>
        <name>hadoop.proxyuser.dataflair.hosts</name <value>*</value>
    </property>
    <property>
        <name>hadoop.proxyuser.server.hosts</name <value>*</value>
    </property>
   <property>
        <name>hadoop.proxyuser.server.groups</name <value>*</value>
    </property>
</configuration>
```
--> open hdfs-site.xml:: ```sudo nano hdfs-site.xml``` and these lines <br>
```
<configuration>
   <property>
         <name>dfs.replication</name><value>1</value>
   </property>
</configuration>
```
--> open mapred-site.xml:: ```sudo nano mapred-site.xml``` and these lines <br>
```
<configuration>
   <property>
       <name>mapreduce.framework.name</name> <value>yarn</value>
   </property>
   <property>
       <name>mapreduce.application.classpath</name>
       <value>$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/:$HADOOP_MAPRED_HOME/share/hadoop/mapreduce/lib/</value>
   </property>
</configuration>
```
--> open yarn-site.xml:: ```sudo nano yarn-site.xml``` and these lines <br>
```
<configuration>

<!-- Site specific YARN configuration properties -->
  <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
        <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREP END_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>
```
--> To start hadoop use ```start-all.sh```<br>
-->To stop hadoop use ```stop-all.sh```<br><br><br>

--> Open project using IntelliJ, and install maven. <br>
--> Now clean and install maven to get the jar file.<br>
--> Now First generate RSA keys with ```hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.RSA``` <br>
--> Now generate AES keys with ```hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.AES```<br>
--> To Encrypt file and store it into hadoop use 
```
hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.aesEncryptJob file:///home/ubuntu/plain_text_file.txt output_file_name_must_be_unique_for_every_use
```
--> To Decrypt file and store it into hadoop use 
```
hadoop jar target/HadoopEncryption-1.0-SNAPSHOT.jar org.hadoopEncryption.aesDecryptJob file:///home/ubuntu/cipher_text_file.txt output_file_name_must_be_unique_for_every_use
```



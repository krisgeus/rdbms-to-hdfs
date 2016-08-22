organization := "nl.krisgeus"
name := "rdbms-to-hdfs"
version := "0.1-SNAPSHOT"
 
scalaVersion := "2.10.6"
 
val sparkVersion = "1.6.0-cdh5.8.0"
val sparkTestingVersion = "1.6.1_0.4.4"
val hadoopVersion = "2.6.0-cdh5.8.0"
val hiveVersion = "1.1.0-cdh5.8.0"

resolvers ++= Seq(
    "cdhReleases" at "https://repository.cloudera.com/artifactory/cloudera-repos/"
)
 
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion excludeAll(
    ExclusionRule(organization = "javax.servlet")
  ),
  "org.apache.spark" %% "spark-sql" % sparkVersion excludeAll(
    ExclusionRule(organization = "javax.servlet")
  ),
  "org.apache.spark" %% "spark-hive" % sparkVersion excludeAll(
    ExclusionRule(organization = "javax.servlet")
    ),
  "org.apache.spark" %% "spark-mllib" % sparkVersion excludeAll(
    ExclusionRule(organization = "javax.servlet")
    ),
  "com.typesafe" % "config" % "1.2.1",
  "mysql" % "mysql-connector-java" % "6.0.3",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "com.holdenkarau" %% "spark-testing-base" % sparkTestingVersion % Test excludeAll(
    ExclusionRule(organization = "org.apache.hadoop"),
    ExclusionRule(organization = "org.xerial.snappy", name="snappy-java"),
    ExclusionRule(organization = "javax.servlet")
  ),
  "org.apache.hadoop" % "hadoop-minicluster" % hadoopVersion % Test excludeAll(
    ExclusionRule(organization = "javax.servlet")
  ),
  "org.xerial.snappy" % "snappy-java" % "1.0.5" % Test
)
 
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
//    case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
    case PathList("org", "joda", "time", xs @ _*) => MergeStrategy.last
    case PathList("parquet", xs @ _*) => MergeStrategy.last
    case PathList("javax", "xml", xs @ _*) => MergeStrategy.last
    case PathList("org", "apache", xs @ _*) => MergeStrategy.last
    case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
    case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("au", "com", "bytecode", xs @ _*) => MergeStrategy.last
    case PathList("org", "codehaus", "jackson", xs @ _*) => MergeStrategy.last
    case PathList("jodd", xs @ _*) => MergeStrategy.last
//    case PathList("org", "slf4j", xs @ _*) => MergeStrategy.last
//    case PathList("org", "eclipse", "aether", "META-INF/sisu/javax.inject.Named") => MergeStrategy.last
//    case PathList("com", "healthmarketscience", "jackcess", "log4j.properties") => MergeStrategy.discard
//    case "META-INF/sisu/javax.inject.Named" => MergeStrategy.rename
    case "META-INF/native/libnetty-transport-native-epoll.so" => MergeStrategy.rename
    case "META-INF/io.netty.versions.properties" => MergeStrategy.rename
//    case "sbt/sbt.autoplugins" => MergeStrategy.rename
//    case "about.html" => MergeStrategy.rename
//    case "plugin.properties" => MergeStrategy.rename
    case "overview.html" => MergeStrategy.rename
    case "plugin.xml" => MergeStrategy.rename
//    case "reference.conf" => MergeStrategy.first
    case x => old(x)
  }
}
 
artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}
 
addArtifact(artifact in (Compile, assembly), assembly)
 
parallelExecution in Test := false
 
overridePublishBothSettings
 
publishMavenStyle := true
 
publishTo := {
  val nexus = "http://yoursonatypenexusrepo:8081/nexus/content/repositories/"
  if (isSnapshot.value) {
    Some("internal (s)nexus SNAPSHOT repo" at nexus + "snapshots")
  } else {
    Some("internal (s)nexus repo" at nexus + "releases")
  }
}
 
credentials += Credentials(Path("~/.ivy2/.credentials").asFile)

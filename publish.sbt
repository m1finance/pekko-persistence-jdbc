val ArtifactRepo = {
  sys.env.get("ARTIFACTORY_REPO") match {
    case Some(r) => "Artifactory-Releases".at(r)
    case _       => sys.error(s"Required repo location not found at ${sys.env.get("ARTIFACTORY_REPO")}")
  }
}
val SnapshotsRepo = {
  sys.env.get("ARTIFACTORY_SNAPSHOT_REPO") match {
    case Some(sr) => "Artifactory-Snapshots".at(sr)
    case _        => sys.error(s"Required snapshot repo location not found at ${sys.env.get("ARTIFACTORY_SNAPSHOT_REPO")}")
  }
}

val publishToEnv = sys.env.get("ARTIFACTORY_REPO_PUBLISH_TO")

(ThisBuild / publishTo) := Some(publishToEnv.fold(SnapshotsRepo)(p =>
  if (p == "SNAPSHOT") {
    SnapshotsRepo
  } else {
    ArtifactRepo
  }))

// Sets credentials for Artifactory for all projects. Requires Env Vars or Credentials File
(ThisBuild / credentials) += {
  val credFilePath: File = sbt.io.Path.userHome / ".sbt" / ".credentials"

  (sys.env.get("ARTIFACTORY_USER"), sys.env.get("ARTIFACTORY_API_KEY"), sys.env.get("ARTIFACTORY_REALM")) match {
    case (Some(u), Some(k), Some(r)) => Credentials("Artifactory Realm", r, u, k)
    case _ if credFilePath.exists()  => Credentials(credFilePath)
    case _                           => sys.error(s"Required credentials not found in ENV VARS or $credFilePath")
  }
}

// sets both Artifactory Repositories as new resolvers
ThisBuild / resolvers ++= ArtifactRepo :: SnapshotsRepo :: Nil

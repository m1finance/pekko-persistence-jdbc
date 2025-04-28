// For full release uncomment lines below and ensure proper version is designated
ThisBuild / version ~= { v =>
  sys.env.get("ARTIFACTORY_REPO_PUBLISH_VERSION_PEKKO_PERSISTENCE_JDBC") match {
    case Some(pv) => pv
    case None     => v
  }
}
ThisBuild / isSnapshot := !sys.env.get("ARTIFACTORY_REPO_PUBLISH_TO").contains("RELEASE")

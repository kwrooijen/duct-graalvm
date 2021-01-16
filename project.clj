(defproject simple "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [duct/core "0.8.0"]
                 ;; [duct/module.ataraxy "0.3.0"]
                 [duct/module.logging "0.5.0"]
                 [duct/module.sql "0.6.0"]
                 [duct/module.web "0.7.0"]
                 [duct/server.http.http-kit "0.1.4"]
                 [org.postgresql/postgresql "42.2.12"]
                 [nl.mediquest/duct.module.reitit "1.0.1"]

                 ;; HikariCP Fork: https://github.com/leafclick/HikariCP
                 ;; Need to build locally
                 [duct/database.sql.hikaricp "0.4.0" :exclusions [hikari-cp com.zaxxer/HikariCP org.slf4j/slf4j-api]]

                 ;; Latest
                 ;; [hikari-cp "2.13.0"]
                 ;; [com.zaxxer/HikariCP "3.4.5"]

                 ;; GraalVM Fix
                 [com.zaxxer/HikariCP "3.3.2-graal-RC4"]
                 [hikari-cp "1.8.3" :exclusions [com.zaxxer/HikariCP org.slf4j/slf4j-api]]
                 ]
  :plugins [[duct/lein-duct "0.12.1"]
            [io.taylorwood/lein-native-image "0.3.1"]]
  :main ^:skip-aot simple.main
  :resource-paths ["resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]
  :source-paths ["src"]

  :native-image {:opts ["--allow-incomplete-classpath"
                        "--enable-url-protocols=http,https"
                        "--initialize-at-build-time"
                        "--initialize-at-run-time=com.jcraft.jsch.agentproxy.connector.PageantConnector$User32"
                        "--initialize-at-run-time=com.sun.jna.platform.win32.Kernel32"
                        "--initialize-at-run-time=com.sun.jna.platform.win32.User32"
                        "--initialize-at-run-time=org.apache.commons.logging.LogAdapter$Log4jLog"
                        "--initialize-at-run-time=org.eclipse.jgit.transport.WalkEncryption$Vals"
                        "--no-fallback"
                        "--no-server"
                        "--report-unsupported-elements-at-runtime"
                        "--verbose"
                        "-Dclojure.compiler.direct-linking=true"
                        "-H:+ReportExceptionStackTraces"
                        "-H:IncludeResources=.*.edn"
                        "--initialize-at-run-time=org.postgresql.sspi.SSPIClient"
                        ;; "--initialize-at-run-time=simple.main"


                        ;; HikariCP
                        "-Djava.library.path=$GRAAL_HOME/jre/lib/amd64/"
                        "-H:ReflectionConfigurationFiles=graal.json"
                        "-H:IncludeResources='.*\\.(properties|edn)'"
                        "-H:IncludeResources='com/ibm/icu/impl/data/icudt58b/.*'"
                        "-H:IncludeResources='com/ibm/icu/impl/duration/impl/data/icudt58b/.*'"
                        "-H:IncludeResources='com/ibm/icu/.*\\.properties'"
                        "-H:IncludeResources='com/github/fge/.*\\.properties'"
                        "-H:IncludeResources='PGMIG_VERSION'"
                        "--initialize-at-run-time=org.postgresql.sspi.NTDSAPI"
                        "--initialize-at-run-time=com.sun.jna.platform.win32.Secur32"
                        "--initialize-at-run-time=com.sun.jna.platform.win32.Kernel32"
                        ;; "--initialize-at-run-time='java.lang.Math$RandomNumberGeneratorHolder'"
                        ]}

  :uberjar-name "simple-main.jar"

  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]

  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all
             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
             :resource-paths ["resources"]}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :plugins [[lein-shell "0.5.0"]]
                  :dependencies   [[integrant/repl "0.3.1"]
                                   [eftest "0.5.9"]
                                   [kerodon "0.9.1"]
                                   [hawk "0.2.11"]]}})

(defproject simple "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [duct/core "0.8.0"]
                 [duct/module.ataraxy "0.3.0"]
                 [duct/module.logging "0.5.0"]
                 [duct/module.sql "0.6.0"]
                 [duct/module.web "0.7.0"]
                 [duct/server.http.http-kit "0.1.4"]
                 [org.postgresql/postgresql "42.2.12"]]
  :plugins [[duct/lein-duct "0.12.1"]]
  :main ^:skip-aot simple.main
  :resource-paths ["resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]

  :aliases
  {"native"
   ["shell"
    "native-image" "--report-unsupported-elements-at-runtime" "--no-server"
    "--initialize-at-build-time"
    "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
    "-H:Name=./target/${:name}"]

   "run-native" ["shell" "./target/${:name}"]}

  :uberjar-name "simple-main.jar"

  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :repl-options {:init-ns user}}
   :uberjar {:aot :all
             :resource-paths ["resources" "resources/simple"]}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :plugins [[lein-shell "0.5.0"]]
                  :dependencies   [[integrant/repl "0.3.1"]
                                   [eftest "0.5.9"]
                                   [kerodon "0.9.1"]
                                   [hawk "0.2.11"]]}})

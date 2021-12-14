(defproject ch-tools "0.0.1"
  :description "Clickhouse database tools"
  :url "https://github.com/i-v-s/ch-tools"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.2.1"]
                 [com.github.housepower/clickhouse-native-jdbc "2.6.2"]]
  :profiles
  {:uberjar {:aot :all
             :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
   :dev {:source-paths ["dev" "src" "test"]
         :dependencies [[org.clojure/tools.namespace "0.2.11"]]}})

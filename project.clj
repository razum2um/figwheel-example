(defproject figwheel-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http-kit "2.1.19"]
                 [org.omcljs/om "1.0.0-alpha4"]
                 [figwheel-sidecar "0.5.0-SNAPSHOT"]

                 [reloaded.repl "0.2.1"]
                 [ring/ring-devel "1.4.0"]
                 [aprint "0.1.3"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :checkout-deps-shares ^:replace [:source-paths :resource-paths :compile-path]

  ;; customized
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs" "src/cljc"]
                        :figwheel true
                        :compiler {:output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :source-map true
                                   :optimizations :none
                                   :source-map-timestamp true
                                   :asset-path "/js/out"
                                   :cache-analysis true
                                   :main "figwheel-example.core"}}]} 
  )

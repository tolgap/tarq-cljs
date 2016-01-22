(defproject tarq-cljs "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async "0.2.374"]
                 [environ "1.0.1"]
                 [sablono "0.3.6"]
                 [secretary "1.2.3"]
                 [figwheel "0.5.0-1"]
                 [figwheel-sidecar "0.5.0-1"]
                 [venantius/accountant "0.1.6"]
                 [cljs-http "0.1.38"]
                 [org.omcljs/om "0.9.0"]
                 [prismatic/dommy "1.1.0"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-1"]
            [lein-environ "1.0.1"]
            [lein-doo "0.1.5-SNAPSHOT"]]

  :source-paths ["src"]
  :test-paths ["test"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :doo {:build "test"
        :paths {:phantom "phantomjs"}
        :alias {:browsers [:chrome :firefox]
                :all [:browsers :headless]}}

  :jvm-opts ["-Xmx1g"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src" "env/dev/src"]

                :figwheel {:on-jsload "tarq-cljs.core/on-js-reload"}

                :compiler {:main tarq-cljs.dev
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/tarq_cljs.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}
               ;; This next build is an compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src" "env/prod/src"]
                :compiler {:output-to "resources/public/js/compiled/tarq_cljs.js"
                           :main tarq-cljs.prod
                           :optimizations :advanced
                           :pretty-print false}}
               {:id "test"
                :source-paths ["src" "test"]
                :compiler {:output-to "resources/test/js/compiled/testable.js"
                           :main tarq-cljs.runner
                           :optimizations :none}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             :ring-handler tarq-cljs.static-server/handler
             })

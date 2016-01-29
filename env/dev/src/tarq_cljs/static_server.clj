(ns tarq-cljs.static-server
  (:require [clojure.java.io :as io]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (io/file "resources/public/index.html")})

(ns ^:figwheel-always tarq-cljs.api
  (:require-macros [tarq-cljs.env :refer [cljs-env]])
  (:require [clojure.string :as string]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! chan]]))

(def api-url (cljs-env :api-url))

(def servers-path
  (string/join "/" [api-url "servers"]))

(def websites-path
  (string/join "/" [api-url "websites"]))

(defn website-path [server-id id]
  (string/join "/" [api-url "servers" server-id "websites" id]))

(def vulnerabilities-path
  (string/join "/" [api-url "vulnerabilities"]))

(defn plugin-vulnerabilities-path [website-id plugin-id]
  (string/join "/" [api-url "websites" website-id "plugins" plugin-id "vulnerabilities"]))

(defn json-to [path]
  (http/get path {:with-credentials? false
                  :accepts :json
                  :channel (chan 1 (map :body))}))


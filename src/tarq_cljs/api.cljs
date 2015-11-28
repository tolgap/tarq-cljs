(ns ^:figwheel-always tarq-cljs.api
  (:require-macros [tarq-cljs.env :refer [cljs-env]])
  (:require [clojure.string :as string]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(def api-url (cljs-env :api-url))

(def servers-path
  (string/join "/" [api-url "servers"]))

(def websites-path
  (string/join "/" [api-url "websites"]))

(defn json-to [path]
  (http/get path {:with-credentials? false}))


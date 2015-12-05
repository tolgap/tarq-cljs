(ns tarq-cljs.common-test
  (:require [dommy.core :as dommy :refer-macros [sel1]]))

(defn append-container! []
  (let [container (.createElement js/document "DIV")]
    (set! (.-id container) "app")
    (dommy/append! (sel1 js/document :body) container)
    (sel1 "#app")))

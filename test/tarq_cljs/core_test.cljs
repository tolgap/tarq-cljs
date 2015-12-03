(ns tarq-cljs.core-test
  (:require [tarq-cljs.core :as core]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing]]))

(defn append-container! []
  (let [container (.createElement js/document "DIV")]
    (set! (.-id container) "app")
    (dommy/append! (sel1 js/document :body) container)
    (sel1 "#app")))

(deftest not-found-page
  (testing "404 component"
    (is (= "404"
           (let [c (append-container!)]
             (om/root core/not-found-page {:page nil} {:target c})
             (dommy/text (sel1 c :h1)))))))

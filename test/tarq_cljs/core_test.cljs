(ns tarq-cljs.core-test
  (:require [tarq-cljs.core :as core]
            [tarq-cljs.common-test :refer [append-container!]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest not-found-page
  (testing "404 component"
    (is (= "404"
           (let [c (append-container!)]
             (om/root core/not-found-page {:page nil} {:target c})
             (dommy/text (sel1 c :h1)))))))

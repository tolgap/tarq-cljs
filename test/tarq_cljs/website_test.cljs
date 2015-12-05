(ns tarq-cljs.website-test
  (:require [tarq-cljs.components.website :as website]
            [tarq-cljs.common-test :refer [append-container!]]
            [dommy.core :as dommy :refer-macros [sel1]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest website-list-item
  (testing "website list component"
    (is (= "some website name"
           (let [c (append-container!)
                 data {:name "some website name"
                       :server_id 4
                       :id 19}]
             (om/root website/website-list-item data {:target c})
             (dommy/text (sel1 c :li.collection-item)))))))

(ns tarq-cljs.website-test
  (:require [tarq-cljs.components.website :as website]
            [tarq-cljs.api :as api]
            [tarq-cljs.common-test :refer [append-container!]]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest website-list-item
  (testing "website name"
    (is (= "some website name"
           (let [c (append-container!)
                 data {:name "some website name"
                       :server_id 4
                       :id 19}]
             (om/root website/website-list-item data {:target c})
             (dommy/text (sel1 c :li.collection-item))))))
  (testing "website url"
    (is
     (not
      (empty?
       (let [c (append-container!)
             data {:name "website 2"
                   :server_id 5
                   :id 1}]
         (om/root website/website-list-item data {:target c})
         (str (dommy/attr (sel1 c :a) :href))))))))

(deftest website-list
  (testing "show 2 websites"
    (is (= 2
           (let [c (append-container!)
                 data [{:name "website 1" :server_id 1 :id 1}
                       {:name "website 2" :server_id 2 :id 20}]]
             (om/root website/websites-list data {:target c})
             (count (sel c :li.collection-item)))))))

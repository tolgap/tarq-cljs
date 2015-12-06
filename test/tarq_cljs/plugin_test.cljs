(ns tarq-cljs.plugin-test
  (:require [tarq-cljs.components.plugin :as plugin]
            [tarq-cljs.common-test :refer [append-container!]]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest plugin-list-item
  (testing "shows name"
    (is (= "some plugin name"
           (let [c (append-container!)
                 data {:name "some plugin name"}]
             (om/root plugin/plugin-list-item data {:target c})
             (dommy/text (sel1 c :li.collection-item.plugin)))))))

(deftest plugins-list
  (testing "shows expand button"
    (is (= "Show plugins"
           (let [c (append-container!)
                 data [{:name "plugin-1"}
                       {:name "plugin-2"}]]
             (om/root plugin/plugins-list data {:target c})
             (dommy/text (sel1 c :a)))))))

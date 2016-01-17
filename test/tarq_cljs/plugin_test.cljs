(ns tarq-cljs.plugin-test
  (:require [tarq-cljs.components.plugin :as plugin]
            [tarq-cljs.common-test :refer [append-container!]]
            [tarq-cljs.api :as api]
            [cljs.core.async :refer [put! chan]]
            [dommy.core :as dommy :refer-macros [sel1 sel]]
            [om.core :as om :include-macros true]
            [cljs.test :refer-macros [deftest is testing async]]))

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
             (dommy/text (sel1 c :a))))))
  (testing "shows the 2 plugins"
    (is (= 2
           (let [c (append-container!)
                 data [{:name "plugin-1"}
                       {:name "plugin-2"}]]
             (om/root plugin/plugins-list data {:target c :init-state {:expanded? true}})
             (count (sel c :li.collection-item.plugin)))))))

(deftest ^:async plugin-table-item
  (testing "should show name, version and vulns"
    (async done
           (with-redefs
             [api/json-to (fn [path]
                            (.log js/console "PLEASE")
                            (let [mock-ch (chan 1)]
                              (put! mock-ch ["1" "2"])
                              mock-ch))]
             (let [c (append-container!)
                   data {:name "plugin"
                         :version "0.0.1"}]
               (om/root plugin/plugin-table-item data {:target c})
               (is (= "plugin0.0.12"
                      (dommy/text c)))
               (done))))))

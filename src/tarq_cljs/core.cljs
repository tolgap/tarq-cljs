(ns ^:figwheel-always tarq-cljs.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.api :as api]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:websites []}))

(defn log [elem]
  (.log js/console (pr-str elem)))

(defn website-list-item [{:keys [name]} owner]
  (reify
    om/IRender
    (render [_]
      (html [:li.list-item name]))))

(defn website-list [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (go
        (let [response (api/json-to api/websites-path)
              websites ((<! response) :body)]
          (om/update! data nil websites))))
    om/IRender
    (render [_]
      (html [:ul (om/build-all website-list-item data)]))))

(defn websites-box [data owner]
  (om/component
   (html [:div.website-box (om/build website-list (data :websites))])))

(om/root
  (fn [data owner]
    (reify om/IRender
      (render [_]
        (om/build websites-box data))))
  app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

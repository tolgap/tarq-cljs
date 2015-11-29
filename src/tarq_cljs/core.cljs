(ns ^:figwheel-always tarq-cljs.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [accountant.core :as accountant]
            [tarq-cljs.api :as api]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:websites [] :page :websites}))

(accountant/configure-navigation!)

(defroute servers-path "/servers" []
  (swap! app-state assoc :page :servers))

(defroute websites-path "/" []
  (swap! app-state assoc :page :websites))

(defroute "*" []
  (swap! app-state assoc :page :404))

(accountant/dispatch-current!)

(defn log [elem]
  (.log js/console (pr-str elem)))

(defn website-list-item [{:keys [name]} owner]
  (reify
    om/IRender
    (render [_]
      (html [:li.list-item name]))))

(defn websites-list [data owner]
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

(defn websites-page [data owner]
  (om/component
   (html [:div#website-page
          (om/build websites-list (data :websites))
          [:a {:href (servers-path)} "servers"]])))

(defn servers-page [data owner]
  (om/component
   (html [:div#server-page
          [:h1 "server component"]
          [:a {:href (websites-path)} "websites"]])))

(defn not-found-page [data owner]
  (om/component
   (html [:div#not-found-page
          [:h1 "404"]
          [:a {:href (websites-path)} "websites"]
          [:a {:href (servers-path)} "servers"]])))

(om/root
  (fn [data owner]
    (reify om/IRender
      (render [_]
        (let [page (data :page)]
          (condp = page
            :websites (om/build websites-page data)
            :servers (om/build servers-page data)
            (om/build not-found-page data))))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

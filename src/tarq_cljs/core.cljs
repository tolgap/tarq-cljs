(ns ^:figwheel-always tarq-cljs.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.materialize.toolbar :as toolbar]
            [tarq-cljs.state :refer [app-state]]
            [tarq-cljs.routing :as path]
            [tarq-cljs.api :as api]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn log [elem]
  (.log js/console (pr-str elem)))

(defn website-list-item [{:keys [server_id id name]} owner]
  (reify
    om/IRender
    (render [_]
      (html [:li.collection-item
             [:a {:href (path/website {:server-id server_id :id id})} name]]))))

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
      (html [:ul.collection.with-header
             [:li.collection-header [:h4 "Websites"]]
             (om/build-all website-list-item data)]))))

(defn websites-page [data owner]
  (om/component
   (html [:div#website-page
          [:div.container
           (om/build websites-list (data :websites))
           [:a {:href (path/servers)} "servers"]]])))

(defn website-page [data owner {:keys [server-id id]}]
  (om/component
   (html [:div#website-detail
          [:p (str "Website id: " id)]
          [:p (str "Server id: " server-id)]])))

(defn servers-page [data owner]
  (om/component
   (html [:div#server-page
          [:h1 "server component"]
          [:a {:href (path/websites)} "websites"]])))

(defn not-found-page [data owner]
  (om/component
   (html [:div#not-found-page
          [:h1 "404"]
          [:a {:href (path/websites)} "websites"]
          [:a {:href (path/servers)} "servers"]])))

(om/root
  (fn [data owner]
    (reify om/IRender
      (render [_]
        (let [page (data :page)
              params (data :params)]
          (html [:div#page
                 (om/build toolbar/generate data {:opts {:title "Tarq"
                                                         :items []}})
                 (condp = page
                   :websites (om/build websites-page data)
                   :website (om/build website-page data {:opts params})
                   :servers (om/build servers-page data)
                   (om/build not-found-page data))])))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

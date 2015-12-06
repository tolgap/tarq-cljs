(ns ^:figwheel-always tarq-cljs.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.materialize.toolbar :as toolbar]
            [tarq-cljs.components.website :as website]
            [tarq-cljs.state :refer [app-state]]
            [tarq-cljs.routing :as path]))

(defn log [elem]
  (.log js/console (pr-str elem)))

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

(defn app []
  (om/root
   (fn [data owner]
     (reify om/IRender
       (render [_]
         (let [page (data :page)
               params (data :params)]
           (html [:div#page
                  (om/build toolbar/generate
                            data
                            {:opts {:title (html [:a.brand-logo {:href (path/websites)} "Tarq"])
                                    :items []}})
                  (condp = page
                    :websites (om/build website/websites-page data)
                    :servers (om/build servers-page data)
                    (om/build not-found-page data))])))))
   app-state
   {:target (. js/document (getElementById "app"))}))

(ns ^:figwheel-always tarq-cljs.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.materialize.toolbar :as toolbar]
            [tarq-cljs.components.website :as website]
            [tarq-cljs.components.vulnerability :as vulnerability]
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

(def menu-items
  {:title (html [:a.brand-logo {:href (path/websites)} "Tarq"])
   :items [{:name "Websites" :url (path/websites)}
           {:name "Vulnerabilities" :url (path/vulnerabilities)}]
   :item-fn toolbar/title-item})

(defn app []
  (om/root
   (fn [data owner]
     (reify om/IRender
       (render [_]
         (let [page (data :page)
               params (data :params)]
           (html [:div#page
                  (om/build toolbar/with-text-items page {:opts menu-items})
                  (condp = page
                    :websites (om/build website/websites-page data)
                    :servers (om/build servers-page data)
                    :vulnerabilities (om/build vulnerability/vulnerabilities-page (data :vulnerabilities))
                    (om/build not-found-page data))])))))
   app-state
   {:target (. js/document (getElementById "app"))}))

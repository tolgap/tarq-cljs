(ns ^:figwheel-always tarq-cljs.components.plugin
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.state :refer [app-state]]
            [tarq-cljs.routing :as path]
            [tarq-cljs.api :as api]))

(defn plugin-list-item [data owner]
  (om/component
   (html
    [:li.collection-item.plugin (data :name)])))

(defn plugins-list [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:expanded? false})
    om/IRenderState
    (render-state [_ {:keys [expanded?]}]
      (html
       (if expanded?
         [:div
          [:a {:onClick (fn [_] (om/set-state! owner :expanded? false))} "Hide plugins"]
          [:ul.collection.with-header
           [:li.collection-header [:h4 "Plugins"]]
           (om/build-all plugin-list-item data)]]
         [:a {:onClick (fn [_] (om/set-state! owner :expanded? true))} "Show plugins"])))))

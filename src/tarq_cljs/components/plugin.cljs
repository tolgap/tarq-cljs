(ns ^:figwheel-always tarq-cljs.components.plugin
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [<!]]
            [sablono.core :as html :refer-macros [html]]
            [tarq-cljs.state :refer [app-state]]
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

(defn plugin-table-item [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:vulnerability-count 0})
    om/IWillMount
    (will-mount [_]
      (go
        (let [response (<! (api/json-to (api/plugin-vulnerabilities-path (data :website_id) (data :plugin_id))))]
          (om/set-state! owner :vulnerability-count (count response)))))
    om/IRenderState
    (render-state [_ {:keys [vulnerability-count]}]
      (html
       [:tr
        [:td (data :name)]
        [:td (data :version)]
        [:td vulnerability-count]]))))

(defn plugins-table [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:expanded? true})
    om/IRenderState
    (render-state [_ {:keys [expanded?]}]
      (html
       (if expanded?
         [:div
          [:a {:onClick (fn [_] (om/set-state! owner :expanded? false))} "Hide plugins"]
          [:table
           [:thead
            [:tr
             [:th "Name"]
             [:th "Version"]
             [:th "Num. vulnerabilites"]]]
           [:tbody
            (om/build-all plugin-table-item data)]]]
         [:a {:on-click (fn [_] (om/set-state! owner :expanded? true))} "Show plugins"])))))

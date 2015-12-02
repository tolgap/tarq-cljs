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
      (html [:ul#website-list.collection.with-header.z-depth-2.col.s4
             [:li.collection-header [:h4 "Websites"]]
             (om/build-all website-list-item data)]))))

(defn website-compact-view [data owner]
  (reify
    om/IRender
    (render [_]
      (html [:div {:id (str "website-" (data :id))}
             [:p (str "Website id: " (data :id))]
             [:p (str "Server id: " (data :server_id))]]))))

(defn load-website [props data owner]
  (om/set-state! owner [:loading] true)
  (go
    (let [server-id (props :server-id)
          id (props :id)
          response (api/json-to (api/website-path server-id id))
          website ((<! response) :body)]
      (om/update! data [:current-website] website)
      (om/set-state! owner [:loading] false))))

(defn website-detail [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:loading true})
    om/IWillMount
    (will-mount [_]
      (load-website data data owner))
    om/IWillReceiveProps
    (will-receive-props [_ next-props]
      (load-website next-props data owner))
    om/IRenderState
    (render-state [_ {:keys [loading]}]
      (html
       (if loading
         [:p "Loading..."]
         (om/build website-compact-view (data :current-website)))))))

(defn websites-page [data owner]
  (reify
    om/IRender
    (render [_]
      (html [:div#website-page
             [:div.container
              [:div.row
               (om/build websites-list (data :websites))
               [:div#website-detail.z-depth-2.col.s5
                (if (empty? (data :params))
                  [:p "Choose a website"]
                  (om/build website-detail (data :params)))]]]]))))

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
                 (om/build toolbar/generate
                           data
                           {:opts {:title (html [:a.brand-logo {:href (path/websites)} "Tarq"])
                                   :items []}})
                 (condp = page
                   :websites (om/build websites-page data)
                   :servers (om/build servers-page data)
                   (om/build not-found-page data))])))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

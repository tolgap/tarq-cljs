(ns ^:figwheel-always tarq-cljs.components.website
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! <! chan]]
            [sablono.core :as html :refer-macros [html]]
            [clojure.string :as string]
            [tarq-cljs.state :refer [app-state]]
            [tarq-cljs.routing :as path]
            [tarq-cljs.api :as api]
            [tarq-cljs.components.plugin :as plugin]))

(defn get-node-value [owner ref]
  (-> (om/get-node owner ref)
      .-value))

;; This components get passed a `(chan)` to push changes to in `(will-receive-props)`
(defn website-filter [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:cms_type nil :name nil})
    om/IWillReceiveProps
    (will-receive-props [_ next-props]
      (go
        (let [channel (om/get-state owner :channel)]
          (put! channel (merge data next-props)))))
    om/IRender
    (render [_]
      (html
       [:div.container
        [:div.row
         [:div#filter.z-depth-2
          [:div.input-field.col.s3
           [:input {:ref "search-input"
                    :type "text"
                    :placeholder "Search"
                    :value (data :name)
                    :on-change (fn [_]
                                 (om/update! data :name (get-node-value owner "search-input")))}]]
          [:div.input-field.col.s3
           [:select.browser-default {:ref "cms-type"
                                     :value (data :cms_type)
                                     :placeholder "Choose CMS type"
                                     :on-change (fn [_]
                                                  (om/update! data :cms_type (get-node-value owner "cms-type")))}
            [:option {:value nil :selected "selected"} nil]
            [:option {:value "wordpress"} "Wordpress"]
            [:option {:value "drupal"} "Drupal"]
            [:option {:value "rails"} "Rails"]]]]]]))))

(defn website-list-item [{:keys [server_id id name] :as data} owner]
  (reify
    om/IRender
    (render [_]
      (html [:li.collection-item
             [:a {:href (path/website {:server-id server_id :id id})} name]
             [:div.icons
              [:i {:class (str "icon icon-" (data :cms_type))}]]]))))

(defn websites-list [data owner]
  (reify
    om/IRender
    (render [_]
      (html [:div#website-list.col.s6
             [:ul.collection.with-header.z-depth-2
              [:li.collection-header [:h4 "Websites"]]
              (om/build-all website-list-item data)]]))))

(defn website-compact-view [data owner]
  (reify
    om/IRender
    (render [_]
      (html [:div {:id (str "website-" (data :id))}
             [:p (str "Website id: " (data :id))]
             [:p (str "Server id: " (data :server_id))]
             (om/build plugin/plugins-list (data :plugins))]))))

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

(defn regex-value [[k expected-rex]]
  (fn [c] (re-find (re-pattern expected-rex)
                   (string/lower-case (get c k)))))

(defn build-filter [filters]
  (apply every-pred (map regex-value filters)))

(defn perform-filter [data original filters]
  (if (not (empty? filters))
    (om/update! data :search-set (filterv (build-filter filters) original))
    (om/update! data :search-set original)))

(defn listen-and-process-search-filters [data owner]
  (go
    (let [response (api/json-to api/websites-path)
          websites ((<! response) :body)
          channel (om/get-state owner :filter-channel)]
      (om/update! data :search-set websites)
      (om/update! data :websites websites)
      (loop []
        (let [filters (<! channel)]
          (perform-filter data websites filters)
          (recur))))))

(defn websites-page [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:filter-channel (chan)})
    om/IWillMount
    (will-mount [_]
      (listen-and-process-search-filters data owner))
    om/IRenderState
    (render-state [_ {:keys [filter-channel]}]
      (html [:div#website-page
             (om/build website-filter (data :filters) {:init-state {:channel filter-channel}})
             [:div.container
              [:div.row
               (om/build websites-list (data :search-set))
               [:div#website-detail.collection.z-depth-2.col.s6
                (if (empty? (data :params))
                  [:p "Choose a website"]
                  (om/build website-detail (data :params)))]]]]))))

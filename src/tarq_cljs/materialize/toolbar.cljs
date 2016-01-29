(ns tarq-cljs.materialize.toolbar
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(defn dropdown-item [name]
  (html [:a [:li
             [:i.material-icons name]]]))

(defn title-item [{:keys [name url]}]
  (html [:li
         [:a {:href url} name]]))

(defn with-text-items [owner data {:keys [title items item-fn]}]
  (om/component
   (html [:nav
          [:div.nav-wrapper
           title
           [:ul.right.hide-on-med-and-down
            (map item-fn items)]]])))

(defn generate [owner data {:keys [title items]}]
  (om/component
   (html [:nav
          [:div.nav-wrapper
           title
           [:ul.right.hide-on-med-and-down
            [:a [:li
             [:i.material-icons "more_vert"]]]
            (map dropdown-item items)]]])))

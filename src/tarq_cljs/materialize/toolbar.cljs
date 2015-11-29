(ns tarq-cljs.materialize.toolbar
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(defn dropdown-item [name]
  (html [:a [:li
         [:i.material-icons name]]]))

(defn generate [owner data {:keys [title items]}]
  (om/component
   (html [:nav
          [:div.nav-wrapper
           [:a.brand-logo title]
           [:ul.right.hide-on-med-and-down
            [:a [:li
             [:i.material-icons "more_vert"]]]
            (map dropdown-item items)]]])))

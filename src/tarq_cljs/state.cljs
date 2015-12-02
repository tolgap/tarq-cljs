(ns tarq-cljs.state)

(defonce app-state
  (atom {:page :websites
         :params {}
         :websites []}))

(ns tarq-cljs.routing
  (:require [tarq-cljs.state :refer [app-state]]
            [secretary.core :as secretary :refer-macros [defroute]]
            [accountant.core :as accountant]))

(accountant/configure-navigation!)

(defroute servers "/servers" []
  (swap! app-state assoc
         :page :servers))

(defroute website "/servers/:server-id/websites/:id" [server-id id]
  (swap! app-state assoc
         :page :websites
         :params {:server-id server-id
                  :id id
                  :current-website {}}))

(defroute websites "/" []
  (swap! app-state assoc
         :page :websites))

(defroute "*" []
  (swap! app-state assoc
         :page :404))

(accountant/dispatch-current!)
